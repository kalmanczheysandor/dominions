package hu.kalmancheysandor.applications.dominions.apis.ai.algorithm.simultan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import hu.kalmancheysandor.applications.dominions.apis.ai.general.common.AiDecisionContext;
import hu.kalmancheysandor.applications.dominions.apis.ai.general.common.AiDecisionResult;
import hu.kalmancheysandor.applications.dominions.apis.ai.general.common.TAiAlgorithm;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.action.GameAction;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.machine.GameStateMachine;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;
import hu.kalmancheysandor.applications.dominions.apis.general.exceptions.IllegalPointOfExecution;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

import static hu.kalmancheysandor.applications.dominions.apis.util.validate.asserted.VariableAssert.*;

public abstract class TSimultaNAlgorithm extends TAiAlgorithm implements ISimultaNAlgorithm {
    private static final Logger log = LoggerFactory.getLogger(TSimultaNAlgorithm.class);
    protected GameStateMachine gameStateMachine;
    private int searchDepthLimit = 0;

    protected abstract double evaluateHeuristicValueOfGameState(GameState gameState, int supportedPlayerKey);

    private static int countOfLeafNodes = 0;

    @Override
    public AiDecisionResult makeDecision(AiDecisionContext context) {
        // Parameter validations
        assertObjectIsNotNull(context, "context");
        assertIntIsNotUnderMinimum(getSearchDepthLimit(),"searchDepthLimit",1);

        // Gain the best step
        Integer chosenOperatorKey = expandState(
                context.getGameState(), // Initialstate of recursive caluclation
                context.getYourPlayerKey(), // Supported player key
                this.searchDepthLimit, // Maximum deepness
                0  // Parent level deepness
        ).getAdvisedStepKey();

        System.out.println("countOfLeafNodes: " + countOfLeafNodes);


        if (chosenOperatorKey == null) {
            throw new RuntimeException("Decision operator key is: null");
        }




        // Generate return value
        AiDecisionResult aiDecisionResult;
        if (chosenOperatorKey == 0) {// When: skip
            aiDecisionResult = new AiDecisionResult(null, 0);
        }
        else { // When: attack
            aiDecisionResult = new AiDecisionResult(
                    chosenOperatorKey-1,
                    calculatePlayerMaxAttackPower(
                            context.getGameState(),
                            context.getYourPlayerKey()
                    )
            );
        }
        System.out.println("AiDecisionResult: " + aiDecisionResult);
        return aiDecisionResult;
    }



    private ChosenStepData expandState(GameState gameState, int supportedPlayerKey, int maxDeepness, int parentLevelDeepness) {
        // Parameter validations
        assertObjectIsNotNull(gameState, "gameState");
        assertIntNotNegative(supportedPlayerKey, "supportedPlayerKey");
        assertIntIsNotUnderMinimum(maxDeepness, "maxDeepness", 1);
        assertIntNotNegative(parentLevelDeepness, "parentLevelDeepness");

        //
        int currentLevel = parentLevelDeepness + 1;


        // Collects all alive players' all possible ElementaryOperations, which are applicable in the current game state
        Set<ElementaryOperator> allElementaryOperatorsOfAlivePlayers = gatherApplicableElementaryOperatorsOfAlivePlayers(gameState);

        // Generate all possible composite operators based on a game state
        List<CompositeOperator> allCompositeOperators = generateAllCompositeOperators(supportedPlayerKey, allElementaryOperatorsOfAlivePlayers);

        // Split composite operators into target-groups of supported player
        Map<Integer, List<CompositeOperator>> compositeOperatorsOfNodeGroups = splitCompositeOperatorsIntoNodeGroups(allCompositeOperators, supportedPlayerKey);

        Integer stepKey;
        List<GameState> stateVariants;
        double worstHeuristicValue;


        if (currentLevel < maxDeepness) {//// Go deeper by recursion, heuristic values only delegated from child nodes but not evaluated

            Double currentBestHeuristicValue = null;//
            Integer chosenGroupKey = null;

            for (Map.Entry<Integer, List<CompositeOperator>> nodeGroupEntry : compositeOperatorsOfNodeGroups.entrySet()) {
                Integer keyOfNode = nodeGroupEntry.getKey();
                List<CompositeOperator> compositeOperatorsOfNode = nodeGroupEntry.getValue();

                // Generate all possible game state variations based on the composite operators of a node
                List<GameState> stateVariationsOfNode = generateStateVariationsByCompositeOperator(gameState, compositeOperatorsOfNode);

                // Safety checking: when no game state variants are present in the list, then skip that step
                if (stateVariationsOfNode.isEmpty()) {
                    continue;
                }


                ChosenStepData b;
                List<Double> heuristicValues = new ArrayList<>();

                for (GameState gameStateVariant : stateVariationsOfNode) {
                    b = expandState(gameStateVariant, supportedPlayerKey, maxDeepness, currentLevel);
                    heuristicValues.add(b.getDelegatedHeuristicValue());
                }
                double heuristicValueOfNode = findTheWorstValue(heuristicValues); // Among variations the worst scenario is the needed value


                // Always store the elementary operator (of supported player) based on its heuristic value, which has to be better than the previous one.
                if (currentBestHeuristicValue == null) {
                    currentBestHeuristicValue = heuristicValueOfNode;
                    chosenGroupKey = keyOfNode;
                } else if (currentBestHeuristicValue < heuristicValueOfNode) {
                    currentBestHeuristicValue = heuristicValueOfNode;
                    chosenGroupKey = keyOfNode;
                }
            }
            return new ChosenStepData(chosenGroupKey, currentBestHeuristicValue);
        } else if (currentLevel == maxDeepness) { // this is a leaf node. here real heuristic evaluation have to calculated

            Double currentBestHeuristicValue = null;
            Integer chosenGroupKey = null;
            for (Map.Entry<Integer, List<CompositeOperator>> nodeGroupEntry : compositeOperatorsOfNodeGroups.entrySet()) {
                Integer keyOfNode = nodeGroupEntry.getKey();
                List<CompositeOperator> compositeOperatorsOfNode = nodeGroupEntry.getValue();

                // Generate all possible game state variations based on the composite operators of a node
                List<GameState> stateAlternativesInNode = generateStateVariationsByCompositeOperator(gameState, compositeOperatorsOfNode);

                // Safety checking: when no game state variants are present in the list, then skip that step
                if (stateAlternativesInNode.isEmpty()) {
                    continue;
                }

                countOfLeafNodes++;
                List<Double> heuristicValues = evaluateHeuristicValueOfGameSateVariants(stateAlternativesInNode, supportedPlayerKey);
                double heuristicValueOfNode = findTheWorstValue(heuristicValues); // Among variations the worst scenario is the needed value


                // Always store the elementary operator (of supported player) based on its heuristic value, which has to be better than the previous one.
                if (currentBestHeuristicValue == null) {
                    currentBestHeuristicValue = heuristicValueOfNode;
                    chosenGroupKey = keyOfNode;
                } else if (currentBestHeuristicValue < heuristicValueOfNode) {
                    currentBestHeuristicValue = heuristicValueOfNode;
                    chosenGroupKey = keyOfNode;
                }
            }
            return new ChosenStepData(chosenGroupKey, currentBestHeuristicValue);
        }

        throw new IllegalPointOfExecution("Unexpected case is found!");

    }

    private double findTheWorstValue(List<Double> gameStateVariantValues) {
        // Parameter validations
        assertObjectIsNotNull(gameStateVariantValues, "gameStateVariantValues");
        assertCollectionNotEmpty(gameStateVariantValues, "gameStateVariantValues");

        return gameStateVariantValues.stream()
                .mapToDouble(Double::doubleValue)
                .min()
                .orElseThrow();
    }

    private List<Double> evaluateHeuristicValueOfGameSateVariants(List<GameState> gameStateVariants, int supportedPlayerKey) {
        // Parameter validations
        assertObjectIsNotNull(gameStateVariants, "gameStateVariants");
        assertCollectionNotEmpty(gameStateVariants, "gameStateVariants");

        // Loop - All game state variants inside of supported player step
        List<Double> heuristicValues = new ArrayList<>();
        Double currentHeuristicValue;
        for (GameState gameStateVariant : gameStateVariants) {
            currentHeuristicValue = evaluateHeuristicValueOfGameState(
                    gameStateVariant,
                    supportedPlayerKey
            );
            heuristicValues.add(currentHeuristicValue);
        }
        System.out.println("Heuristic values: " + heuristicValues);
        return heuristicValues;
    }


    /// ////////////////////////////////////////////////////////////////////////////////////////
    ///  COMPOSITE OPERATORS
    /// ////////////////////////////////////////////////////////////////////////////////////////
    private List<CompositeOperator> generateAllCompositeOperators(int supportedPlayerKey, Collection<ElementaryOperator> allElementaryOperatorsOfAlivePlayers) {
        // Parameter validations
        assertIntNotNegative(supportedPlayerKey, "supportedPlayerKey");

        return CompositeOperator.createMultiple(
                supportedPlayerKey,
                allElementaryOperatorsOfAlivePlayers
        );
    }

    private Map<Integer, List<CompositeOperator>> splitCompositeOperatorsIntoNodeGroups(List<CompositeOperator> compositeOperators, int supportedPlayerKey) {
        // Parameter validations
        assertObjectIsNotNull(compositeOperators, "compositeOperators");
        assertIntNotNegative(supportedPlayerKey, "supportedPlayerKey");


        List<CompositeOperator> combinationListInAGroup = new ArrayList<>();
        Map<Integer, List<CompositeOperator>> nodeGroups = new HashMap<>();

        for (CompositeOperator compositeOperator : compositeOperators) {

            // Determine group-key
           int  targetedCellKey = compositeOperator.getTargetIndex();

            // Add to related group
            if (!nodeGroups.containsKey(targetedCellKey)) {
                List<CompositeOperator> groupItems = new ArrayList<>();
                groupItems.add(compositeOperator);
                nodeGroups.put(targetedCellKey, groupItems);
            } else {
                List<CompositeOperator> groupItems = nodeGroups.get(targetedCellKey);
                groupItems.add(compositeOperator);
            }
        }
        return nodeGroups;
    }

    private GameState applyOneCompositeOperatorOnGameState(GameState initialGameState, CompositeOperator compositeOperator) {
        // Parameter validations
        assertObjectIsNotNull(initialGameState, "initialGameState");
        assertObjectIsNotNull(compositeOperator, "compositeOperator");


        // Initialise state-machine
        if (this.gameStateMachine == null) {
            this.gameStateMachine = new GameStateMachine();
        }

        // Collect all intentions
        Set<GameAction> actions = new HashSet<>();
        for (ElementaryOperator elementaryOperator : compositeOperator.getCombinations()) {
            int playerKey = elementaryOperator.getPlayerKey();
            int operatorKey = elementaryOperator.getOperatorKey();

            int troopSize = getPlayerReserveSize(initialGameState, playerKey);

            troopSize = (troopSize > TROOPS_SIZE_MAX) ? TROOPS_SIZE_MAX : troopSize;
            if (operatorKey == 0) {//When reserve action
                troopSize = 0;
                actions.add(new GameAction(playerKey, null, troopSize));// Skip action
            }
            else {
                int targetedCellKey =  operatorKey-1;
                actions.add(new GameAction(playerKey,targetedCellKey, troopSize));   // attack action
            }



        }

        // Generate new game-state based on the intentions
        return gameStateMachine.doSteps(actions, GameState.deepCopy(initialGameState));
    }

    private List<GameState> applyMultipleCompositeOperatorOnGameState(GameState initialGameState, List<CompositeOperator> compositeOperatorsList) {
        // Parameter validations
        assertObjectIsNotNull(initialGameState, "initialGameState");
        assertObjectIsNotNull(compositeOperatorsList, "compositeOperatorsList");

        // Execution
        List<GameState> gameStateList = new ArrayList<>();
        for (CompositeOperator compositeOperator : compositeOperatorsList) {
            gameStateList.add(
                    applyOneCompositeOperatorOnGameState(
                            initialGameState,
                            compositeOperator
                    )
            );
        }
        return gameStateList;
    }

    protected List<GameState> generateStateVariationsByCompositeOperator(GameState gameState, List<CompositeOperator> compositeOperatorsList) {
        return applyMultipleCompositeOperatorOnGameState(gameState, compositeOperatorsList);
    }

    /// ////////////////////////////////////////////////////////////////////////////////////////
    ///  ELEMENTARY OPERATORS
    /// ////////////////////////////////////////////////////////////////////////////////////////
    protected Set<ElementaryOperator> gatherApplicableElementaryOperatorsOfAPlayer(GameState gameState, Integer playerKey) {
        // Parameter validations
        assertObjectIsNotNull(gameState, "gameState");
        assertIntNotNegative(playerKey, "playerKey");

        //Adding attack steps
        Set<ElementaryOperator> elementaryOperators = new HashSet<>();
        for (Integer cellKey : collectCellKeysOfCurrentAttackZoneOfPlayer(gameState, playerKey)) {
            elementaryOperators.add(new ElementaryOperator(playerKey, cellKey+1));  // cellKeys are from 0... but first attack operator index is from 1.. and skip operator stays at 0-key
        }
        // Adding the "reserve" step
        elementaryOperators.add(new ElementaryOperator(playerKey, 0));

        return elementaryOperators;
    }

    protected Set<ElementaryOperator> gatherApplicableElementaryOperatorsOfAlivePlayers(GameState gameState) {
        // Parameter validations
        assertObjectIsNotNull(gameState, "gameState");

        // All players key
        Set<Integer> alivePlayerKeys = collectAllAlivePlayerKeys(gameState);

        //
        Set<ElementaryOperator> output = new HashSet<>();
        for (Integer playerKey : alivePlayerKeys) {
            output.addAll(gatherApplicableElementaryOperatorsOfAPlayer(gameState, playerKey));
        }

        return output;
    }


    /// ////////////////////////////////////////////////////////////////////////////////////////
    ///  Getter and Setter
    /// ////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void setSearchDepthLimit(int depthLimit){
        // Parameter checking
        assertIntNotNegative(depthLimit, "searchDepthLimit");
        assertIntIsNotUnderMinimum(depthLimit,"searchDepthLimit",1);

        this.searchDepthLimit = depthLimit;
    }


    public int getSearchDepthLimit() {
        return searchDepthLimit;
    }


    /// ////////////////////////////////////////////////////////////////////////////////////////
    /// // Inner Classes /////////////////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////////
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ChosenStepData {
        Integer advisedStepKey;
        double delegatedHeuristicValue;

    }
}

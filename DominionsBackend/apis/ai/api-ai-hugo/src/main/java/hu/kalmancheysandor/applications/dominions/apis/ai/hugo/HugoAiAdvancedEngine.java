package hu.kalmancheysandor.applications.dominions.apis.ai.hugo;

import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionContext;
import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionResult;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.IHeuristicAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.IHeuristicEvaluation;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.IHeuristicEvaluator;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;
import hu.kalmancheysandor.applications.dominions.apis.general.exceptions.IllegalPointOfExecution;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;


public class HugoAiAdvancedEngine extends THugoAIEngine implements IHeuristicAiEngine {

    private IHeuristicEvaluator heuristicEvaluator;



    @Override
    public AiDecisionResult makeDecision(AiDecisionContext context) {
        // Initialisation
        BranchData branchData = chooseTheBestStep(context.getGameState(), context.getYourPlayerKey(), 1, 0);
        Integer chosenCellKey = branchData.advisedStepKey;

        if (chosenCellKey != null) {
            System.out.println("ChosenCellKey:" + chosenCellKey);
            return new AiDecisionResult(chosenCellKey, calculatePlayerMaxAttackPower(context.getGameState(), context.getYourPlayerKey()));
        }
        System.out.println("ChosenCellKey:Reserve");
        return new AiDecisionResult(0, 0);
    }

    private BranchData chooseTheBestStep(GameState gameState, int supportedPlayerKey, int maxDeepness, int parentLevelDeepness) {
        int currentLevel = parentLevelDeepness + 1;
        System.out.println("########## LEVEL " + currentLevel + " ####################");

        System.out.println("DefendedCellKeys:" + collectCellKeysOfAPlayer(gameState, supportedPlayerKey));
        System.out.println("AvailableSteps:" + collectCellKeysOfCurrentAttackZoneOfPlayer(gameState, supportedPlayerKey));
        // Step 1
        Map<Integer, List<Map<Integer, Integer>>> combinationsGroups = generateCombinationGroups(gameState, supportedPlayerKey);

        // Step 2 - Adott lepesem mellet(kombinalva a tobbiekevel) mielyen allapot variaciok johetnek letre
        Map<Integer, List<GameState>> gameStateListGroups = phase2(gameState, combinationsGroups);

        // Step 3
        Integer stepKey;
        List<GameState> gameStateVariants;
        HugoHeuristicEvaluation theLessWorse = null;
        Integer chosenKey = null;
        for (Map.Entry<Integer, List<GameState>> entry : gameStateListGroups.entrySet()) {
            stepKey = entry.getKey();
            gameStateVariants = entry.getValue();


            // Loop - All game state variants inside of my step
            BranchData b;
            HugoHeuristicEvaluation currentHeuristicEvaluation;
            HugoHeuristicEvaluation worstHeuristicEvaluation = null;
            List<HugoHeuristicEvaluation> hugoHeuristicEvaluationList = new ArrayList<>();
            for (GameState gameStateVariant : gameStateVariants) {
                if (maxDeepness > currentLevel) {
                    b = chooseTheBestStep(gameStateVariant, supportedPlayerKey, maxDeepness, currentLevel);
                    currentHeuristicEvaluation = b.getHugoHeuristicEvaluation();
                } else if (maxDeepness == currentLevel) {
                    currentHeuristicEvaluation = generateHeuristicEvaluationObject(gameStateVariant, supportedPlayerKey);
                    //currentHeuristicEvaluation = heuristicEvaluator.evaluate(gameStateVariant, supportedPlayerKey);
                } else {
                    throw new IllegalPointOfExecution("Unexpected case is found!");
                }


                //
                if (worstHeuristicEvaluation == null) {
                    worstHeuristicEvaluation = currentHeuristicEvaluation;
                } else if (currentHeuristicEvaluation.compareTo(worstHeuristicEvaluation) < 0) { // worstHeuristic is better than heuristicItem, so heuristicItem is worse than worstHeuristic
                    worstHeuristicEvaluation = currentHeuristicEvaluation;
                }


            }

            // Azt tekintjuk a legjobb operator alklamazasnak a tamogatott jatekos szamara, ami a a leheto legjobb erteket adja.(vagyis a legkevesbe rosszat akarjuk valasztani)
            if (theLessWorse == null) {
                theLessWorse = worstHeuristicEvaluation;
                chosenKey = stepKey;
            } else if (theLessWorse.compareTo(worstHeuristicEvaluation) < 0) {// Amikor a theLessWorse rosszabb mint a worst, akkor a worst a theLessWorse roszabb. akkor egy kevesbe roszabra kell attalitani
                theLessWorse = worstHeuristicEvaluation;
                chosenKey = stepKey;
            }

            System.out.println(">".repeat(currentLevel) + " ObservedCellKey:" + stepKey + " H:" + worstHeuristicEvaluation.getHeuristicValue() + " .....chosen:" + chosenKey);
        }

        return new BranchData(chosenKey, theLessWorse);
    }

    private HugoHeuristicEvaluation findWorstHeuristicEvaluationInAGroup(List<HugoHeuristicEvaluation> hugoHeuristicEvaluationList) {
        HugoHeuristicEvaluation worstHeuristic = null;

        for (HugoHeuristicEvaluation heuristicItem : hugoHeuristicEvaluationList) {
            if (worstHeuristic == null) {
                worstHeuristic = heuristicItem;
            } else if (heuristicItem.compareTo(worstHeuristic) < 0) { // worstHeuristic is better than heuristicItem, so heuristicItem is worse than worstHeuristic
                worstHeuristic = heuristicItem;
            }
        }
        return worstHeuristic;
    }


    private HugoHeuristicEvaluation generateHeuristicEvaluationObject(GameState gameState, int myPlayerKey) {
        IHeuristicEvaluation evaluation = heuristicEvaluator.evaluate(gameState, myPlayerKey);
        if (evaluation instanceof HugoHeuristicEvaluation) {
            return (HugoHeuristicEvaluation) evaluation;
        }
        throw new RuntimeException("Wrong class cast type");
    }


//    private HugoHeuristicEvaluation generateHeuristicEvaluationObject(GameState gameState, int myPlayerKey) {
//        int allCellCount = gameState.getCellCount();
//        int emptyCellCount = gameState.getEmptyCellCount();
//        double occupiedCellCount = allCellCount - emptyCellCount;
//        int playerCount = gameState.getPlayerCount();
//        double myOccupiedCellCount = gameState.getOccupiedCellsCountOfPlayer(myPlayerKey);
//        //System.out.println("myOccupiedCellCount[" + myOccupiedCellCount + "] / occupiedCellCount[" + occupiedCellCount + "]");
//
//        double occupancyShare = myOccupiedCellCount / occupiedCellCount;
//        return new HugoHeuristicEvaluation(occupancyShare);
//    }


    @Override
    public void registerHeuristicEvaluator(IHeuristicEvaluator evaluator) {
//        if (!(evaluator instanceof HugoFirstHeuristicEvaluator)) {
//            throw new RuntimeException("Wrong heuristic evaluator!");
//        }
//
//        this.heuristicEvaluator = (HugoFirstHeuristicEvaluator) evaluator;
        this.heuristicEvaluator = evaluator;
    }


    /// ////////////////////////////////////////////////////////////////////////////////////////
    /// // Inner Classes /////////////////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////////
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class BranchData {
        Integer advisedStepKey;
        HugoHeuristicEvaluation hugoHeuristicEvaluation;
    }

}

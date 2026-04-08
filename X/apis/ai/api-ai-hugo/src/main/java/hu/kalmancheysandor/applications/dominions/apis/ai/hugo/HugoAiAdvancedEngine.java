package hu.kalmancheysandor.applications.dominions.apis.ai.hugo;

import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionContext;
import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionResult;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;
import hu.kalmancheysandor.applications.dominions.apis.general.exceptions.IllegalPointOfExecution;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;


public class HugoAiAdvancedEngine extends THugoAIEngine {


    @Override
    public AiDecisionResult makeDecision(AiDecisionContext context) {
        // Initialisation
        BranchData branchData = chooseTheBestStep(context.getGameState(), context.getYourPlayerKey(), 1, 0);
        Integer chosenCellKey = branchData.advisedStepKey;



        if (chosenCellKey != null) {
            System.out.println("ChosenCellKey:"+chosenCellKey);
            return new AiDecisionResult(chosenCellKey, calculatePlayerMaxAttackPower(context.getGameState(), context.getYourPlayerKey()));
        }
        System.out.println("ChosenCellKey:Reserve");
        return new AiDecisionResult(0, 0);
    }

    private BranchData chooseTheBestStep(GameState gameState, int supportedPlayerKey, int maxDeepness, int parentLevelDeepness) {
        int currentLevel = parentLevelDeepness + 1;
        System.out.println("########## LEVEL "+currentLevel+" ####################");

        System.out.println("DefendedCellKeys:"+collectCellKeysOfAPlayer(gameState,supportedPlayerKey));
        System.out.println("AvailableSteps:"+collectCellKeysOfCurrentAttackZoneOfPlayer(gameState, supportedPlayerKey));
        // Step 1
        Map<Integer, List<Map<Integer, Integer>>> combinationsGroups = generateCombinationGroups(gameState, supportedPlayerKey);

        // Step 2 - Adott lepesem mellet(kombinalva a tobbiekevel) mielyen allapot variaciok johetnek letre
        Map<Integer, List<GameState>> gameStateListGroups = phase2(gameState, combinationsGroups);

        // Step 3
        Integer stepKey;
        List<GameState> gameStateVariants;
        HeuristicData theLessWorse = null;
        Integer chosenKey = null;
        for (Map.Entry<Integer, List<GameState>> entry : gameStateListGroups.entrySet()) {
            stepKey = entry.getKey();
            gameStateVariants = entry.getValue();


            // Loop - All game state variants inside of my step
            HeuristicData h;
            List<HeuristicData> heuristicDataList = new ArrayList<>();
            for (GameState gameStataVariant : gameStateVariants) {
                if (maxDeepness > currentLevel) {
                    BranchData b = chooseTheBestStep(gameStataVariant, supportedPlayerKey, maxDeepness, currentLevel);
                    h = b.getHeuristicValue();
                    heuristicDataList.add(h);
                } else if (maxDeepness == currentLevel) {
                    h = generateHeuristicData(gameStataVariant, supportedPlayerKey);
                    heuristicDataList.add(h);
                } else {
                    throw new IllegalPointOfExecution("Unexpected case is found!");
                }
            }
            // TODO vegallapot eseten nem lehet ujabb satet, de eleve nem is lesz attack zone
            // Az adott kezdo allapotra alkalmazott sajat operator alkalmazas heurisztikaja, az ellenfelek osszes leheteseges lepes variacioja altal eloallitott legrosszabat kell tekinteni.
            HeuristicData worst = findWorstHeuristic(heuristicDataList); // worst for me, but better for opponents


            // Azt tekintjuk a legjobb operator alklamazasnak a tamogatott jatekos szamara, ami a a leheto legjobb erteket adja.
            if (theLessWorse == null) {
                theLessWorse = worst;
                chosenKey = stepKey;
            } else if (isHeuristicBetterThan(worst, theLessWorse)||isHeuristicEqualWith(worst, theLessWorse)) {
                theLessWorse = worst;
                chosenKey = stepKey;
            }
//            else if (isHeuristicEqualWith(worst, theLessWorse)) {   // Hogy elkeruljuk a reserve bent ragadasat, mert ha mindneki egyenlo akkor az elso maradna, ami a reserve.
//                theLessWorse = worst;
//                chosenKey = stepKey;
//            }

            System.out.println(">".repeat(currentLevel)+" ObservedCellKey:"+stepKey+" H:"+worst.getOccupancyShare()+" .....chosen:"+chosenKey);
        }

        return new BranchData(chosenKey, theLessWorse);
    }


    private boolean isHeuristicBetterThan(HeuristicData a, HeuristicData b) {
        return a.getOccupancyShare() > b.getOccupancyShare();
    }

    private boolean isHeuristicEqualWith(HeuristicData a, HeuristicData b) {
        return a.getOccupancyShare() == b.getOccupancyShare();
    }

    private boolean isHeuristicWorseThan(HeuristicData a, HeuristicData b) {
        return a.getOccupancyShare() < b.getOccupancyShare();
    }

    private HeuristicData findBestHeuristic(List<HeuristicData> heuristicDataList) {
        HeuristicData bestHeuristic = null;
        for (HeuristicData heuristicItem : heuristicDataList) {
            if (bestHeuristic == null) {
                bestHeuristic = heuristicItem;
            } else if (isHeuristicBetterThan(heuristicItem, bestHeuristic)) {
                bestHeuristic = heuristicItem;
            }
        }
        return bestHeuristic;
    }

    private HeuristicData findWorstHeuristic(List<HeuristicData> heuristicDataList) {
        HeuristicData worstHeuristic = null;
        for (HeuristicData heuristicItem : heuristicDataList) {
            if (worstHeuristic == null) {
                worstHeuristic = heuristicItem;
            } else if (isHeuristicWorseThan(heuristicItem, worstHeuristic)) {
                worstHeuristic = heuristicItem;
            }
        }
        return worstHeuristic;
    }


    private HeuristicData generateHeuristicData(GameState gameState, int myPlayerKey) {
        int allCellCount = gameState.getCellCount();
        int emptyCellCount = gameState.getEmptyCellCount();
        double occupiedCellCount = allCellCount - emptyCellCount;
        int playerCount = gameState.getPlayerCount();
        double myOccupiedCellCount = gameState.getOccupiedCellsCountOfPlayer(myPlayerKey);
        //System.out.println("myOccupiedCellCount[" + myOccupiedCellCount + "] / occupiedCellCount[" + occupiedCellCount + "]");

        double occupancyShare = myOccupiedCellCount / occupiedCellCount;
        return new HeuristicData(occupancyShare);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    ///// [Inner Classes] /////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class BranchData {
        Integer advisedStepKey;
        HeuristicData heuristicValue;
    }

}

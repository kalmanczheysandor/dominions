package hu.kalmancheysandor.applications.dominions.apis.ai.hugo;

import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionContext;
import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionResult;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;

import java.util.*;

public class HugoAiBasicEngine extends THugoAIEngine {



    @Override
    public AiDecisionResult makeDecision(AiDecisionContext context) {
        // Initialisation
        Integer chosenCellKey = chooseTheBestStep(context.getGameState(),context.getYourPlayerKey());
        if (chosenCellKey != null) {
            return new AiDecisionResult(chosenCellKey, calculatePlayerMaxAttackPower(context.getGameState(),context.getYourPlayerKey()));
        }

        return new AiDecisionResult(0, 0);
    }

    private Integer chooseTheBestStep(GameState gameState,int playerKey) {
        System.out.println("---------[chooseTheBestStep]---------------------");
        Map<Integer, HeuristicData> prognosisList = calculateAverageHeuristicValuesOfEachSteps(gameState,playerKey);


        System.out.println("------");

        double best = Double.MIN_VALUE;
        Integer chosenCellKey = null;
        for (Map.Entry<Integer, HeuristicData> prognosisEntry : prognosisList.entrySet()) {
            Integer targetCellKey = prognosisEntry.getKey();
            HeuristicData prognosis = prognosisEntry.getValue();
            System.out.println("Prognosis[" + targetCellKey + "]:" + prognosis);

            if (prognosis.getOccupancyShare() > best) {
                chosenCellKey = targetCellKey;
                best = prognosis.getOccupancyShare();
            }
        }

        System.out.println("> Decision:" + chosenCellKey);
        return chosenCellKey;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    ///// [Combination methods] ///////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////












    ///////////////////////////////////////////////////////////////////////////////////////////
    ///// [Heuristic methods] /////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////


    private Map<Integer, HeuristicData> phase3(Map<Integer, List<GameState>> gameStateListGroups, int myPlayerKey) {
        Integer gameStateGroupKey;
        List<GameState> gameStateListInAGroup;
        List<HeuristicData> heuristicList;
        HeuristicData averageHeuristic;
        HeuristicData h;
        Map<Integer, HeuristicData> averageHeuristicValueGroups = new HashMap<>();
        for (Map.Entry<Integer, List<GameState>> gameStateGroupEntry : gameStateListGroups.entrySet()) {
            // Initialise
            gameStateGroupKey = gameStateGroupEntry.getKey();
            gameStateListInAGroup = gameStateGroupEntry.getValue();


            System.out.println("+++++++++++++");
            System.out.println("StepGroupKey:" + gameStateGroupKey);

            //
            heuristicList = new ArrayList<>();
            for (GameState gameStateItem : gameStateListInAGroup) {
                h = generateHeuristicData(gameStateItem, myPlayerKey);
                System.out.println("Heur:" + h);
                heuristicList.add(h);
            }
            averageHeuristic = calculateAverageHeuristicValue(heuristicList);
            averageHeuristicValueGroups.put(gameStateGroupKey, averageHeuristic);
        }
        return averageHeuristicValueGroups;
    }

    private Map<Integer, HeuristicData> calculateAverageHeuristicValuesOfEachSteps(GameState gameState,int playerKey) {
        // Step 1
        Map<Integer, List<Map<Integer, Integer>>> combinationsGroups = generateCombinationGroups(gameState,playerKey);

        // Step 2
        Map<Integer, List<GameState>> gameStateListGroups = phase2(gameState,combinationsGroups);

        // Step 3 - calculate averages heuristic values of each group
        Map<Integer, HeuristicData> averageHeuristicValueGroups = phase3(gameStateListGroups,playerKey);

        return averageHeuristicValueGroups;
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

    private HeuristicData calculateAverageHeuristicValue(List<HeuristicData> heuristicList) {
        int count = 1;
        HeuristicData average = new HeuristicData();
        for (HeuristicData data : heuristicList) {
            average.setOccupancyShare(average.getOccupancyShare() + data.getOccupancyShare());
            count++;
        }
        average.setOccupancyShare(average.getOccupancyShare() / count);
        return average;
    }

}

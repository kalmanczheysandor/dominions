package hu.kalmancheysandor.applications.dominions.apis.ai.engine.liz;

import hu.kalmancheysandor.applications.dominions.apis.ai.algorithm.riskmin.TRiskMinAlgorithm;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.liz.evalutor.LizEvaluationInput;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.liz.interfaces.*;
import hu.kalmancheysandor.applications.dominions.apis.ai.general.common.AiDecisionContext;
import hu.kalmancheysandor.applications.dominions.apis.ai.general.common.AiDecisionResult;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.network.INeuralNetwork;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;

import java.util.*;

import static hu.kalmancheysandor.applications.dominions.apis.util.validate.asserted.VariableAssert.assertDoubleIsInInterval;


public class LizEngine extends TRiskMinAlgorithm implements ILizEngine {
    private ILizOperatorScoreEvaluator operatorScoreEvaluator;
    private Map<String, INeuralNetwork> enemyNetworks;

    public LizEngine() {
        enemyNetworks = new LinkedHashMap<>();
    }

    @Override
    public AiDecisionResult makeDecision(AiDecisionContext context) {
        return super.makeDecision(context);
    }

    @Override
    public void registerEnemyNetwork(String userUuid, INeuralNetwork neuralNetwork) {
        enemyNetworks.put(userUuid, neuralNetwork);
    }

    @Override
    public void registerOperatorScoreEvaluator(ILizOperatorScoreEvaluator evaluator) {
        this.operatorScoreEvaluator = evaluator;
    }


    @Override
    protected Map<Double, Set<Integer>> buildOperatorRiskGroups(GameState gameState, int supportedPlayerKey, Map<Integer, AiDecisionContext.Player> enemyPlayers) {
        //
        Set<Integer> availableOperators = collectAvailableOperators(gameState, supportedPlayerKey);
        Map<Integer, ILizEnemyDataItem> enemyDataItemList = buildEnemyDataItemList(enemyPlayers);

        //
        Map<Double, Set<Integer>> riskGroups = new TreeMap<>();
        for (Integer observedOperatorIndex : availableOperators) {

            // Use the evaluator object to determine the operator value
            ILizOperatorScoreEvaluation evaluation = operatorScoreEvaluator.evaluate(LizEvaluationInput.builder()
                    .gameState(gameState)
                    .observedPlayerKey(supportedPlayerKey)
                    .operatorKey(observedOperatorIndex)
                    .enemyDataList(enemyDataItemList)
                    .build()
            );
            Double operatorScore = evaluation.getOperatorScore();
            assertDoubleIsInInterval(operatorScore, "operatorScore", 0.0, 1.0);

            // Gather cell keys into evaluationScore-value groups
            if (riskGroups.containsKey(operatorScore)) { // When the group is already defined
                Set<Integer> keyList = riskGroups.get(operatorScore);
                keyList.add(observedOperatorIndex);
                //riskGroups.put(operatorScore, keyList);
            } else { // When the group is not defined yet
                Set<Integer> keyList = new HashSet<>();
                keyList.add(observedOperatorIndex);
                riskGroups.put(operatorScore, keyList);
            }

        }

        return riskGroups;
    }


    private Set<Integer> collectAvailableOperators(GameState gameState, int supportedPlayerKey) {

        //
        Set<Integer> attackOperators = collectCellKeysOfCurrentAttackZoneOfPlayer(gameState, supportedPlayerKey);

        //
        Set<Integer> operators = new HashSet<>(attackOperators);
        operators.add(0); // the skip operator
        return operators;
    }


    private Map<Integer, ILizEnemyDataItem> buildEnemyDataItemList(Map<Integer, AiDecisionContext.Player> enemyPlayers) {

        Map<Integer, ILizEnemyDataItem> enemyDataList = new HashMap<>();

        for (Map.Entry<Integer, AiDecisionContext.Player> enemyPlayerEntry : enemyPlayers.entrySet()) {
            String enemyPlayerUuid = enemyPlayerEntry.getValue().getUserUuid();
            int enemyPlayerKey = enemyPlayerEntry.getValue().getPlayerKey();

            // Checking whether the enemy has an added neural network
            if (this.enemyNetworks == null || !this.enemyNetworks.containsKey(enemyPlayerUuid)) {
                throw new RuntimeException("No network object is registered at key:" + enemyPlayerUuid);
            }

            // Putting together
            enemyDataList.put(enemyPlayerKey, LizEvaluationInput.EnemyDataItem.builder()
                    .playerKey(enemyPlayerKey)
                    .playerType(enemyPlayerEntry.getValue().getPlayerType())
                    .name(enemyPlayerEntry.getValue().getName())
                    .userUuid(enemyPlayerUuid)
                    .neuralNetwork(this.enemyNetworks.get(enemyPlayerUuid))
                    .build()
            );
        }
        return enemyDataList;
    }

}

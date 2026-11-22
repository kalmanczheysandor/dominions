package hu.kalmancheysandor.applications.dominions.apis.ai.engine.otto;



import hu.kalmancheysandor.applications.dominions.apis.ai.algorithm.riskmin.TRiskMinAlgorithm;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.otto.interfaces.IOttoEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.otto.interfaces.IOttoOperatorScoreEvaluation;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.otto.interfaces.IOttoOperatorScoreEvaluator;
import hu.kalmancheysandor.applications.dominions.apis.ai.general.common.AiDecisionContext;
import hu.kalmancheysandor.applications.dominions.apis.ai.general.common.AiDecisionResult;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class OttoEngine extends TRiskMinAlgorithm implements IOttoEngine {

    private IOttoOperatorScoreEvaluator operatorScoreEvaluator;

    @Override
    public AiDecisionResult makeDecision(AiDecisionContext context) {
        return super.makeDecision(context);
    }

    @Override
    public void registerOperatorScoreEvaluator(IOttoOperatorScoreEvaluator evaluator) {
        this.operatorScoreEvaluator = evaluator;
    }


    @Override
    protected Map<Double, Set<Integer>> buildOperatorRiskGroups(GameState gameState, int supportedPlayerKey, Map<Integer, AiDecisionContext.Player> enemyPlayers) {
        //
        Set<Integer> availableOperators = collectAvailableOperators(gameState, supportedPlayerKey);

        //
        Map<Double, Set<Integer>> riskGroups = new TreeMap<>();
        for(Integer observedOperatorIndex : availableOperators){

            // Use the evaluator object to determine the operator value
            IOttoOperatorScoreEvaluation evaluation = operatorScoreEvaluator.evaluate(
                    gameState,
                    observedOperatorIndex,
                    supportedPlayerKey
            );
            Double operatorScore = evaluation.getOperatorScore();

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



}

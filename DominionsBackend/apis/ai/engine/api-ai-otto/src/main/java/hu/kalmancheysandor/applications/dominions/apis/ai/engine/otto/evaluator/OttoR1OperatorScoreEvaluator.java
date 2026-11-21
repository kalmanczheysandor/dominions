package hu.kalmancheysandor.applications.dominions.apis.ai.engine.otto.evaluator;


import hu.kalmancheysandor.applications.dominions.apis.ai.engine.otto.interfaces.IOttoOperatorScoreEvaluation;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.otto.interfaces.IOttoOperatorScoreEvaluator;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.otto.evaluation.OttoOperatorScoreEvaluation;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStatePlayer;
import hu.kalmancheysandor.applications.dominions.apis.general.exceptions.IllegalPointOfExecution;

import static hu.kalmancheysandor.applications.dominions.apis.util.validate.asserted.VariableAssert.*;

public class OttoR1OperatorScoreEvaluator extends TOttoOperatorScoreEvaluator implements IOttoOperatorScoreEvaluator {

    @Override
    public IOttoOperatorScoreEvaluation evaluate(GameState gameState, int operatorKey, int observedPlayerKey) {
        // Parameter checking
        assertObjectIsNotNull(gameState, "GameState");
        assertIntNotNegative(operatorKey, "OperatorKey");
        assertIntIsInInterval(operatorKey, "operatorKey", 0, gameState.getCells().length);// Here is an extra operator (skip), so "length-1" does not needs
        assertIntNotNegative(observedPlayerKey, "ObservedPlayerKey");
        assertIntIsInInterval(observedPlayerKey, "observedPlayerKey", 0, gameState.getOpponents().length - 1);

        //
        GameStatePlayer playerObj = gameState.getOpponent(observedPlayerKey);
        assertObjectIsNotNull(playerObj, "PlayerObj");


        if(!isPlayerAlive(gameState, observedPlayerKey)) {
            return new OttoOperatorScoreEvaluation(0.0);
        }
        else if(countActiveEnemies(gameState, observedPlayerKey) == 0) {
            return new OttoOperatorScoreEvaluation(0.0);
        }
        else {

            if(operatorKey==0) {// 0= skip action
                return new OttoOperatorScoreEvaluation(1-reinforcementIncreaseNeed(gameState, observedPlayerKey));
            }
            else if(operatorKey>0) {// 0= skip action; 1= cell0; 2= cell1; 3= cell2; ...
                int cellKey = operatorKey-1;

                int potentialAttackersCount = countPotentialAttackersAgainstACell(gameState, cellKey, observedPlayerKey);
                int ActiveEnemiesCount = countActiveEnemies(gameState, observedPlayerKey);

                //
                return new OttoOperatorScoreEvaluation(
                        (double)potentialAttackersCount / (double)ActiveEnemiesCount
                );

            }

        }

        throw new IllegalPointOfExecution("Unexpected point of execution reached in TOttoR1OperatorScoreEvaluator.evaluate() method!");
    }





}

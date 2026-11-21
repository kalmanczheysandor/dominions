package hu.kalmancheysandor.applications.dominions.apis.ai.engine.liz.interfaces;

import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;

import java.util.Map;

public interface ILizEvaluationInput {
    public GameState getGameState();
    public int getOperatorKey();
    public int getObservedPlayerKey();
    public Map<Integer, ILizEnemyDataItem> getEnemyDataList();
}

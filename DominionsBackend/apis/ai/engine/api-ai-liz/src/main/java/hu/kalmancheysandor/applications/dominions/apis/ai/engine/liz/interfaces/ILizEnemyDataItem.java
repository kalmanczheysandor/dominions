package hu.kalmancheysandor.applications.dominions.apis.ai.engine.liz.interfaces;

import hu.kalmancheysandor.applications.dominions.apis.ai.neural.network.INeuralNetwork;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.GameMapPlayerType;

import java.util.Map;

public interface ILizEnemyDataItem {
    public int getPlayerKey();
    public GameMapPlayerType getPlayerType();
    public String getName();
    public String getUserUuid();
    public  INeuralNetwork getNeuralNetwork();
}

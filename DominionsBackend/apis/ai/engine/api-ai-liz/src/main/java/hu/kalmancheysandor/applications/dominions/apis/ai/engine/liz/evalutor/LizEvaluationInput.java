package hu.kalmancheysandor.applications.dominions.apis.ai.engine.liz.evalutor;

import hu.kalmancheysandor.applications.dominions.apis.ai.engine.liz.interfaces.ILizEnemyDataItem;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.liz.interfaces.ILizEvaluationInput;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.network.INeuralNetwork;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.GameMapPlayerType;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
public class LizEvaluationInput implements ILizEvaluationInput {
    private GameState gameState;
    private int operatorKey;
    private int observedPlayerKey;
    private Map<Integer, ILizEnemyDataItem> enemyDataList;

    public LizEvaluationInput(GameState gameState, int operatorKey, int observedPlayerKey, Map<Integer, ILizEnemyDataItem> enemyDataList) {
        this.gameState = gameState;
        this.operatorKey = operatorKey;
        this.observedPlayerKey = observedPlayerKey;
        this.enemyDataList = enemyDataList;
    }

    public GameState getGameState() {
        return gameState;
    }

    public int getOperatorKey() {
        return operatorKey;
    }

    public int getObservedPlayerKey() {
        return observedPlayerKey;
    }

    public Map<Integer, ILizEnemyDataItem> getEnemyDataList() {
        return enemyDataList;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class EnemyDataItem implements ILizEnemyDataItem {
        private int playerKey;
        private GameMapPlayerType playerType;
        private String name;
        private String userUuid;
        private INeuralNetwork neuralNetwork;


        public int getPlayerKey() {
            return playerKey;
        }

        public GameMapPlayerType getPlayerType() {
            return playerType;
        }

        public String getName() {
            return name;
        }

        public String getUserUuid() {
            return userUuid;
        }

        public INeuralNetwork getNeuralNetwork() {
            return neuralNetwork;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            EnemyDataItem that = (EnemyDataItem) o;
            return playerKey == that.playerKey;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(playerKey);
        }
    }
}

package hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto;

import hu.kalmancheysandor.applications.dominions.apis.game.common.map.GameMapPlayerType;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AiDecisionRequest {
    private GameState gameState;
    private Integer yourPlayerKey;
    private String yourUserUuid;
    private Integer yourReserveSize;
    private Map<Integer, Integer> reserves = new HashMap<>();
    private Map<Integer,Player> enemyPlayers;
    //private Integer scenarioId;
    private String scenarioUuid;
    private String playerCharacterCode;



    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Player {
        private int playerKey;
        private GameMapPlayerType playerType;

        private String name;
        private String userUuid;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Player that)) return false;
            return playerKey == that.playerKey;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(playerKey);
        }
    }
}

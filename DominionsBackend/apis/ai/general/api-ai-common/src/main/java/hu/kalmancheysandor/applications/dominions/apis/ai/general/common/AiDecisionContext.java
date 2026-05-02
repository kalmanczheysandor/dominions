package hu.kalmancheysandor.applications.dominions.apis.ai.general.common;

import hu.kalmancheysandor.applications.dominions.apis.game.common.map.GameMapPlayerType;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

import java.util.Map;
import java.util.Objects;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiDecisionContext {
    private GameState gameState;
    private Integer yourPlayerKey;
    private String yourUserUuid;
    private Integer yourReserveSize;
    private Map<Integer, Integer> reserves = new HashMap<>();
    private Map<Integer,Player> players;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
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

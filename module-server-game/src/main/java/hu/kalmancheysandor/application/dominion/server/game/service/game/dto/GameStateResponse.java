package hu.kalmancheysandor.application.dominion.server.game.service.game.dto;

import hu.kalmancheysandor.application.dominion.api.game.common.engine.GameMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameStateResponse {

    private int yourKey;
    private int currentTurn;
    private int pendingCount;
    private int playerCount;
    private StatusCode statusCode;

    private Map<Integer, GameMap.MapCell> cells;
    private Map<Integer,GameMap.Opponent> players;

    public enum StatusCode {
        RECRUITING,
        PLAYING,
        ENDED
    }

}

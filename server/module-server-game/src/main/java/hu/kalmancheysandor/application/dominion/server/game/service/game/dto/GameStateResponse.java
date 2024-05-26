package hu.kalmancheysandor.application.dominion.server.game.service.game.dto;

import hu.kalmancheysandor.application.dominion.api.game.common.engine.GameMap;
import hu.kalmancheysandor.application.dominion.api.game.common.session.SessionStatusCode;
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
    private SessionStatusCode statusCode;

    private Map<Integer, GameMap.MapCell> cells;
    private Map<Integer,GameMap.Opponent> players;



}

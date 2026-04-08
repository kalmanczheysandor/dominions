package hu.kalmancheysandor.applications.dominions.servers.game.service.game;

import hu.kalmancheysandor.applications.dominions.apis.game.common.map.GameMapCell;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.GameMapOpponent;
import hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.PlayStateStatusCode;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStateStatusCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameStateResponse {
    private int currentTurn;
    private int pendingCount;
    private int playerCount;
    private PlayStateStatusCode sessionStatus;
    private GameStateStatusCode gameStatus;

    private Integer winnerKey;
    private int[] alivePlayers;

    private Map<Integer, GameMapCell> cells;
    private Map<Integer, GameMapOpponent> players;

}
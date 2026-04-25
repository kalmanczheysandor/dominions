package hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game;

import hu.kalmancheysandor.applications.dominions.apis.game.common.map.configuration.GameMapConfiguration;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.state.GameMapState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.PlayStateStatusCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GamePlayStateResponse {
    private GameMapState gameMapState;
    private GameMapConfiguration gameMapConfiguration;
    private int maxHumanPlayerCount;
    private int currentHumanPlayerCount;
    private PlayStateStatusCode statusCode;
    private int[] winnerKeys;
    private List<String> participants;
    private int turn;
    private int missingPlayerRespondCount = 0;
}

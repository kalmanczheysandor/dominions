package hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game;

import hu.kalmancheysandor.applications.dominions.apis.game.common.map.configuration.GameMapConfiguration;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.state.GameMapState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.PlayStateStatusCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GamePlayCreateResponse {
    private String gameSessionUuid;
    private Integer playerIndex;
    private String secretKey;

    private GameMapState gameMapState;
    private GameMapConfiguration gameMapConfiguration;
    private int maxHumanPlayerCount;
    private int currentHumanPlayerCount;
    private PlayStateStatusCode statusCode;
}

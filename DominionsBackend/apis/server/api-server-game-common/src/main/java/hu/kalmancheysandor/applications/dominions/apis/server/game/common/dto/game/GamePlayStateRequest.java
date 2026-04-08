package hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GamePlayStateRequest {
    private String gameSessionUuid;
    private String endpointKey;
    private String userUuid;
}

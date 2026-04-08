package hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.action;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GamePlayReserveActionRequest {
    private String gameSessionUuid;
    private String endpointKey;
    private String userUuid;

    private int playerIndex;
}

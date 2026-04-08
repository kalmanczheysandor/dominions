package hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.action;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GamePlayAttackActionRequest {
    private String gameSessionUuid;
    private String endpointKey;
    private String userUuid;

    private int playerIndex;
    private int targetCellKey;
    private int attackingTroopSize;

}

package hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GamePlayItemResponse {
    private String uuid;
    private String scenarioUuid;
    private String title;
    private String difficulty;
    private int freeSlotCount;
    private int playerAiCount;
    private int playerHumanCount;
}

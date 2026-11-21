package hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.scenario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameScenarioItemResponse {
    private String uuid;
    private String title;
    private String difficulty;
    private String description;
    private int playerHumanCount;
    private int playerAiCount;
}

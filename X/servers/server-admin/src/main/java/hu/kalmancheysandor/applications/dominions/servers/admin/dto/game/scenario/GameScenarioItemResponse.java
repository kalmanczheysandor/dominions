package hu.kalmancheysandor.applications.dominions.servers.admin.dto.game.scenario;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameScenarioItemResponse {
    private String uuid;
    private String title;
    private String difficulty;
    private int playerHumanCount;
    private int playerAiCount;
    private String gameMap;
    private boolean published;
    private boolean enabled;
}

package hu.kalmancheysandor.applications.dominions.servers.admin.dto.game.scenario;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@Validated
public class GameScenarioCreateRequest {
    private String title;
    private String difficulty;
    private String description;
    private String gameMap;
    private boolean published;
    private boolean enabled;

    private String imageBase64;
}

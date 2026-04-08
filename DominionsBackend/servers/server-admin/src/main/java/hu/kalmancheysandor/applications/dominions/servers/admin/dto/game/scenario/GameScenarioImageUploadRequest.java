package hu.kalmancheysandor.applications.dominions.servers.admin.dto.game.scenario;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameScenarioImageUploadRequest {
    @NotBlank
    private String imageBase64;

    @NotBlank
    private String filename;
}

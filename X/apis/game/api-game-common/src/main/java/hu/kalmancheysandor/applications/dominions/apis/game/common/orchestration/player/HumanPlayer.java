package hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.player;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.GameMapPlayerType;

@Data
@NoArgsConstructor
@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE
)
public class HumanPlayer extends PlayerData {

    public HumanPlayer(int id, @NotBlank String userUuid, @NotBlank String name, @NotBlank String secretKey) {
        super(id,userUuid, name, GameMapPlayerType.HUMAN, secretKey);
    }
}

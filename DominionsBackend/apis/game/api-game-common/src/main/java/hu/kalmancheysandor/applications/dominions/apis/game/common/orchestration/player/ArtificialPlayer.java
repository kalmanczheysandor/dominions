package hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.player;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.GameMapPlayerType;
import hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.player.exception.IncorrectPlayerConfigurationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.GameMapEngineType;

@Data
@NoArgsConstructor
@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE
)
public class ArtificialPlayer extends PlayerData {
    private GameMapEngineType engineType;
    private String characterCode;

    public ArtificialPlayer(int id, String userUuid, String name,  GameMapEngineType gameMapEngineType,String characterCode, String secretKey) {
        super(id, userUuid, name, GameMapPlayerType.ARTIFICIAL, secretKey);
        if (GameMapEngineType.HUMAN == gameMapEngineType) {
            throw new IncorrectPlayerConfigurationException();
        }
        this.engineType = gameMapEngineType;
        this.characterCode = characterCode;
    }

    public GameMapEngineType getEngineType() {
        return engineType;
    }

    public String getCharacterCode() {
        return characterCode;
    }
}

package hu.kalmancheysandor.applications.dominions.apis.game.common.map.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.GameMapCharacterCode;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.GameMapEngineType;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.GameMapPlayerType;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.exception.IncompatibleCharacterCodeGameMapException;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.exception.IncompatibleEngineTypeGameMapException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE
)
public class GameMapConfigurationPlayer implements Serializable {
    @JsonProperty("type")
    private GameMapPlayerType type;
    @JsonProperty("engine")
    private GameMapEngineType engine;
    @JsonProperty("character")
    private GameMapCharacterCode character;


    public GameMapConfigurationPlayer(GameMapPlayerType type, GameMapEngineType engine, GameMapCharacterCode character) {
        this.type = type;
        this.engine = engine;
        this.character = character;

        if (GameMapPlayerType.HUMAN == type) {
            if (GameMapEngineType.HUMAN != engine) {
                throw new IncompatibleEngineTypeGameMapException();
            }
            if (GameMapCharacterCode.HUMAN != character) {
                throw new IncompatibleCharacterCodeGameMapException();
            }
        } else if (GameMapPlayerType.ARTIFICIAL == type) {
            if (GameMapEngineType.HUMAN == engine) {
                throw new IncompatibleEngineTypeGameMapException();
            }
            if (GameMapCharacterCode.HUMAN == character) {
                throw new IncompatibleCharacterCodeGameMapException();
            }
        }
    }
}
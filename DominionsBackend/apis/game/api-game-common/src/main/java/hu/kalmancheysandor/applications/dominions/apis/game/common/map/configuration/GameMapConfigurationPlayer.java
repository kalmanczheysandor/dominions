package hu.kalmancheysandor.applications.dominions.apis.game.common.map.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.GameMapEngineType;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.GameMapPlayerType;
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


    public GameMapConfigurationPlayer(GameMapPlayerType type, GameMapEngineType engine) {
        this.type = type;
        this.engine = engine;

        if ((GameMapPlayerType.ARTIFICIAL == type && GameMapEngineType.HUMAN == engine) || (GameMapPlayerType.HUMAN == type && GameMapEngineType.HUMAN != engine)) {
            throw new IncompatibleEngineTypeGameMapException();
        }
    }
}
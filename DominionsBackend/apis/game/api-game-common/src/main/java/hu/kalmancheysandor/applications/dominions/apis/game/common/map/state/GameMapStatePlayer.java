package hu.kalmancheysandor.applications.dominions.apis.game.common.map.state;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.GameMapEngineType;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.GameMapPlayerType;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.exception.IncompatibleEngineTypeGameMapException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;


@Data
@NoArgsConstructor
public class GameMapStatePlayer implements Serializable {
    @JsonProperty("name")
    private String name;
    @JsonProperty("reserveSize")
    private Integer reserveSize;
    @JsonProperty("isAlive")
    private boolean alive = true;
    @JsonProperty("isIntentionGiven")
    private boolean intentionGiven = false;


    public GameMapStatePlayer(String name, Integer reserveSize, boolean alive) {
        this.name = name;
        this.reserveSize = reserveSize;
        this.alive = alive;
    }
}
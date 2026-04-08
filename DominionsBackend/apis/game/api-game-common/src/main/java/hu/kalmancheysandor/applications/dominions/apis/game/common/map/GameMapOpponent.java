package hu.kalmancheysandor.applications.dominions.apis.game.common.map;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.exception.IncompatibleEngineTypeGameMapException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
public class GameMapOpponent implements Serializable {
    private String name;
    private GameMapPlayerType type;
    private GameMapEngineType engine;
    private Integer reserveSize;
    @JsonProperty("isAlive")
    private boolean alive = true;

    public GameMapOpponent(String name, GameMapPlayerType type, GameMapEngineType engine, Integer reserveSize) {
        init(name, type, engine, reserveSize, true);
    }

    public GameMapOpponent(String name, GameMapPlayerType type, GameMapEngineType engine, Integer reserveSize, boolean alive) {
        init(name, type, engine, reserveSize, alive);
    }

    private void init(String name, GameMapPlayerType type, GameMapEngineType engine, Integer reserveSize, boolean isAlive) {
        this.name = name;
        this.type = type;
        this.engine = engine;
        this.reserveSize = reserveSize;
        this.alive = isAlive;

        if ((GameMapPlayerType.ARTIFICIAL == type && GameMapEngineType.HUMAN == engine) || (GameMapPlayerType.HUMAN == type && GameMapEngineType.HUMAN != engine)) {
            throw new IncompatibleEngineTypeGameMapException();
        }
    }

}
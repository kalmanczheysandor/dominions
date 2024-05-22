package hu.kalmancheysandor.application.dominion.api.game.common.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.kalmancheysandor.application.dominion.api.game.common.engine.exception.UnableToOpenMapFileGameException;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Data
public class GameMap {
    private Map<Integer,GameMap.MapCell> cells;

    @Data
    public static class MapCell {
        private int[] neighbours;
    }

    public static GameMap open(String path) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Path to your JSON file
            File jsonFile = new File(path);

            // Convert JSON file to Object
            GameMap map = objectMapper.readValue(jsonFile, GameMap.class);

            return map;
        } catch (IOException e) {
            throw new UnableToOpenMapFileGameException(path);
        }
    }

    public int cellCount() {
        return cells.size();
    }
}

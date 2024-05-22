package hu.kalmancheysandor.application.dominion.api.game.common.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.kalmancheysandor.application.dominion.api.game.common.engine.exception.UnableToOpenMapFileGameException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Data
@NoArgsConstructor
public class GameMap {
    private Map<Integer,GameMap.MapCell> cells;
    private Map<Integer,GameMap.Opponent> players;


    public static GameMap open(String path) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Path to your JSON file
            File jsonFile = new File(path);

            // Convert JSON file to Object
            GameMap map = objectMapper.readValue(jsonFile, GameMap.class);

            return map;
        } catch (IOException e) {
            e.printStackTrace();
            throw new UnableToOpenMapFileGameException(path);
        }
    }


    public static void validate(GameMap gameMap) {

    }






    public int cellCount() {
        return cells.size();
    }

    public int playerCount() {
        return players.size();
    }

    @Data
    public static class Opponent {
        private PlayerType type;
        private Integer reserveSize;
        public enum PlayerType {
            HUMAN,
            AI_BASIC,
            AI_NEURAL,
        }
    }

    @Data
    public static class MapCell {
        private Integer playerKey;
        private int armySize;
        private int[] neighbours;
    }
}

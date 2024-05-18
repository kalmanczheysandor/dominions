package hu.kalmanczheysandor.application.dominion.ai.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Data
@AllArgsConstructor
public class GameState {
    private int yourKey;
    private Map<Integer,BoardCell> cells = new HashMap<>();

    @Data
    @AllArgsConstructor
    private static class BoardCell {
        private int playerKey;
        private int armySize = 0;
        private Set<Integer> neighbours = new HashSet<>();
    }
}

package hu.kalmancheysandor.application.dominion.api.ai.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiRequest {
    private Integer yourKey;
    private Integer reserveSize;
    private Map<Integer, LandCell> landCells = new HashMap<>();

    @Data

    public static class LandCell {
        private Integer playerKey;
        private Integer troopSize = 0;
        private Set<Integer> neighbours = new HashSet<>();

        public LandCell(Integer playerKey, Integer troopSize, Set<Integer> neighbours) {
            this.playerKey = playerKey;
            this.troopSize = troopSize;
            this.neighbours = neighbours;
        }

        public boolean isEmpty() {
            if(playerKey==null) {
                return true;
            }
            return false;
        }
    }
}

package hu.kalmancheysandor.applications.dominions.apis.game.common.neo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public  class NeoState implements Serializable {
    private Map<Integer, NeoStateOpponent> players;
    private Map<Integer, NeoStateCell> cells;

    public NeoStateOpponent addPlayer(int index, NeoStateOpponent gameMapOpponent) {
        if (this.players == null) {
            this.players = new HashMap<>();
        }
        return this.players.put(index, gameMapOpponent);
    }

    public NeoStateCell addCell(int index, NeoStateCell cell) {

        if(this.cells==null) {
            this.cells = new HashMap<>();
        }

        return this.cells.put(index, cell);
    }

}

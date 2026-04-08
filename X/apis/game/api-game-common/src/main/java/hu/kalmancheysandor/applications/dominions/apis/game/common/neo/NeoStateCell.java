package hu.kalmancheysandor.applications.dominions.apis.game.common.neo;

import lombok.Data;

import java.io.Serializable;

@Data
public class NeoStateCell implements Serializable {
    private Integer playerKey;
    private int armySize;
    private int[] neighbours;
    private int occupiedCellCount;
}
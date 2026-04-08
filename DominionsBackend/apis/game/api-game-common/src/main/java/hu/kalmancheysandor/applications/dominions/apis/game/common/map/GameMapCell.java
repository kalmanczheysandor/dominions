package hu.kalmancheysandor.applications.dominions.apis.game.common.map;

import lombok.Data;

import java.io.Serializable;

@Data
public class GameMapCell implements Serializable {
    private Integer playerKey;
    private int armySize;
    private int[] neighbours;
    private int occupiedCellCount;
}
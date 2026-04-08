package hu.kalmancheysandor.applications.dominions.apis.game.common.map.state;

import lombok.Data;

import java.io.Serializable;

@Data
public class GameMapStateCell implements Serializable {
    private Integer occupierKey;
    private int armySize;
}
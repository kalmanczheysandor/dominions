package hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE
)
public class GameStateCell {
    private int occupierKey = -1;
    private int defendingTroopSize = 0;


    public GameStateCell(int occupierKey, int defendingTroopSize) {
        this.occupierKey = occupierKey;
        this.defendingTroopSize = defendingTroopSize;
    }

    public int getOccupierKey() {
        return occupierKey;
    }

    public int getDefendingTroopSize() {
        return defendingTroopSize;
    }

    public void setOccupierKey(int occupierKey) {
        this.occupierKey = occupierKey;
    }

    public void setDefendingTroopSize(int defendingTroopSize) {
        this.defendingTroopSize = defendingTroopSize;
    }

    public boolean isEmpty() {
        if (occupierKey == -1) {
            return true;
        }
        return false;
    }

    public void free() {
        occupierKey = -1;
        defendingTroopSize = 0;
    }

    @Override
    public String toString() {
        return "Cell{" + "occupierKey=" + occupierKey + ", defendingTroopSize=" + defendingTroopSize + '}';
    }
//        public void decrementTroopSize(int decrementWithValue) {
//            this.troopSize -= decrementWithValue;
//            if(this.troopSize<0) {
//                this.troopSize=0;
//            }
//
//        }
}

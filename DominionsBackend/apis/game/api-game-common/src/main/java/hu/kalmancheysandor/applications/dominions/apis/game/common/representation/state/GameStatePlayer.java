package hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.exception.UnexpectedCaseFoundGameStateException;
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
public class GameStatePlayer {
    private int reserveSize = 0;
    private boolean alive = true;

    public GameStatePlayer(int reserveSize) {
        this.reserveSize = reserveSize;
    }

    public int getReserveSize() {
        return reserveSize;
    }

    public void setReserveSize(int reserveSize) {
        if (reserveSize < 0) {
            throw new UnexpectedCaseFoundGameStateException("Reserve size must not be less than 0!");
        }
        this.reserveSize = reserveSize;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void incrementReserveSize(int incrementWithValue) {
        this.reserveSize += incrementWithValue;
    }

}

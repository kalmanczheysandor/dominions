package hu.kalmancheysandor.application.dominion.api.game.common.session;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public abstract class PlayerData {
    @Getter
    private final int index;
    private PlayerType playerType;
    @Getter
    private String name;

    @Getter @Setter
    private String intention = null;

    @Setter
    private boolean intentionGiven = false;

    public PlayerData(int index, String name, PlayerType playerType) {
        this.index = index;
        this.name = name;
        this.playerType = playerType;
    }

    public boolean isIntentionAlreadyGiven() {
        return intentionGiven;
    }

    public void eliminateIntention() {
        intentionGiven = false;
        intention = null;
    }

    public boolean isArtificial() {
        if(playerType==PlayerType.ARTIFICIAL) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerData that = (PlayerData) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(index);
    }

    protected enum PlayerType {
        HUMAN, ARTIFICIAL
    }
}

package hu.kalmancheysandor.application.dominion.api.game.common.session;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public abstract class PlayerData {
    @Getter
    private final int id;
    private PlayerType playerType;
    @Getter
    private String name;

    @Getter @Setter
    private String intention = null;

    @Setter
    private boolean intentionGiven = false;

    public PlayerData(int id,String name, PlayerType playerType) {
        this.id = id;
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
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    protected enum PlayerType {
        HUMAN, ARTIFICIAL
    }
}

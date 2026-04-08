package hu.kalmancheysandor.applications.dominions.apis.game.common.representation.action;


import java.util.Objects;

public class GameAction {
    private int playerKey;
    private Integer targetCellKey;
    private int attackingTroopSize;

    public GameAction(int playerKey, Integer targetCellKey, int attackingTroopSize) {
        this.playerKey = playerKey;
        this.targetCellKey = targetCellKey;
        this.attackingTroopSize = attackingTroopSize;
    }

    public GameAction() {

    }

    public int getPlayerKey() {
        return playerKey;
    }


    public Integer getTargetCellKey() {
        return targetCellKey;
    }

    public int getAttackingTroopSize() {
        return attackingTroopSize;
    }


    public void setPlayerKey(int playerKey) {
        this.playerKey = playerKey;
    }

    public void setTargetCellKey(Integer targetCellKey) {
        this.targetCellKey = targetCellKey;
    }

    public void setAttackingTroopSize(int attackingTroopSize) {
        this.attackingTroopSize = attackingTroopSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameAction action = (GameAction) o;
        return playerKey == action.playerKey;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerKey);
    }


    @Override
    public String toString() {
        return "GameAction{" +
            "playerKey=" + getPlayerKey() +
            ", targetCellKey=" + getTargetCellKey() +
            ", attackingTroopSize=" + getAttackingTroopSize() +
            '}';
    }
}

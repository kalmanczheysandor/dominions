package hu.kalmancheysandor.applications.dominions.apis.ai.algorithm.simultan;

import java.util.Objects;

public class ElementaryOperator {
    private int playerKey;
    private Integer targetIndex;

    public ElementaryOperator(int playerKey, Integer targetIndex) {
        this.playerKey = playerKey;
        this.targetIndex = targetIndex;
    }

    public int getPlayerKey() {
        return playerKey;
    }

    public Integer getTargetIndex() {
        return targetIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ElementaryOperator that = (ElementaryOperator) o;
        return playerKey == that.playerKey && Objects.equals(targetIndex, that.targetIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerKey, targetIndex);
    }
}

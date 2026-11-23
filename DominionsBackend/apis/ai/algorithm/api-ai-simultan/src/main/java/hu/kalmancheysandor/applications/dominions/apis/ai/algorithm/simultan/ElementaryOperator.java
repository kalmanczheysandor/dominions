package hu.kalmancheysandor.applications.dominions.apis.ai.algorithm.simultan;

import java.util.Objects;

public class ElementaryOperator {
    private int playerKey;
    private int operatorKey;

    public ElementaryOperator(int playerKey, int operatorKey) {
        this.playerKey = playerKey;
        this.operatorKey = operatorKey;
    }

    public int getPlayerKey() {
        return playerKey;
    }

    public int getOperatorKey() {
        return operatorKey;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ElementaryOperator that = (ElementaryOperator) o;
        return playerKey == that.playerKey && operatorKey == that.operatorKey;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerKey, operatorKey);
    }

    @Override
    public String toString() {
        return "ElementaryOperator{" +
                "playerKey=" + playerKey +
                ", operatorKey=" + operatorKey +
                '}';
    }
}

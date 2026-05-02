package hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo;

import hu.kalmancheysandor.applications.dominions.apis.ai.neural.IHeuristicEvaluation;


public class HugoHeuristicEvaluation implements Comparable<HugoHeuristicEvaluation>, IHeuristicEvaluation {
    private double occupancyShare;

    public HugoHeuristicEvaluation(double occupancyShare) {
        this.occupancyShare = occupancyShare;
    }

    @Override
    public double getHeuristicValue() {
        return occupancyShare;
    }

    @Override
    public int compareTo(HugoHeuristicEvaluation other) {
        return Double.compare(this.getHeuristicValue(), other.getHeuristicValue());
    }

}

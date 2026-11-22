package hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo.evalution;


import hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo.interfaces.IHugoHeuristicEvaluation;

public class HugoHeuristicEvaluation implements IHugoHeuristicEvaluation {
    private double occupancyShare;

    public HugoHeuristicEvaluation(double occupancyShare) {
        this.occupancyShare = occupancyShare;
    }

    @Override
    public double getHeuristicValue() {
        return occupancyShare;
    }


}

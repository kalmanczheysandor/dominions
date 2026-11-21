package hu.kalmancheysandor.applications.dominions.apis.ai.engine.liz.evalution;


import hu.kalmancheysandor.applications.dominions.apis.ai.engine.liz.interfaces.ILizOperatorScoreEvaluation;

public class LizOperatorScoreEvaluation implements Comparable<LizOperatorScoreEvaluation>, ILizOperatorScoreEvaluation {
    private double operatorScore;

    public LizOperatorScoreEvaluation(double operatorScore) {
        this.operatorScore = operatorScore;
    }

    @Override
    public double getOperatorScore() {
        return operatorScore;
    }

    @Override
    public int compareTo(LizOperatorScoreEvaluation other) {
        return Double.compare(this.getOperatorScore(), other.getOperatorScore());
    }

}

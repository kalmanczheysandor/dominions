package hu.kalmancheysandor.applications.dominions.apis.ai.engine.otto.evaluation;


import hu.kalmancheysandor.applications.dominions.apis.ai.engine.otto.interfaces.IOttoOperatorScoreEvaluation;

public class OttoOperatorScoreEvaluation implements Comparable<OttoOperatorScoreEvaluation>, IOttoOperatorScoreEvaluation {
    private double operationScore;

    public OttoOperatorScoreEvaluation(double operationScore) {
        this.operationScore = operationScore;
    }

    @Override
    public double getOperatorScore() {
        return operationScore;
    }

    @Override
    public int compareTo(OttoOperatorScoreEvaluation other) {
        return Double.compare(this.getOperatorScore(), other.getOperatorScore());
    }

}

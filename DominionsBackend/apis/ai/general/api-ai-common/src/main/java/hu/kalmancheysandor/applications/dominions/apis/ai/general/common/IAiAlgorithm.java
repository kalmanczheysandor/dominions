package hu.kalmancheysandor.applications.dominions.apis.ai.general.common;

public interface IAiAlgorithm {
    AiDecisionResult makeDecision(AiDecisionContext context);
}

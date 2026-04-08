package hu.kalmancheysandor.applications.dominions.apis.ai.common;

public interface IAiEngine {
    AiDecisionResult makeDecision(AiDecisionContext context);
}

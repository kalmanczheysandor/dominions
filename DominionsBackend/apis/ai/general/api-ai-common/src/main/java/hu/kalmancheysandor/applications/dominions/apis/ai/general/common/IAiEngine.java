package hu.kalmancheysandor.applications.dominions.apis.ai.general.common;

public interface IAiEngine {
    AiDecisionResult makeDecision(AiDecisionContext context);
}

package hu.kalmancheysandor.applications.dominions.apis.ai.neural;

@FunctionalInterface
public interface INeuralTrainingInterruptListener {
    boolean onListen();
}

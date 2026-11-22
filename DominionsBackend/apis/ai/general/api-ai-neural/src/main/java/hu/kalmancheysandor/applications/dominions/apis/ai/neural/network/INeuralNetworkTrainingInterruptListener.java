package hu.kalmancheysandor.applications.dominions.apis.ai.neural.network;

@FunctionalInterface
public interface INeuralNetworkTrainingInterruptListener {
    boolean onListen();
}

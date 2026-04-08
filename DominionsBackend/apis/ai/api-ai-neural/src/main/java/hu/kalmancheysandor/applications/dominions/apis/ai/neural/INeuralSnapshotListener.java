package hu.kalmancheysandor.applications.dominions.apis.ai.neural;

@FunctionalInterface
public interface INeuralSnapshotListener {
    void onListen(NeuralNetworkTrainingSnapshot snapshot);
}

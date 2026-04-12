package hu.kalmancheysandor.applications.dominions.apis.ai.neural;

@FunctionalInterface
public interface INeuralTrainingSnapshotListener {
    void onListen(NeuralNetworkTrainingSnapshot snapshot);
}

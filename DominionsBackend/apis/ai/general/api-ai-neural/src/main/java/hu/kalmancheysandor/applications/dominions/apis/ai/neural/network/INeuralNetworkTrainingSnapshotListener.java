package hu.kalmancheysandor.applications.dominions.apis.ai.neural.network;

import hu.kalmancheysandor.applications.dominions.apis.ai.neural.NeuralNetworkTrainingSnapshot;

@FunctionalInterface
public interface INeuralNetworkTrainingSnapshotListener {
    void onListen(NeuralNetworkTrainingSnapshot snapshot);
}

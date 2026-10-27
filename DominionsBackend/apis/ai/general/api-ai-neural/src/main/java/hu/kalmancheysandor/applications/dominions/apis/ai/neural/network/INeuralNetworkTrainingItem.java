package hu.kalmancheysandor.applications.dominions.apis.ai.neural.network;

public interface INeuralNetworkTrainingItem {
    public INeuralNetworkModelInput getNeuralNetworkModelInput();
    public INeuralNetworkModelOutput getNeuralNetworkModelOutput();
}

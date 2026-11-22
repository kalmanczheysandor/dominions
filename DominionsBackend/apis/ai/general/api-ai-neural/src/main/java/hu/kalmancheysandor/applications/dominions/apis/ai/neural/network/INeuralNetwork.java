package hu.kalmancheysandor.applications.dominions.apis.ai.neural.network;

import hu.kalmancheysandor.applications.dominions.apis.ai.neural.NeuralNetworkTrainingResult;

import java.util.List;


public interface INeuralNetwork {
    public void save();

    public void saveAs(String filePath);

    public void setTrainingSnapshotListener(INeuralNetworkTrainingSnapshotListener trainingSnapshotListener);

    public void setTrainingInterruptListener(INeuralNetworkTrainingInterruptListener trainingInterruptListener);

    public NeuralNetworkTrainingResult train(
            INeuralNetworkTrainingConfiguration trainingConfiguration,
            List<INeuralNetworkTrainingItem> trainingItemList,
            List<INeuralNetworkTrainingItem> testingItemList
    );


    public INeuralNetworkModelOutput calculate(INeuralNetworkModelInput inputData);
}

package hu.kalmancheysandor.applications.dominions.apis.ai.neural;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface INeuralNetwork {
    public void save();
    public void saveAs(String filePath);
    public NeuralNetworkTrainingResult train( List<INeuralInputData> trainingDataList,INeuralNetworkTrainingConfiguration trainingConfiguration,List<INeuralInputData> testingDataList);
    public void test( List<INeuralInputData> testingDataList);
    public double[] calculate(INeuralInputData inputData);
}

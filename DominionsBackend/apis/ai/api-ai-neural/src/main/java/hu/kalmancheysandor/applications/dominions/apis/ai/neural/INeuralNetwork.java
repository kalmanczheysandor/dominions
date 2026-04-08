package hu.kalmancheysandor.applications.dominions.apis.ai.neural;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Interface hogy mikent nezzenek ki a neuralis halok kifele a redszer fele. fuggetlenul attol hogy egyes implementaciokban, mely gyarto/fejleszto mely api-jat alkalamztam
 */
public interface INeuralNetwork {
    public void save();

    public void saveAs(String filePath);

    public void setSnapshotListener(INeuralSnapshotListener snapshotListener);
    public void setInterruptListener(INeuralInterruptListener interruptListener);

    public NeuralNetworkTrainingResult train(List<INeuralInputData> trainingDataList,INeuralNetworkTrainingConfiguration trainingConfiguration,List<INeuralInputData> testingDataList);
    //    public NeuralNetworkTrainingResult train( List<INeuralInputData> trainingDataList,INeuralNetworkTrainingConfiguration trainingConfiguration,List<INeuralInputData> testingDataList);
    public void test(List<INeuralInputData> testingDataList);

    public double[] calculate(INeuralInputData inputData);
}

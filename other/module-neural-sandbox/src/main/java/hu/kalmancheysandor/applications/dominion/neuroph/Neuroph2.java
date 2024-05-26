package hu.kalmancheysandor.applications.dominion.neuroph;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;

public class Neuroph2 {

    public static void main(String[] args) {
        // Step 1: Create and train the neural network
        DataSet trainingSet = new DataSet(2, 1);
        trainingSet.addRow(new DataSetRow(new double[]{0, 0}, new double[]{0}));
        trainingSet.addRow(new DataSetRow(new double[]{0, 1}, new double[]{1}));
        trainingSet.addRow(new DataSetRow(new double[]{1, 0}, new double[]{1}));
        trainingSet.addRow(new DataSetRow(new double[]{1, 1}, new double[]{0}));

        NeuralNetwork neuralNet = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 2, 2, 1);
        BackPropagation backPropagation = new BackPropagation();
        backPropagation.setMaxIterations(10000);

        neuralNet.learn(trainingSet,backPropagation);

        // Optional: Save the trained neural network
        neuralNet.save("trainedNeuralNet.nnet");
        System.out.println("Neural network saved to 'trainedNeuralNet.nnet'");

        // Optional: Load the neural network
        NeuralNetwork<?> loadedNeuralNet = NeuralNetwork.createFromFile("trainedNeuralNet.nnet");

        // Step 2: Create a test dataset (same as training set in this example)
        DataSet testSet = new DataSet(2, 1);
        testSet.addRow(new DataSetRow(new double[]{0, 0}, new double[]{0}));
        testSet.addRow(new DataSetRow(new double[]{0, 1}, new double[]{1}));
        testSet.addRow(new DataSetRow(new double[]{1, 0}, new double[]{1}));
        testSet.addRow(new DataSetRow(new double[]{1, 1}, new double[]{0}));

        // Step 3: Test the trained neural network
        System.out.println("Testing trained neural network:");
        for (DataSetRow dataRow : testSet.getRows()) {
            loadedNeuralNet.setInput(dataRow.getInput());
            loadedNeuralNet.calculate();
            double[] networkOutput = loadedNeuralNet.getOutput();
            double[] desiredOutput = dataRow.getDesiredOutput();

            System.out.println("Input: " + arrayToString(dataRow.getInput()) +
                " Desired Output: " + arrayToString(desiredOutput) +
                " Network Output: " + arrayToString(networkOutput));
        }
    }

    private static String arrayToString(double[] array) {
        StringBuilder sb = new StringBuilder();
        for (double value : array) {
            sb.append(String.format("%.4f ", value));
        }
        return sb.toString().trim();
    }
}

package hu.kalmancheysandor.applications.dominions.apis.ai.liz;

import hu.kalmancheysandor.applications.dominions.apis.ai.neural.*;
import hu.kalmancheysandor.applications.dominions.apis.general.utils.TArray;
import lombok.*;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.input.WeightedSum;
import org.neuroph.core.transfer.Sigmoid;
import org.neuroph.core.transfer.Tanh;
import org.neuroph.nnet.comp.neuron.InputNeuron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.NeuralNetworkType;

import java.io.File;
import java.util.*;

public class LizNeuralNetwork extends TNeuralNetwork {

    private final static int INPUT_VECTOR_ATTACKPOWER_SECTION_LEGNTH = 1;
    private Configuration configuration;
    private NeuralNetwork network;
    private String networkFilePath;
    private INeuralTrainingSnapshotListener trainingSnapshotListener;
    private INeuralTrainingInterruptListener trainingInterruptListener;

    @Override
    public void setTrainingSnapshotListener(INeuralTrainingSnapshotListener trainingSnapshotListener) {
        this.trainingSnapshotListener = trainingSnapshotListener;
    }

    @Override
    public void setTrainingInterruptListener(INeuralTrainingInterruptListener trainingInterruptListener) {
        this.trainingInterruptListener = trainingInterruptListener;
    }


    public LizNeuralNetwork(LizNeuralNetwork.Configuration configuration, String filePath) {
        Configuration.validation(configuration);
        this.configuration = configuration;
        this.networkFilePath = filePath;
        initialise();
    }


    private void initialise() {
        // Load network file
        File fileObj = new File(networkFilePath);
        if (!fileObj.exists()) { // If not exists than generate and save it
            NeuralNetwork network = assembleNeuralNetworkByData(determineInputVectorLength(), determineOutputVectorLength(), 4);
            network.save(networkFilePath);
        }
        network = NeuralNetwork.createFromFile(networkFilePath);
    }

    @Override
    public void save() {
        network.save(networkFilePath);
        System.out.println("Neural network saved to '" + networkFilePath + "'");
    }

    @Override
    public void saveAs(String filePath) {
        network.save(filePath);
        System.out.println("Neural network saved to '" + filePath + "'");
    }

    @Override
    public NeuralNetworkTrainingResult train(List<INeuralInputData> trainingDataList, INeuralNetworkTrainingConfiguration trainingConfiguration, List<INeuralInputData> testingDataList) {
        System.out.println("<<<< LizNeuralNetwork: Training.Start >>>>");

        // Convert
        List<LizInputData> trainingDataRows = LizInputData.convertInputDataList(trainingDataList);
        LizNeuralNetworkTrainingConfiguration lizTrainingConfiguration = LizNeuralNetworkTrainingConfiguration.generateInstance(trainingConfiguration);

        // Print training data list
        printTrainingDataList(trainingDataRows);

        // Set up training set by training data
        DataSet trainingSet = buildDataSet(trainingDataRows);
        ;

        // Set up configurations
        BackPropagation learningRule = new BackPropagation();
        learningRule.setMaxIterations(lizTrainingConfiguration.getMaxIterationsPerTurn());
        learningRule.setLearningRate(lizTrainingConfiguration.getLearningRate().doubleValue());
        network.setLearningRule(learningRule);


        // Initialise neural network and train it
        //List<NeuralNetworkTrainingSnapshot> trainingSnapshotsList = new ArrayList<>();
        NeuralNetworkEvaluationResult evaluationResult;
        boolean isCurrentIstanceSaved = false;
        boolean isInterrupted = false;
        double bestPrecisionRate = 0.0;
        int bestTurn = 0;
        int maxTurnCount = lizTrainingConfiguration.getMaxTurn();
        for (int turn = 1; turn <= maxTurnCount; turn++) {
            System.out.println("++++++++TURN:" + turn + "+++++++++++++++++++++++++++++++++++++++++++");

            // Checking: Whether to interrupt the long-running process;
            if (trainingInterruptListener.onListen()) {
                isInterrupted = true;
                break;
            }

            // Invocation of network representing 3rd-party api
            network.learn(trainingSet);

            // Evaluate current phase
            evaluationResult = evaluationOfANetwork(network, testingDataList);

            // Save network-instance into file if precision is better in current turn than in previous turn
            if (evaluationResult.getPrecisionRate() > bestPrecisionRate) {
                network.save(networkFilePath);      // It overwrites previous save
                bestPrecisionRate = evaluationResult.getPrecisionRate();
                bestTurn = turn;
                isCurrentIstanceSaved = true;
            }

            // Send snapshot data to listener
            if (trainingSnapshotListener != null) {
                trainingSnapshotListener.onListen(NeuralNetworkTrainingSnapshot.builder()
                        .turn(turn)
                        .currentBestTurn(bestTurn)
                        .evaluationResult(evaluationResult)
                        .iterationCount(lizTrainingConfiguration.getMaxIterationsPerTurn())
                        .build()
                );
            }


        }

        //
        if (isInterrupted) {
            System.out.println("<<<< LizNeuralNetwork: Training.END: Cancelled >>>>");

            // Generate output
            return NeuralNetworkTrainingResult.builder()
                    .precisionRate(bestPrecisionRate)
                    .turnBest(bestTurn)
                    .turnCount(maxTurnCount)
                    .status(NeuralNetworkTrainingResult.Status.CANCELED)
                    .build();
        }

        // Load the final best! It is necessary because the user can invoke the save method after this train method, and it would save the very latest version of network, and that version is might not the best
        if (isCurrentIstanceSaved) {
            network = NeuralNetwork.createFromFile(networkFilePath);
        }

        System.out.println("<<<< LizNeuralNetwork: Training.END: Successful >>>>");
        // Generate output
        return NeuralNetworkTrainingResult.builder()
                .precisionRate(bestPrecisionRate)
                .turnBest(bestTurn)
                .turnCount(maxTurnCount)
                .status(NeuralNetworkTrainingResult.Status.DONE)
                .build();
    }


    private void process() {

    }


    private boolean evaluationOfAnOutput(double[] networkOutput, double[] desiredOutput) {
        boolean isOk = true;
        for (int i = 0; i < networkOutput.length; i++) {

            double outputValue = networkOutput[i];
            double desiredOutputValue = desiredOutput[i];
            if (desiredOutputValue == 1.0) {
                if (outputValue < 0.9) {
                    isOk = false;
                }
            } else if (desiredOutputValue == 0.0) {
                if (outputValue >= 0.1) {
                    isOk = false;
                }
            } else {
                throw new RuntimeException("Unexpected desired output: " + desiredOutputValue);
            }

        }
        return isOk;
    }

    private NeuralNetworkEvaluationResult evaluationOfANetwork(NeuralNetwork networkObj, List<INeuralInputData> testingDataList) {
        // Convert
        List<LizInputData> testingDataRows = LizInputData.convertInputDataList(testingDataList);

        // Build data set
        DataSet testingSet = buildDataSet(testingDataRows);

        // Test the trained neural network
        int okCount = 0;
        for (DataSetRow dataRow : testingSet.getRows()) {
            networkObj.setInput(dataRow.getInput());
            networkObj.calculate();

            double[] networkOutput = networkObj.getOutput();
            double[] desiredOutput = dataRow.getDesiredOutput();

            boolean result = evaluationOfAnOutput(networkOutput, desiredOutput);
            if (result) {
                okCount++;
            }
        }

        double precisionRate = 0.0;
        int itemsCount = testingSet.size();
        if (itemsCount > 0) {
            precisionRate = (double) okCount / (double) itemsCount;
        }

        System.out.println("okCount: " + okCount);
        System.out.println("itemsCount: " + itemsCount);
        System.out.println("precisionRate: " + precisionRate);

        return NeuralNetworkEvaluationResult.builder().precisionRate(precisionRate).build();
    }


    @Override
    public void test(List<INeuralInputData> testingDataList) {
        System.out.println("<<<< LizNeuralNetwork: Testing >>>>");

        // Convert
        List<LizInputData> testingDataRows = LizInputData.convertInputDataList(testingDataList);

        // Build data set
        DataSet testingSet = buildDataSet(testingDataRows);
//        printDataSet(testingSet);

        // Test the trained neural network
        System.out.println("Testing trained neural network:");
        int okCount = 0;
        for (DataSetRow dataRow : testingSet.getRows()) {
            network.setInput(dataRow.getInput());
            network.calculate();

            double[] networkOutput = network.getOutput();
            double[] desiredOutput = dataRow.getDesiredOutput();

            boolean result = evaluationOfAnOutput(networkOutput, desiredOutput);
            if (result) {
                okCount++;
            }

        }

        double precisionRate = 0.0;
        int itemsCount = testingSet.size();
        if (itemsCount > 0) {
            precisionRate = (double) okCount / (double) itemsCount;
        }

//        System.out.println("OkCount: " + okCount);
//        System.out.println("ItemsCount: " + itemsCount);
        System.out.println("Precision Rateeee: " + precisionRate);
    }

    @Override
    public double[] calculate(INeuralInputData inputData) {
        System.out.println("<<<< LizNeuralNetwork: Calculate >>>>");

        // Convert
        LizInputData lizInputData = LizInputData.generateInstance(inputData);
        double[] inputVector = generateInputVectorByTrainingData(lizInputData);


        // Get result from the model
        double[] normalisedOutputVector;
        network.setInput(inputVector);
        network.calculate();
        normalisedOutputVector = network.getOutput();
        if (normalisedOutputVector.length != determineOutputVectorLength()) {
            throw new RuntimeException("Output size is wrong!");
        }

        // Denormalisation - Cell hit scores
        double[] cellHitScoresNormalised = new double[determineOutputVectorAttackProbabilitySectionLength()];
        System.arraycopy(normalisedOutputVector, 0, cellHitScoresNormalised, 0, determineOutputVectorAttackProbabilitySectionLength());
        double[] cellHitScoresDenormalised = new double[determineOutputVectorAttackProbabilitySectionLength()];
        for (int i = 0; i < cellHitScoresNormalised.length; i++) {
            cellHitScoresDenormalised[i] = denormaliseValue(cellHitScoresNormalised[i], 0, 100); // values denormalised between 0% and 100%
        }

        // Denormalisation - Attacking troops size
        double attackingTroopSizeNormalised = normalisedOutputVector[normalisedOutputVector.length - 1]; // Pointing to the last index
        double attackingTroopSizeDenormalised = denormaliseValue(attackingTroopSizeNormalised, configuration.getAttackPowerMin(), configuration.getAttackPowerMax());

        // Generate output
        double[] output = new double[determineOutputVectorLength()];
        System.arraycopy(cellHitScoresDenormalised, 0, output, 0, cellHitScoresDenormalised.length);
        output[output.length - 1] = attackingTroopSizeDenormalised;
        printOutputVectorInfo(output);

        return output;
    }

    private void printOutputVectorInfo(double[] outputVector) {
        String[] s1 = new String[determineOutputVectorLength()];
        String[] s2 = new String[determineOutputVectorLength()];
        String[] s3 = new String[determineOutputVectorLength()];

        int columnIndex = 0;
        for (double value : outputVector) {
            if (columnIndex + 1 <= determineOutputVectorAttackProbabilitySectionLength()) {
                s1[columnIndex] = "C" + columnIndex;
                s2[columnIndex] = "";
                if (value > 0.0) {
                    s2[columnIndex] = "X";
                }

                s3[columnIndex] = value + "%";
            } else if (columnIndex + 1 <= determineOutputVectorAttackProbabilitySectionLength() + 1) {
                s1[columnIndex] = "AttackPower";
                s2[columnIndex] = "";
                s3[columnIndex] = String.valueOf(value);
            }
            columnIndex++;
        }

        TArray.printMatrix(new Object[][]{
                s1,
                s2,
                s3
        });
    }

    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Network assembling /////////////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//
//    private static NeuralNetwork<?> assembleNeuralNetworkByData(int inputSize, int outputSize) {
//
//        // --- Input réteg ---
//        Layer inputLayer = new Layer();
//        for (int i = 0; i < inputSize; i++) {
//            inputLayer.addNeuron(new InputNeuron());  // input neuron
//        }
//
//        // --- Rejtett réteg 1 ---
//        Layer hiddenLayerOne = new Layer();
//        for (int i = 0; i < inputSize; i++) {
//            hiddenLayerOne.addNeuron(new Neuron());
//        }
//
//        // --- Rejtett réteg 2 ---
//        Layer hiddenLayerTwo = new Layer();
//        for (int i = 0; i < inputSize; i++) {
//            hiddenLayerTwo.addNeuron(new Neuron(,new Tanh()));
//        }
//
//        // --- Output réteg ---
//        Layer outputLayer = new Layer();
//        for (int i = 0; i < outputSize; i++) {
//            outputLayer.addNeuron(new Neuron(TransferFunctionType.SIGMOID.getTransferFunction()));
//        }
//
//        // --- Háló létrehozása ---
//        NeuralNetwork<Neuron> network = new NeuralNetwork<>();
//
//        network.addLayer(0, inputLayer);
//        network.addLayer(1, hiddenLayerOne);
//        ConnectionFactory.fullConnect(inputLayer, hiddenLayerOne); // teljes összekötés + bias
//
//        network.addLayer(2, hiddenLayerTwo);
//        ConnectionFactory.fullConnect(hiddenLayerOne, hiddenLayerTwo); // teljes összekötés + bias
//
//        network.addLayer(3, outputLayer);
//        ConnectionFactory.fullConnect(hiddenLayerTwo, outputLayer); // teljes összekötés + bias
//
//        // opcionális: skip connection az inputról outputra (bias nélkül)
//        ConnectionFactory.fullConnect(inputLayer, outputLayer, false);
//
//        // --- Bemeneti és kimeneti neuronok beállítása ---
//        network.setInputNeurons(inputLayer.getNeurons());
//        network.setOutputNeurons(outputLayer.getNeurons());
//
//        network.setNetworkType(NeuralNetworkType.MULTI_LAYER_PERCEPTRON);
//
//        return network;
//    }

//
//    private static NeuralNetwork<?> assembleNeuralNetworkByData(int inputSize, int outputSize) {
//
//        Layer inputLayer = new Layer();
//        for (int i = 1; i <= inputSize; i++) {
//            inputLayer.addNeuron(new Neuron());
//        }
//
//        Layer hiddenLayerOne = new Layer();
//        for (int i = 1; i <= inputSize; i++) {
//            hiddenLayerOne.addNeuron(new Neuron());
//        }
//
//        Layer hiddenLayerTwo = new Layer();
//        for (int i = 1; i <= inputSize; i++) {
//            hiddenLayerTwo.addNeuron(new Neuron());
//        }
//
//        Layer outputLayer = new Layer();
//        for (int i = 1; i <= outputSize; i++) {
//            outputLayer.addNeuron(new Neuron());
//        }
//
//        NeuralNetwork network = new NeuralNetwork();
//
//        network.addLayer(0, inputLayer);
//        network.addLayer(1, hiddenLayerOne);
//        ConnectionFactory.fullConnect(network.getLayerAt(0), network.getLayerAt(1));
//
//        network.addLayer(2, hiddenLayerTwo);
//        ConnectionFactory.fullConnect(network.getLayerAt(1), network.getLayerAt(2));
//
//        network.addLayer(3, outputLayer);
//        ConnectionFactory.fullConnect(network.getLayerAt(2), network.getLayerAt(3));
//        ConnectionFactory.fullConnect(network.getLayerAt(0), network.getLayerAt(network.getLayersCount() - 1), false);
//
//        network.setInputNeurons(inputLayer.getNeurons());
//        network.setOutputNeurons(outputLayer.getNeurons());
//
//        network.setNetworkType(NeuralNetworkType.MULTI_LAYER_PERCEPTRON);
//        return network;
//    }
    private static Layer generateAnInputLayer(int neuronCount) {
        Layer layer = new Layer();
        for (int i = 1; i <= neuronCount; i++) {
            layer.addNeuron(new InputNeuron());
        }
        return layer;
    }

    private static Layer generateAnOutputLayer(int neuronCount) {
        Layer layer = new Layer();
        for (int i = 1; i <= neuronCount; i++) {
            layer.addNeuron(new Neuron(new WeightedSum(), new Sigmoid()));
        }
        return layer;
    }

    private static Layer generateAHiddenLayer(int neuronCount) {
        Layer layer = new Layer();
        for (int i = 1; i <= neuronCount; i++) {
            layer.addNeuron(new Neuron(new WeightedSum(), new Tanh()));
        }
        return layer;
    }

    private static NeuralNetwork<?> assembleNeuralNetworkByData(int inputSize, int outputSize, int hiddenLayerCount) {
        // Initialise the network
        NeuralNetwork network = new NeuralNetwork();

        // Attach the input layer
        Layer inputLayer = generateAnInputLayer(inputSize);
        network.addLayer(0, inputLayer);

        // Attach all hidden layer
        int latestLayerIndex = 0;
        int currentLayerIndex = 1;
        while (currentLayerIndex <= hiddenLayerCount) {

            // Attach a new hidden layers
            //int neuronsInLayer = inputSize - currentLayerIndex * (inputSize - outputSize) / (hiddenLayerCount + 1);
            int neuronsInLayer = inputSize;
            Layer hiddenLayer = generateAHiddenLayer(neuronsInLayer);

            network.addLayer(currentLayerIndex, hiddenLayer);
            ConnectionFactory.fullConnect(network.getLayerAt(latestLayerIndex), network.getLayerAt(currentLayerIndex));

            // Increment indexes
            latestLayerIndex = currentLayerIndex;
            currentLayerIndex++;
        }

        // Attach the output layer
        Layer outputLayer = generateAnOutputLayer(outputSize);
        network.addLayer(currentLayerIndex, outputLayer);
        ConnectionFactory.fullConnect(network.getLayerAt(latestLayerIndex), network.getLayerAt(currentLayerIndex));
//        ConnectionFactory.fullConnect(network.getLayerAt(0), network.getLayerAt(currentLayerIndex), false);

        network.setInputNeurons(inputLayer.getNeurons());
        network.setOutputNeurons(outputLayer.getNeurons());

        network.setNetworkType(NeuralNetworkType.MULTI_LAYER_PERCEPTRON);
        return network;
    }


    private NeuralNetwork<?> assembleNeuralNetworkByNetworkFile(String filePath) {
        // Load file
        File fileObj = new File(filePath);
        if (!fileObj.exists()) {
            System.out.println("Neural network file does not exist:" + filePath);
            return null;
        }

        System.out.println("Neural network load from:" + filePath);
        return NeuralNetwork.createFromFile(filePath);
    }


    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Build methods //////////////////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private DataSet buildDataSet(List<LizInputData> trainingDataList) {
        DataSet trainingSet = new DataSet(determineInputVectorLength(), determineOutputVectorLength());
        for (LizInputData trainingData : trainingDataList) {
            trainingSet.addRow(
                    new DataSetRow(
                            generateInputVectorByTrainingData(trainingData),
                            generateOutputVectorByTrainingData(trainingData)
                    )
            );
        }
        return trainingSet;
    }

    public double[] generateInputVectorByTrainingData(LizInputData trainingData) {

        // Input checking - attackPower
        if (trainingData.getAttackPower() < configuration.getAttackPowerMin()) {
            throw new RuntimeException("Value of attackPower('" + trainingData.getAttackPower() + "') must not be under than interval('" + configuration.getAttackPowerMin() + "') minimum!");
        }
        if (trainingData.getAttackPower() > configuration.getAttackPowerMax()) {
            throw new RuntimeException("Value of maxAttackPower('" + trainingData.getAttackPower() + "')must not be above than interval('" + configuration.getAttackPowerMax() + "') maximum!");
        }

        // Input checking - cell owners
        if (trainingData.getCellOwnerRanks().length != determineInputVectorOwnersSectionLength()) {
            throw new RuntimeException("Invalid length of cell owner ranks!");
        }
        if (trainingData.getCellDefendersSize().length != determineInputVectorDefendersSectionLength()) {
            throw new RuntimeException("Invalid length of cell defenders!");
        }

        // Initialisation
        ArrayList<Double> inputNodeValuesList = new ArrayList<>();

        // Input nodes - Reserve size
        inputNodeValuesList.add(normaliseValue(trainingData.getAttackPower(), configuration.getAttackPowerMin(), configuration.getAttackPowerMax()));

        // Input nodes - Cell owners
        for (int ownerKey : trainingData.getCellOwnerRanks()) {
            //int swappedKey = ownerKey + 1; // so -1 will be 0 and will mean empty. (Originally, in game state representation, -1 was referred to empty cell)
            double normalisedKey = normaliseValue(ownerKey, configuration.getOwnerRankMinIndex(), configuration.getOwnerRankMaxIndex());
            inputNodeValuesList.add(normalisedKey);
        }

        // Input nodes - Defending troop size
        for (int defenderSize : trainingData.getCellDefendersSize()) {
            inputNodeValuesList.add(normaliseValue(defenderSize, configuration.getDefendersMinCount(), configuration.getDefendersMaxCount()));
        }

        return inputNodeValuesList.stream().mapToDouble(i -> i).toArray();
    }

    private double[] generateOutputVectorByTrainingData(LizInputData trainingData) {
        // Input checking - Cell owners
        if (trainingData.getCellOwnerRanks().length != determineInputVectorOwnersSectionLength()) {
            throw new RuntimeException("Invalid length of cell owner ranks!");
        }
        if (trainingData.getCellDefendersSize().length != determineInputVectorDefendersSectionLength()) {
            throw new RuntimeException("Invalid length of cell defenders!");
        }

        // Initialise
        int cellCount = trainingData.getCellOwnerRanks().length;

        // Output nodes - Targeted cell percentage
        double[] outputNodeValuesArray = new double[cellCount];     // By default, all values are 0 (because array default values) which means 0% probability
        if (trainingData.getChosenTarget() != -1) {        // When attack action happened and not reserve action
            outputNodeValuesArray[trainingData.getChosenTarget()] = normaliseValue(100, 0, 100);        // The potentiality id 100%, others 0%
        }
        return outputNodeValuesArray;
    }

    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Helper methods //////////////////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private int determineInputVectorLength() {
        //return INPUT_VECTOR_ATTACKPOWER_SECTION_LEGNTH + INPUT_VECTOR_ENEMY_SECTION_LEGNTH + determineInputVectorOwnersSectionLength() + determineInputVectorDefendersSectionLength(); // 1X attackPower + 1X enemiesCount + 42X cellOwnerKey + 42X cellDefendersSizeValue
        return INPUT_VECTOR_ATTACKPOWER_SECTION_LEGNTH + determineInputVectorOwnersSectionLength() + determineInputVectorDefendersSectionLength(); // 1X attackPower + 1X enemiesCount + 42X cellOwnerKey + 42X cellDefendersSizeValue
    }

    private int determineOutputVectorLength() {
        return determineOutputVectorAttackProbabilitySectionLength();// 42X CellHitScore (probability of attack) + 1X AttackingTroopSize
    }

    private int determineInputVectorOwnersSectionLength() {
        return configuration.getCellCount();
    }

    private int determineInputVectorDefendersSectionLength() {
        return configuration.getCellCount();
    }

    private int determineOutputVectorAttackProbabilitySectionLength() {
        return configuration.getCellCount();
    }


    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Print methods //////////////////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static void printTrainingDataList(List<LizInputData> trainingDataList) {
        for (LizInputData trainingData : trainingDataList) {
            System.out.println(trainingData);
        }
    }

    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// INNER CLASSES //////////////////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Configuration {

        private int enemiesCountMin;
        private int enemiesCountMax;

        private int ownerRankMinIndex;
        private int ownerRankMaxIndex;

        private int defendersMinCount;
        private int defendersMaxCount;

        private int attackPowerMin;
        private int attackPowerMax;

        private int cellCount;


        private static void validation(Configuration configuration) {
            // Validations
            if (configuration.enemiesCountMax <= configuration.enemiesCountMin) {
                throw new RuntimeException("Parameter ...max must be grater then parameter ...min!");
            }

            if (configuration.ownerRankMaxIndex <= configuration.ownerRankMinIndex) {
                throw new RuntimeException("Parameter ...max must be grater then parameter ...min!");
            }

            if (configuration.defendersMaxCount <= configuration.defendersMinCount) {
                throw new RuntimeException("Parameter ...max must be grater then parameter ...min!");
            }

            if (configuration.attackPowerMax <= configuration.attackPowerMin) {
                throw new RuntimeException("Parameter ...max must be grater then parameter ...min!");
            }


        }

        public void validate() {
            Configuration.validation(this);
        }

    }
}

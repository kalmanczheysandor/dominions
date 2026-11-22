package hu.kalmancheysandor.applications.dominions.apis.ai.engine.liz.neural;

import hu.kalmancheysandor.applications.dominions.apis.ai.neural.*;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.network.*;
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
import java.util.stream.IntStream;

public class LizNeuralNetwork extends TNeuralNetwork implements INeuralNetwork {

    private LizNeuralNetworkConfiguration configuration;
    private NeuralNetwork network;
    private String networkFilePath;
    private INeuralNetworkTrainingSnapshotListener trainingSnapshotListener;
    private INeuralNetworkTrainingInterruptListener trainingInterruptListener;

    @Override
    public void setTrainingSnapshotListener(INeuralNetworkTrainingSnapshotListener trainingSnapshotListener) {
        this.trainingSnapshotListener = trainingSnapshotListener;
    }

    @Override
    public void setTrainingInterruptListener(INeuralNetworkTrainingInterruptListener trainingInterruptListener) {
        this.trainingInterruptListener = trainingInterruptListener;
    }


    public LizNeuralNetwork(LizNeuralNetworkConfiguration configuration, String filePath) {
        LizNeuralNetworkConfiguration.validation(configuration);
        this.configuration = configuration;
        this.networkFilePath = filePath;
        initialise();
    }


    private void initialise() {
        // Load network file
        File fileObj = new File(networkFilePath);
        if (!fileObj.exists()) { // If not exists than generate and save it
            NeuralNetwork network = assembleNeuralNetworkByData(configuration);
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
    public NeuralNetworkTrainingResult train(INeuralNetworkTrainingConfiguration trainingConfiguration, List<INeuralNetworkTrainingItem> trainingItemList, List<INeuralNetworkTrainingItem> testingItemList) {
        System.out.println("<<<< LizNeuralNetwork: Training.Start >>>>");

        // Convert
        List<LizNeuralNetworkTrainingItem> trainingItems = LizNeuralNetworkTrainingItem.convertTrainingItemList(trainingItemList);
        List<LizNeuralNetworkTrainingItem> testingItems = LizNeuralNetworkTrainingItem.convertTrainingItemList(testingItemList);
        LizNeuralNetworkTrainingConfiguration lizTrainingConfiguration = LizNeuralNetworkTrainingConfiguration.generateInstance(trainingConfiguration);

        // Print training data list
        //printTrainingDataList(trainingDataRows);

        // Set up training set by training data
        DataSet trainingSet = buildTrainingDataSet(trainingItems);
        DataSet testingSet = buildTrainingDataSet(testingItems);

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
            evaluationResult = evaluationOfANetwork(network, testingSet);

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

    private NeuralNetworkEvaluationResult evaluationOfANetwork(NeuralNetwork networkObj, DataSet testingSet) {

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

        return NeuralNetworkEvaluationResult.builder()
                .precisionRate(precisionRate)
                .build();
    }

    @Override
    public INeuralNetworkModelOutput calculate(INeuralNetworkModelInput modelInput) {
        System.out.println("<<<< LizNeuralNetwork: calculate >>>>");

        // Convert
        LizNeuralNetworkModelInput modelInputObj = LizNeuralNetworkModelInput.convertInstance(modelInput);

        // Vectorisation
        double[] modelInputVector = convertModelInputObjToModelInputVector(modelInputObj);
        System.out.println("[LIZ.calculate] Neural network input vector is: " + java.util.Arrays.toString(modelInputVector));

        // Get result from the model
        network.setInput(modelInputVector);
        network.calculate();
        double[] modelOutputVector = network.getOutput();

        System.out.println("[LIZ.calculate] Neural network output data is: " + java.util.Arrays.toString(modelOutputVector));

        return convertModelOutputVectorToModelOutputObj(modelOutputVector);
    }



    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Network assembling /////////////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    private static NeuralNetwork<?> assembleNeuralNetworkByData(LizNeuralNetworkConfiguration configuration) {
        // Initialise the network
        NeuralNetwork network = new NeuralNetwork();

        // Attach the input layer
        Layer inputLayer = generateAnInputLayer(configuration.determineInputLayerSize());
        network.addLayer(0, inputLayer);

        // Attach all hidden layer
        int latestLayerIndex = 0;
        int currentLayerIndex = 1;
        while (currentLayerIndex <= configuration.getHiddenLayerCount()) {

            // Attach a new hidden layers
            int neuronsInLayer = configuration.determineHiddenLayerSize();
            Layer hiddenLayer = generateAHiddenLayer(neuronsInLayer);

            network.addLayer(currentLayerIndex, hiddenLayer);
            ConnectionFactory.fullConnect(network.getLayerAt(latestLayerIndex), network.getLayerAt(currentLayerIndex));

            // Increment indexes
            latestLayerIndex = currentLayerIndex;
            currentLayerIndex++;
        }

        // Attach the output layer
        Layer outputLayer = generateAnOutputLayer(configuration.determineOutputLayerSize());
        network.addLayer(currentLayerIndex, outputLayer);
        ConnectionFactory.fullConnect(network.getLayerAt(latestLayerIndex), network.getLayerAt(currentLayerIndex));
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
    private DataSet buildTrainingDataSet(List<LizNeuralNetworkTrainingItem> trainingItems) {
        //
        DataSet trainingSet = new DataSet(determineInputVectorLength(), determineOutputVectorLength());

        //
        for (LizNeuralNetworkTrainingItem trainingItem : trainingItems) {

            LizNeuralNetworkModelInput modelInputObj = LizNeuralNetworkModelInput.convertInstance(trainingItem.getNeuralNetworkModelInput());
            LizNeuralNetworkModelOutput modelOutputObj = LizNeuralNetworkModelOutput.convertInstance(trainingItem.getNeuralNetworkModelOutput());
            double[] modelInputVector = convertModelInputObjToModelInputVector(modelInputObj);
            double[] modelOutputVector = convertModelOutputObjToModelOutputVector(modelOutputObj);

            //
            trainingSet.addRow(
                    new DataSetRow(modelInputVector, modelOutputVector)
            );
        }
        return trainingSet;
    }

    private static void copyIntIntoIntAndFill(int[] source, int[] target) {
        IntStream.range(0, target.length)
                .forEach(i -> {
                    if (i < source.length) {
                        target[i] = source[i];
                    } else {
                        target[i] = -1;
                    }
                });
    }
    private static void copyDoubleIntoDoubleAndFillWithZero(double[] source, double[] target) {
        IntStream.range(0, target.length)
                .forEach(i -> {
                    if (i < source.length) {
                        target[i] = source[i];
                    } else {
                        target[i] = 0.0;
                    }
                });
    }

    private static void copyBooleanIntoIntAndFill(boolean[] source, int[] target) {
        IntStream.range(0, target.length)
                .forEach(i -> {
                    if (i < source.length) {
                        if (source[i]) {
                            target[i] = 1;
                        } else {
                            target[i] = 0;
                        }
                    } else {
                        target[i] = -1;
                    }
                });
    }

    private double[] convertModelInputObjToModelInputVector(LizNeuralNetworkModelInput modelInputObj) {

        if (modelInputObj.getCellOccupierIndex().length > configuration.getInputLayer_cellOccupierIndexNodesSize()) {
            throw new RuntimeException("Cell occupier index out of bounds!"+modelInputObj.getCellOccupierIndex().length+" > "+configuration.getInputLayer_cellOccupierIndexNodesSize());
        }
        if (modelInputObj.getCellGarrisonSize().length > configuration.getInputLayer_cellGarrisonSizeNodesSize()) {
            throw new RuntimeException("Cell garrison size out of bounds!"+modelInputObj.getCellGarrisonSize().length+" > "+configuration.getInputLayer_cellGarrisonSizeNodesSize());
        }
        if (modelInputObj.getPlayerTerritorySize().length > configuration.getInputLayer_playerTerritorySizeNodesSize()) {
            throw new RuntimeException("Player territory size out of bounds!"+modelInputObj.getPlayerTerritorySize().length+" > "+configuration.getInputLayer_playerTerritorySizeNodesSize());
        }

        if (modelInputObj.getPlayerReserveSize().length > configuration.getInputLayer_playerReserveSizeNodesSize()) {
            throw new RuntimeException("Player reserve size out of bounds!"+modelInputObj.getPlayerReserveSize().length+" > "+configuration.getInputLayer_playerReserveSizeNodesSize());
        }

        if (modelInputObj.getPlayerIsAlive().length > configuration.getInputLayer_playerIsAliveNodesSize()) {
            throw new RuntimeException("Player is alive array out of bounds!"+modelInputObj.getPlayerIsAlive().length+" > "+configuration.getInputLayer_playerIsAliveNodesSize());
        }


        //
        int[] cellOccupierIndex = new int[configuration.getInputLayer_cellOccupierIndexNodesSize()];
        int[] cellGarrisonSize = new int[configuration.getInputLayer_cellGarrisonSizeNodesSize()];
        int[] playerTerritorySize = new int[configuration.getInputLayer_playerTerritorySizeNodesSize()];
        int[] playerReserveSize = new int[configuration.getInputLayer_playerReserveSizeNodesSize()];
        int[] playerIsAlive = new int[configuration.getInputLayer_playerIsAliveNodesSize()];

        copyIntIntoIntAndFill(modelInputObj.getCellOccupierIndex(), cellOccupierIndex);
        copyIntIntoIntAndFill(modelInputObj.getCellGarrisonSize(), cellGarrisonSize);
        copyIntIntoIntAndFill(modelInputObj.getPlayerTerritorySize(), playerTerritorySize);
        copyIntIntoIntAndFill(modelInputObj.getPlayerReserveSize(), playerReserveSize);
        copyBooleanIntoIntAndFill(modelInputObj.getPlayerIsAlive(), playerIsAlive);

        // Normalisations
        ArrayList<Double> inputNodeValuesList = new ArrayList<>();


        // Input nodes - cellOccupierIndex
        for (int occupierKey : cellOccupierIndex) {
            inputNodeValuesList.add(
                    normaliseValue(occupierKey, -1, configuration.getMaximumPlayerIndex())
            );
        }

        // Input nodes - cellGarrisonSize
        for (int garrisonSize : cellGarrisonSize) {
            inputNodeValuesList.add(
                    normaliseValue(garrisonSize, -1, configuration.getMaximumGarrisonSize())
            );
        }

        // Input nodes - playerTerritorySize
        for (int territorySize : playerTerritorySize) {
            inputNodeValuesList.add(
                    normaliseValue(territorySize, -1, configuration.getMaximumCellCount())
            );
        }

        // Input nodes - playerReserveSize
        for (int reserveSize : playerReserveSize) {
            inputNodeValuesList.add(
                    normaliseValue(reserveSize, -1, configuration.getMaximumReserveSize())
            );
        }


        // Input nodes - playerIsAlive
        for (int isAlive : playerIsAlive) {
            inputNodeValuesList.add(
                    normaliseValue(isAlive, -1, configuration.getMaximumAliveValue())
            );
        }

        return inputNodeValuesList.stream().mapToDouble(i -> i).toArray();
    }

    private double[] convertModelOutputObjToModelOutputVector(LizNeuralNetworkModelOutput modelOutputObj) {

        // Input checking
        if (modelOutputObj.getOperatorProbability().length > determineOutputVectorLength()) {
            throw new RuntimeException("Operator probability out of bounds!");
        }

        double[] operatorProbability = new double[determineOutputVectorLength()]; // Int is not problem because it is a flag
        copyDoubleIntoDoubleAndFillWithZero(modelOutputObj.getOperatorProbability(), operatorProbability);

        // Normalisations
        ArrayList<Double> outputNodeValuesList = new ArrayList();

        // Input nodes - cellOccupierIndex
        for (double probability : operatorProbability) {
            outputNodeValuesList.add(probability);
        }

        return outputNodeValuesList.stream().mapToDouble(i -> i).toArray();
    }

    private LizNeuralNetworkModelOutput convertModelOutputVectorToModelOutputObj(double[] modelOutputVector) {

        // Input checking
        if (modelOutputVector.length != determineOutputVectorLength()) {
            throw new RuntimeException("Output size is wrong!");
        }





        // Denormalisation
        double[] operatorProbabilityNormalised = new double[determineOutputVectorLength()];
        double[] operatorProbabilityDenormalised = new double[determineOutputVectorLength()];
        System.arraycopy(modelOutputVector, 0, operatorProbabilityNormalised, 0, determineOutputVectorLength());


        for (int i = 0; i < operatorProbabilityNormalised.length; i++) {
            operatorProbabilityDenormalised[i] = denormaliseValue(operatorProbabilityNormalised[i], 0, 1); // values denormalised between 0% and 100%
        }

        System.out.println("[NORMALIZED] Neural network output vector is: " + java.util.Arrays.toString(operatorProbabilityNormalised));
        System.out.println("[DENORMALIZED] Neural network output vector is: " + java.util.Arrays.toString(operatorProbabilityDenormalised));
        return LizNeuralNetworkModelOutput.builder()
                .operatorProbability(operatorProbabilityDenormalised)
                .build();
    }


    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Helper methods //////////////////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private int determineInputVectorLength() {
        return configuration.determineInputLayerSize();
    }

    private int determineOutputVectorLength() {
        return configuration.determineOutputLayerSize();
    }


    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Print methods //////////////////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static void printTrainingDataList(List<LizNeuralNetworkModelInput> trainingDataList) {
        for (LizNeuralNetworkModelInput trainingData : trainingDataList) {
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
    public static class LizNeuralNetworkConfiguration {

        private int hiddenLayerCount;

        private int inputLayer_cellOccupierIndexNodesSize;
        private int inputLayer_cellGarrisonSizeNodesSize;
        private int inputLayer_playerTerritorySizeNodesSize;
        private int inputLayer_playerReserveSizeNodesSize;
        private int inputLayer_playerIsAliveNodesSize;

        private int outputLayer_operatorSkipNodesSize;
        private int outputLayer_operatorAttackNodesSize;


        private int maximumPlayerIndex;
        private int maximumGarrisonSize;
        private int maximumReserveSize;
        private int maximumCellCount;
        private int maximumAliveValue;


        public int determineInputLayerSize() {
            return inputLayer_cellOccupierIndexNodesSize +
                    inputLayer_cellGarrisonSizeNodesSize +
                    inputLayer_playerTerritorySizeNodesSize +
                    inputLayer_playerReserveSizeNodesSize +
                    inputLayer_playerIsAliveNodesSize;
        }

        public int determineOutputLayerSize() {
            return outputLayer_operatorSkipNodesSize +
                    outputLayer_operatorAttackNodesSize;
        }

        public int determineHiddenLayerSize() {
            return determineInputLayerSize();
        }

        private static void validation(LizNeuralNetworkConfiguration configuration) {

            // Input nodes - section length checking
            if (configuration.inputLayer_cellOccupierIndexNodesSize < 1) {
                throw new RuntimeException("Parameter inputLayer_cellOccupierIndexNodesSize must be greater than 0!");
            }
            if (configuration.inputLayer_cellGarrisonSizeNodesSize < 1) {
                throw new RuntimeException("Parameter inputLayer_cellGarrisonSize must be greater than 0!");
            }
            if (configuration.inputLayer_playerTerritorySizeNodesSize < 1) {
                throw new RuntimeException("Parameter inputLayer_playerTerritorySizeNodesSize must be greater than 0!");
            }
            if (configuration.inputLayer_playerReserveSizeNodesSize < 1) {
                throw new RuntimeException("Parameter inputLayer_playerReserveSizeNodesSize must be greater than 0!");
            }
            if (configuration.inputLayer_playerIsAliveNodesSize < 1) {
                throw new RuntimeException("Parameter inputLayer_playerIsAliveNodesSize must be greater than 0!");
            }

            // Output nodes - section length checking
            if (configuration.outputLayer_operatorSkipNodesSize != 1) {
                throw new RuntimeException("Parameter outputLayer_operatorSkipNodesSize must be equal to 1!");
            }
            if (configuration.outputLayer_operatorAttackNodesSize < 1) {
                throw new RuntimeException("Parameter outputLayer_operatorAttackNodes must be greater than 0!");
            }


            // Length equalness checking
            if (configuration.inputLayer_cellOccupierIndexNodesSize != configuration.inputLayer_cellGarrisonSizeNodesSize) {
                throw new RuntimeException("Parameter inputLayer_cellOccupierIndexNodesSize must be equal to parameter inputLayer_cellGarrisonSize!");
            }

            if (configuration.inputLayer_cellOccupierIndexNodesSize != configuration.outputLayer_operatorAttackNodesSize) {
                throw new RuntimeException("Parameter inputLayer_cellOccupierIndexNodesSize must be equal to parameter outputLayer_attackNodes!");
            }

            // Other
            if (configuration.hiddenLayerCount < 1) {
                throw new RuntimeException("Parameter hiddenLayerCount must be greater than 0!");
            }
        }

    }

}

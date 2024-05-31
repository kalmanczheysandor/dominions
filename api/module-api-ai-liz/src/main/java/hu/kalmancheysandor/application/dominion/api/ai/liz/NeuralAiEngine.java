package hu.kalmancheysandor.application.dominion.api.ai.liz;

import hu.kalmancheysandor.application.dominion.api.ai.common.AiEngine;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiRequest;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiResponse;
import hu.kalmancheysandor.application.dominion.api.ai.common.neural.INeuralAiEngine;
import hu.kalmancheysandor.application.dominion.api.ai.common.neural.NeuralTrainingData;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.NeuralNetworkType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class NeuralAiEngine extends AiEngine implements INeuralAiEngine {

    private final static String FILENAME_PREFIX = "LizAi";
    private final static String FILENAME_FORMAT = "nnet";

    private final static int ENEMIES_COUNT_MIN = 0;
    private final static int ENEMIES_COUNT_MAX = 10;

    private final static int OWNERS_COUNT_MIN = 0;
    private final static int OWNERS_COUNT_MAX = 10;

    private final static int DEFENDERS_SIZE_MIN = 0;
    private final static int DEFENDERS_SIZE_MAX = 100;

    private final static int RESERVE_SIZE_MIN = 0;
    private final static int RESERVE_SIZE_MAX = 100;


    private final static int TROOPS_SIZE_MIN = 0;
    private final static int TROOPS_SIZE_MAX = 100;


    @Override
    public void train(String playerCode, List<NeuralTrainingData> trainingDataList) {
        System.out.println("LIZ AI Start training...");
        System.out.println("Player code: " + playerCode);

        int inputSize = 2 + 42 + 42;
        int outputSize = 42 + 1;

        DataSet trainingSet = buildDataSet(trainingDataList, inputSize, outputSize);
        printDataSet(trainingSet);

        NeuralNetwork network = assembleNeuralNetwork(inputSize, outputSize);
        BackPropagation backPropagation = new BackPropagation();
        backPropagation.setMaxIterations(100);

        network.learn(trainingSet, backPropagation);

        String filename = generateFilePath(playerCode);
        network.save(filename);
        System.out.println("Neural network saved to '" + filename + "'");

        // Optional: Load the neural network
        //NeuralNetwork<?> loadedNeuralNet = NeuralNetwork.createFromFile(Filename);
    }


    @Override
    public void check(String playerCode, List<NeuralTrainingData> testingDataList) {
        System.out.println("LIZ AI Start checking...");
        System.out.println("Player code: " + playerCode);

        int inputSize = 2 + 42 + 42;
        int outputSize = 42 + 1;

        // Build data set
        DataSet testingSet = buildDataSet(testingDataList, inputSize, outputSize);
        printDataSet(testingSet);


        // Load file
        String filename = generateFilePath(playerCode);
        NeuralNetwork<?> network = NeuralNetwork.createFromFile(filename);
        System.out.println("Neural network load from:" + filename);

        // Test the trained neural network
        System.out.println("Testing trained neural network:");
        for (DataSetRow dataRow : testingSet.getRows()) {

            network.setInput(dataRow.getInput());
            network.calculate();

            double[] networkOutput = network.getOutput();
            double[] desiredOutput = dataRow.getDesiredOutput();

//            System.out.println("Input: " + arrayToString(dataRow.getInput()) +
//                " Desired Output: " + arrayToString(desiredOutput) +
//                " Network Output: " + arrayToString(networkOutput));

            System.out.println(
                "Desired Output: " + arrayToString(desiredOutput) + "\n" +
                    "Network Output: " + arrayToString(networkOutput) + "\n\n");

        }
    }


    protected double[] calculate(String playerCode,DataSetRow inputData) {
        System.out.println("\n <<<<<<  LIZ AI calculate: begin   >>>>>>");
        System.out.println("Player code: " + playerCode);

        int inputSize = 2 + 42 + 42;
        int outputSize = 42 + 1;

        // Load file
        String filename = generateFilePath(playerCode);
        NeuralNetwork<?> network = NeuralNetwork.createFromFile(filename);
        System.out.println("Neural network load from:" + filename);

        // Test the trained neural network
        System.out.println("Testing trained neural network:");

        network.setInput(inputData.getInput());
        network.calculate();

        // Denormalisation
        double[] denormalised = new double[network.getOutput().length];
        for(int i=0;i<network.getOutput().length-1-1;i++) {
            denormalised[i]=denormaliseValue(network.getOutput()[i],0,100);
        }
        denormalised[network.getOutput().length-1]=denormaliseValue(network.getOutput()[network.getOutput().length-1],TROOPS_SIZE_MIN,TROOPS_SIZE_MAX);
System.out.println("Denormalised:"+arrayToString(denormalised));
        return denormalised;
    }

    protected final static DataSet buildDataSet(List<NeuralTrainingData> trainingDataList, int inputSize, int outputSize) {
        DataSet trainingSet = new DataSet(inputSize, outputSize);
        for (NeuralTrainingData trainingData : trainingDataList) {
            trainingSet.addRow(buildDataSetRow(trainingData));
        }
        return trainingSet;
    }

    protected final static DataSetRow buildDataSetRow(NeuralTrainingData trainingData) {
        double[] inputNodeValuesArray = buildInputNodeValuesArray(trainingData);
        double[] outputNodeValuesArray = buildOutputNodeValuesArray(trainingData);
        return new DataSetRow(inputNodeValuesArray, outputNodeValuesArray);
    }

    protected final static double[] buildInputNodeValuesArray(NeuralTrainingData trainingData) {
        ArrayList<Double> inputNodeValuesList = new ArrayList<>();

        // Input nodes - Reserve size
        inputNodeValuesList.add(normaliseValue(trainingData.getReserveSize(), RESERVE_SIZE_MIN, RESERVE_SIZE_MAX));

        // Input nodes - Enemies count
        inputNodeValuesList.add(normaliseValue(trainingData.getEnemiesCount(), ENEMIES_COUNT_MIN, ENEMIES_COUNT_MAX));

        // Input nodes - Owners
        int cellCount = trainingData.getCellOwners().length;
        for (int ownerKey : trainingData.getCellOwners()) {
            int swappedKey = ownerKey + 1; // so -1 will be 0 and o will means empty.
            double normalisedKey = normaliseValue(swappedKey, OWNERS_COUNT_MIN, OWNERS_COUNT_MAX);
            inputNodeValuesList.add(normalisedKey);
        }

        // Input nodes - Defenders size
        for (int defenderSize : trainingData.getCellDefendersSize()) {
            inputNodeValuesList.add(normaliseValue(defenderSize, DEFENDERS_SIZE_MIN, DEFENDERS_SIZE_MAX));
        }

        return inputNodeValuesList.stream().mapToDouble(i -> i).toArray();
    }

    protected final static double[] buildOutputNodeValuesArray(NeuralTrainingData trainingData) {
        // Output nodes - Targeted cell percentage
        int cellCount = trainingData.getCellOwners().length;
        double[] outputNodeValuesArray = new double[cellCount + 1];
        if (trainingData.getChosenTarget() != -1) {        // When attack action happened and not reserve action
            outputNodeValuesArray[trainingData.getChosenTarget()] = normaliseValue(100, 0, 100);        // The potentiality id 100%, others 0%
        }

        // Output nodes - Troop size
        outputNodeValuesArray[outputNodeValuesArray.length - 1] = normaliseValue(trainingData.getChosenTroopSize(), TROOPS_SIZE_MIN, TROOPS_SIZE_MAX);

        return outputNodeValuesArray;
    }


//    private static DataSet normaliseDataSet(DataSet dataSet) {
//        for(DataSetRow row:dataSet.getRows()) {
//
//            normaliseValues(Arrays.copyOfRange(row.getInput(),2,row.getInput().length-1),;
//        }
//    }

    private static NeuralNetwork assembleNeuralNetwork(int inputSize, int outputSize) {

        Layer inputLayer = new Layer();
        for (int i = 1; i <= inputSize; i++) {
            inputLayer.addNeuron(new Neuron());
        }

        Layer hiddenLayerOne = new Layer();
        for (int i = 1; i <= inputSize; i++) {
            hiddenLayerOne.addNeuron(new Neuron());
        }

        Layer hiddenLayerTwo = new Layer();
        for (int i = 1; i <= inputSize; i++) {
            hiddenLayerTwo.addNeuron(new Neuron());
        }

        Layer outputLayer = new Layer();
        for (int i = 1; i <= outputSize; i++) {
            outputLayer.addNeuron(new Neuron());
        }

        NeuralNetwork network = new NeuralNetwork();

        network.addLayer(0, inputLayer);
        network.addLayer(1, hiddenLayerOne);
        ConnectionFactory.fullConnect(network.getLayerAt(0), network.getLayerAt(1));

        network.addLayer(2, hiddenLayerTwo);
        ConnectionFactory.fullConnect(network.getLayerAt(1), network.getLayerAt(2));

        network.addLayer(3, outputLayer);
        ConnectionFactory.fullConnect(network.getLayerAt(2), network.getLayerAt(3));
        ConnectionFactory.fullConnect(network.getLayerAt(0), network.getLayerAt(network.getLayersCount() - 1), false);

        network.setInputNeurons(inputLayer.getNeurons());
        network.setOutputNeurons(outputLayer.getNeurons());

        network.setNetworkType(NeuralNetworkType.MULTI_LAYER_PERCEPTRON);
        return network;
    }

    private static String generateFilePath(String playerCode) {
        return FILENAME_PREFIX + "_" + playerCode + "." + FILENAME_FORMAT;
    }

    private static void printDataSet(DataSet trainingSet) {
        for (DataSetRow row : trainingSet.getRows()) {
            printTrainingInput(row.getInput(), row.getDesiredOutput());
        }
    }

    private static void printTrainingInput(double[] inputNodeValuesArray, double[] outputNodeValuesArray) {
        StringBuilder sb = new StringBuilder();
        sb.append(Arrays.toString(inputNodeValuesArray));
        sb.append(" >>> ");
        sb.append(Arrays.toString(outputNodeValuesArray));
        System.out.println(sb.toString());
    }


    private static String arrayToString(double[] array) {
        StringBuilder sb = new StringBuilder();
        for (double value : array) {
            sb.append(String.format("%.2f|", value));
        }
        return sb.toString().trim();
    }

    public static double normaliseValue(double value, double intervalMin, double intervalMax) {
        return (value - intervalMin) / (intervalMax - intervalMin);
    }

    public static double[] normaliseArray(double[] array, double intervalMin, double intervalMax) {
        double[] normalisedArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            normalisedArray[i] = normaliseValue(array[i], intervalMin, intervalMax);
        }
        return normalisedArray;
    }


    public static double denormaliseValue(double normalisedValue, double intervalMin, double intervalMax) {
        return normalisedValue * (intervalMax - intervalMin) + intervalMin;
    }

    public static double[] denormaliseArray(double[] normalisedArray, double intervalMin, double max) {
        double[] denormalizedArray = new double[normalisedArray.length];
        for (int i = 0; i < normalisedArray.length; i++) {
            denormalizedArray[i] = denormaliseValue(normalisedArray[i], intervalMin, max);
        }
        return denormalizedArray;
    }


//
//    public void etc() {
//        // Step 1: Create and train the neural network
//        DataSet trainingSet = new DataSet(2, 1);
//        trainingSet.addRow(new DataSetRow(new double[]{0, 0}, new double[]{0}));
//        trainingSet.addRow(new DataSetRow(new double[]{0, 1}, new double[]{1}));
//        trainingSet.addRow(new DataSetRow(new double[]{1, 0}, new double[]{1}));
//        trainingSet.addRow(new DataSetRow(new double[]{1, 1}, new double[]{0}));
//
//        NeuralNetwork neuralNet = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 2, 2, 1);
//        BackPropagation backPropagation = new BackPropagation();
//        backPropagation.setMaxIterations(10000);
//
//        neuralNet.learn(trainingSet,backPropagation);
//
//        // Optional: Save the trained neural network
//        neuralNet.save("trainedNeuralNet.nnet");
//        System.out.println("Neural network saved to 'trainedNeuralNet.nnet'");
//
//        // Optional: Load the neural network
//        NeuralNetwork<?> loadedNeuralNet = NeuralNetwork.createFromFile("trainedNeuralNet.nnet");
//
//        // Step 2: Create a test dataset (same as training set in this example)
//        DataSet testSet = new DataSet(2, 1);
//        testSet.addRow(new DataSetRow(new double[]{0, 0}, new double[]{0}));
//        testSet.addRow(new DataSetRow(new double[]{0, 1}, new double[]{1}));
//        testSet.addRow(new DataSetRow(new double[]{1, 0}, new double[]{1}));
//        testSet.addRow(new DataSetRow(new double[]{1, 1}, new double[]{0}));
//
//        // Step 3: Test the trained neural network
//        System.out.println("Testing trained neural network:");
//        for (DataSetRow dataRow : testSet.getRows()) {
//            loadedNeuralNet.setInput(dataRow.getInput());
//            loadedNeuralNet.calculate();
//            double[] networkOutput = loadedNeuralNet.getOutput();
//            double[] desiredOutput = dataRow.getDesiredOutput();
//
//            System.out.println("Input: " + arrayToString(dataRow.getInput()) +
//                " Desired Output: " + arrayToString(desiredOutput) +
//                " Network Output: " + arrayToString(networkOutput));
//        }
//    }


    //
//    protected static Set<Integer> collectCellKeysOfCurrentAttackZone(Map<Integer, AiRequest.LandCell> landCells, Integer yourKey) {
//        boolean isCausingDoughnutEffect = isPlayerCausingDoughnutEffect(landCells, yourKey);
//        Set<Integer> attackZone = new HashSet<>();
//        Set<Integer> myLandKeys = collectAllYourLand(yourKey, landCells);
//        for (Integer landKey : myLandKeys) {
//            Set<Integer> neighbourKeys = collectNeighbourKeysOfALand(landKey, landCells);
//
//            for (Integer neighbourKey : neighbourKeys) {
//
//                AiRequest.LandCell neighbourCell = landCells.get(neighbourKey);
//
//                if (neighbourCell.isEmpty()) {
//                    attackZone.add(neighbourKey);
//                }
////                else if (neighbourCell.getPlayerKey() != yourKey && !isCausingDoughnutEffect) {
//                else if (neighbourCell.getPlayerKey() != yourKey && !isCausingDoughnutEffect) {
//                    attackZone.add(neighbourKey);
//                }
//            }
//        }
//        return attackZone;
//    }
//
//    protected static boolean isPlayerCausingDoughnutEffect(Map<Integer, AiRequest.LandCell> landCells, int playerKey) {
//        System.out.println("isPlayerCausingDoughnutEffect(player:"+playerKey+")");
//
//
//        for (Integer emptyCellKey : getEmptyCellKeys(landCells)) {
//            System.out.println("----EmptyKey:"+emptyCellKey);
//            if (isCellBlockedByPlayer(landCells, emptyCellKey, playerKey)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private static boolean isCellBlockedByPlayer(Map<Integer, AiRequest.LandCell> landCells, int observedCellKey, int playerKey) {
//        Set<Integer> neighbourKeys=landCells.get(observedCellKey).getNeighbours();
//
//        for (Integer neighbourKey : neighbourKeys) {
//            AiRequest.LandCell neighbourCell = landCells.get(neighbourKey);
//
//            if (neighbourCell.getPlayerKey() == null || neighbourCell.getPlayerKey() != playerKey) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//
//    private static Set<Integer> getEmptyCellKeys(Map<Integer, AiRequest.LandCell> landCells) {
//        Set<Integer> emptyCellKeys = new HashSet<>();
//        for (Map.Entry<Integer, AiRequest.LandCell> entry : landCells.entrySet()) {
//            AiRequest.LandCell cell = entry.getValue();
//            if (cell.isEmpty()) {
//                emptyCellKeys.add(entry.getKey());
//            }
//        }
//        return emptyCellKeys;
//    }
//
//
//    protected static Integer findEmptyLand(Map<Integer, AiRequest.LandCell> landCells, Integer yourKey) {
//        Set<Integer> myLandKeys = collectAllYourLand(yourKey, landCells);
//
//        System.out.println("findEmptyLand (yourKey:" + yourKey + ")");
//
//
//
//        Set<Integer> attackZoneKeys = collectCellKeysOfCurrentAttackZone(landCells, yourKey);
//
//        // Attempt to find an empty land to attack
//        for (Integer landKey : myLandKeys) {
//            Set<Integer> neighbourKeys = collectNeighbourKeysOfALand(landKey, landCells);
//            System.out.println("--LandKey::" + landKey + "");
//
//            for (Integer neighbourKey : neighbourKeys) {
//                AiRequest.LandCell neighbourCell = landCells.get(neighbourKey);
//                System.out.println("-- ---(" + landKey + ")neighbourKey::" + neighbourKey + " owner:" + neighbourCell.getPlayerKey());
//
//
//
//                if (attackZoneKeys.contains(neighbourKey) &&
//                        neighbourCell.isEmpty()
//                ) {
//                    System.out.println("return:" + neighbourKey + " playerKey:" + neighbourCell.getPlayerKey() + " yourKey" + yourKey);
//                    return neighbourKey;
//                }
//            }
//        }
//        return null;
//    }
//
//    protected static Integer findWeakerLand(Map<Integer, AiRequest.LandCell> landCells, Integer yourKey, Integer attackPower) {
//        Set<Integer> myLandKeys = collectAllYourLand(yourKey, landCells);
//
//        System.out.println("findWeakerLand (yourKey:" + yourKey + ")");
//
//        Set<Integer> attackZoneKeys = collectCellKeysOfCurrentAttackZone(landCells, yourKey);
//
//        // Attempt to find an empty land to attack
//        for (Integer landKey : myLandKeys) {
//            Set<Integer> neighbourKeys = collectNeighbourKeysOfALand(landKey, landCells);
//            System.out.println("--LandKey::" + landKey + "");
//            for (Integer neighbourKey : neighbourKeys) {
//                AiRequest.LandCell neighbourCell = landCells.get(neighbourKey);
//
//                System.out.println("-- ---(" + landKey + ")neighbourKey::" + neighbourKey + " owner:" + neighbourCell.getPlayerKey());
//
//                if (
//                    attackZoneKeys.contains(neighbourKey) &&
//                        neighbourCell.getPlayerKey() != yourKey &&
//                        !neighbourCell.isEmpty() &&
//                        isLandWeakerThan(neighbourKey, attackPower, landCells)
//                ) {
//                    System.out.println("return:" + neighbourKey + " playerKey:" + neighbourCell.getPlayerKey() + " yourKey" + yourKey);
//                    return neighbourKey;
//                }
//            }
//        }
//
//        return null;
//    }
//
//    protected static Integer findStrongLand(Map<Integer, AiRequest.LandCell> landCells, Integer yourKey, Integer attackPower) {
//        Set<Integer> myLandKeys = collectAllYourLand(yourKey, landCells);
//        System.out.println("findStrongLand (yourKey:" + yourKey + ")");
//
//        Set<Integer> attackZoneKeys = collectCellKeysOfCurrentAttackZone(landCells, yourKey);
//
//        // Attempt to find an empty land to attack
//        for (Integer landKey : myLandKeys) {
//            Set<Integer> neighbourKeys = collectNeighbourKeysOfALand(landKey, landCells);
//            System.out.println("--LandKey::" + landKey + "");
//
//            for (Integer neighbourKey : neighbourKeys) {
//                AiRequest.LandCell neighbourCell = landCells.get(neighbourKey);
//
//                System.out.println("-- ---(" + landKey + ")neighbourKey::" + neighbourKey + " owner:" + neighbourCell.getPlayerKey());
//
//
//                if (
//                    attackZoneKeys.contains(neighbourKey) &&
//                        neighbourCell.getPlayerKey() != yourKey &&
//                        !neighbourCell.isEmpty() &&
//                        isLandStrongerThan(neighbourKey, attackPower, landCells)) {
//                    System.out.println("return:" + neighbourKey + " playerKey:" + neighbourCell.getPlayerKey() + " yourKey" + yourKey);
//                    return neighbourKey;
//                }
//            }
//        }
//
//        return null;
//    }
//
//    protected static Set<Integer> collectAllYourLand(Integer yourKey, Map<Integer, AiRequest.LandCell> landCells) {
//        Set<Integer> output = new HashSet<>();
//
//        for (Map.Entry<Integer, AiRequest.LandCell> entry : landCells.entrySet()) {
//            AiRequest.LandCell cell = entry.getValue();
//
//            if (cell.getPlayerKey() == yourKey) {
//                output.add(entry.getKey());
//            }
//        }
//        return output;
//    }
//
//    protected static boolean isEmptyLand(Integer landKey, Map<Integer, AiRequest.LandCell> landCells) {
//        if (!landCells.containsKey(landKey)) {
//            throw new LandKeyNotExistAiException(landKey);
//        }
//        AiRequest.LandCell landCell = landCells.get(landKey);
//        if (landCell.getTroopSize() == 0) {
//            return true;
//        }
//        return false;
//    }
//
//    protected static boolean isLandWeakerThan(Integer landKey, Integer attackPower, Map<Integer, AiRequest.LandCell> landCells) {
//        if (!landCells.containsKey(landKey)) {
//            throw new LandKeyNotExistAiException(landKey);
//        }
//        AiRequest.LandCell landCell = landCells.get(landKey);
//        if (landCell.getTroopSize() < attackPower) {
//            return true;
//        }
//        return false;
//    }
//
//    protected static boolean isLandStrongerThan(Integer landKey, Integer attackPower, Map<Integer, AiRequest.LandCell> landCells) {
//        if (!landCells.containsKey(landKey)) {
//            throw new LandKeyNotExistAiException(landKey);
//        }
//        AiRequest.LandCell landCell = landCells.get(landKey);
//        if (landCell.getTroopSize() >= attackPower) {
//            return true;
//        }
//        return false;
//    }
//
//
//    protected static Set<Integer> collectNeighbourKeysOfALand(Integer landKey, Map<Integer, AiRequest.LandCell> landCells) {
//        if (!landCells.containsKey(landKey)) {
//            throw new LandKeyNotExistAiException(landKey);
//        }
//        AiRequest.LandCell landCell = landCells.get(landKey);
//        return landCell.getNeighbours();
//    }
}

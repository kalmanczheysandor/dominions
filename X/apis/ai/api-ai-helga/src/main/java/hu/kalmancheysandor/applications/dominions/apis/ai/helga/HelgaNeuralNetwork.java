package hu.kalmancheysandor.applications.dominions.apis.ai.helga;

import hu.kalmancheysandor.applications.dominions.apis.ai.neural.INeuralInputData;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.TNeuralNetwork;
import hu.kalmancheysandor.applications.dominions.apis.general.utils.TArray;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.NeuralNetworkType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HelgaNeuralNetwork extends TNeuralNetwork {
    private final static String FILENAME_PREFIX = "HelgaAi";
    private final static String FILENAME_FORMAT = "nnet";

    private final static int INPUT_VECTOR_ENEMY_LEGNTH = 1;
    private final static int INPUT_VECTOR_ATTACKPOWER_LEGNTH = 1;
    private final static int INPUT_VECTOR_CELLOWNER_LEGNTH = 42;
    private final static int INPUT_VECTOR_DEFENDERS_LEGNTH = 42;

    private final static int OUTPUT_VECTOR_CELLHITSCORE_LEGNTH = 42;


    private final static int INPUT_VECTOR_SIZE = INPUT_VECTOR_ATTACKPOWER_LEGNTH + INPUT_VECTOR_ENEMY_LEGNTH + INPUT_VECTOR_CELLOWNER_LEGNTH + INPUT_VECTOR_DEFENDERS_LEGNTH; // 1X attackPower + 1X enemiesCount + 42X cellOwnerKey + 42X cellDefendersSizeValue
    private final static int OUTPUT_VECTOR_SIZE = OUTPUT_VECTOR_CELLHITSCORE_LEGNTH + 1;// 42X CellHitScore (probability of attack) + 1X AttackingTroopSize

    private HelgaNeuralNetwork.Configuration configuration;

    public HelgaNeuralNetwork(@NotNull HelgaNeuralNetwork.Configuration configuration) {
        Configuration.validation(configuration);
        this.configuration = configuration;
    }

    private static String generateFilePath(String userUuid) {
        return FILENAME_PREFIX + "_" + userUuid + "." + FILENAME_FORMAT;
    }


    public void train(String userUuid, List<INeuralInputData> trainingDatas) {
        System.out.println("<<<< HelgaNeuralNetwork: Training >>>>");
        System.out.println("userUuid: " + userUuid);

        // Convert
        List<HelgaInputData> trainingDataList= HelgaInputData.convertTrainingDataList(trainingDatas);
            
        // Print training data list
        printTrainingDataList(trainingDataList);

        // Set up training set by training data
        DataSet trainingSet = buildDataSet(trainingDataList);
        printDataSet(trainingSet);

        // Set up configurations
        BackPropagation propagationConfiguration = new BackPropagation();
        propagationConfiguration.setMaxIterations(100);

        // Initialise neural network and train it
        NeuralNetwork network = assembleNeuralNetworkByData(INPUT_VECTOR_SIZE, OUTPUT_VECTOR_SIZE);
        network.learn(trainingSet, propagationConfiguration);

        // Save trained network into file
        String filename = generateFilePath(userUuid);
        network.save(filename);
        System.out.println("Neural network saved to '" + filename + "'");
    }


    public void test(String userUuid, List<INeuralInputData> testingDatas) {
        System.out.println("<<<< HelgaNeuralNetwork: Testing >>>>");
        System.out.println("userUuid: " + userUuid);
        
        // Convert
        List<HelgaInputData> testingDataList= HelgaInputData.convertTrainingDataList(testingDatas);
        
        // Instantiate neural network
        NeuralNetwork<?> networkObj = assembleNeuralNetworkByNetworkFile(userUuid);

        // Build data set
        DataSet testingSet = buildDataSet(testingDataList);
        printDataSet(testingSet);

        // Test the trained neural network
        System.out.println("Testing trained neural network:");
        for (DataSetRow dataRow : testingSet.getRows()) {

            networkObj.setInput(dataRow.getInput());
            networkObj.calculate();

            double[] networkOutput = networkObj.getOutput();
            double[] desiredOutput = dataRow.getDesiredOutput();

            System.out.println("Desired Output: " + convertArrayToString(desiredOutput));
            System.out.println("Network Output: " + convertArrayToString(networkOutput));
        }
    }

    public Set<Integer> calculate(String userUuid, DataSetRow inputData) {
        System.out.println("<<<< HelgaNeuralNetwork: Calculate >>>>");
        System.out.println("Player code: " + userUuid);

        // Instantiate neural network
        NeuralNetwork<?> networkObj = assembleNeuralNetworkByNetworkFile(userUuid);

        // Get result from the model
        networkObj.setInput(inputData.getInput());
        networkObj.calculate();
        double[] normalisedOutputVector = networkObj.getOutput();
        if (normalisedOutputVector.length != OUTPUT_VECTOR_SIZE) {
            throw new RuntimeException("Output size is wrong!");
        }

        // Denormalisation - Cell hit scores
        double[] cellHitScoresNormalised = new double[OUTPUT_VECTOR_CELLHITSCORE_LEGNTH];
        System.arraycopy(normalisedOutputVector, 0, cellHitScoresNormalised, 0, OUTPUT_VECTOR_CELLHITSCORE_LEGNTH);
        double[] cellHitScoresDenormalised = new double[OUTPUT_VECTOR_CELLHITSCORE_LEGNTH];
        for (int i = 0; i < cellHitScoresNormalised.length; i++) {
            cellHitScoresDenormalised[i] = denormaliseValue(cellHitScoresNormalised[i], 0, 100); // values denormalised between 0% and 100%
        }

        // Denormalisation - Attacking troops size
        double attackingTroopSizeNormalised = normalisedOutputVector[normalisedOutputVector.length - 1]; // Pointing to the last index
        double attackingTroopSizeDenormalised = denormaliseValue(attackingTroopSizeNormalised, configuration.getAttackPowerMin(), configuration.getAttackPowerMax());

//        // Generate output
//        double[] output = new double[OUTPUT_VECTOR_SIZE];
//        System.arraycopy(cellHitScoresDenormalised, 0, output, 0, cellHitScoresDenormalised.length);
//        output[output.length - 1] = attackingTroopSizeDenormalised;

        //System.out.println("Denormalised:" + convertArrayToString(output));


        //printOutputVectorInfo(output);

        Set<Integer> output= new HashSet<>();
        Integer cellKey = 0;
        for(double hitScore:cellHitScoresDenormalised) {
            if(hitScore>0.0) {
                output.add(cellKey);
            }
            cellKey++;
        }
        return output;
    }




//    public double[] calculate(String userUuid, DataSetRow inputData) {
//        System.out.println("<<<< HelgaNeuralNetwork: Calculate >>>>");
//        System.out.println("Player code: " + userUuid);
//
//        // Instantiate neural network
//        NeuralNetwork<?> networkObj = assembleNeuralNetworkByNetworkFile(userUuid);
//
//        // Get result from the model
//        networkObj.setInput(inputData.getInput());
//        networkObj.calculate();
//        double[] normalisedOutputVector = networkObj.getOutput();
//        if (normalisedOutputVector.length != OUTPUT_VECTOR_SIZE) {
//            throw new RuntimeException("Output size is wrong!");
//        }
//
//        // Denormalisation - Cell hit scores
//        double[] cellHitScoresNormalised = new double[OUTPUT_VECTOR_CELLHITSCORE_LEGNTH];
//        System.arraycopy(normalisedOutputVector, 0, cellHitScoresNormalised, 0, OUTPUT_VECTOR_CELLHITSCORE_LEGNTH);
//        double[] cellHitScoresDenormalised = new double[OUTPUT_VECTOR_CELLHITSCORE_LEGNTH];
//        for (int i = 0; i < cellHitScoresNormalised.length; i++) {
//            cellHitScoresDenormalised[i] = denormaliseValue(cellHitScoresNormalised[i], 0, 100); // values denormalised between 0% and 100%
//        }
//
//        // Denormalisation - Attacking troops size
//        double attackingTroopSizeNormalised = normalisedOutputVector[normalisedOutputVector.length - 1]; // Pointing to the last index
//        double attackingTroopSizeDenormalised = denormaliseValue(attackingTroopSizeNormalised, configuration.getAttackPowerMin(), configuration.getAttackPowerMax());
//
//        // Generate output
//        double[] output = new double[OUTPUT_VECTOR_SIZE];
//        System.arraycopy(cellHitScoresDenormalised, 0, output, 0, cellHitScoresDenormalised.length);
//        output[output.length - 1] = attackingTroopSizeDenormalised;
//
//        //System.out.println("Denormalised:" + convertArrayToString(output));
//
//
//        printOutputVectorInfo(output);
//
//        return output;
//    }

    private static void printOutputVectorInfo(double[] outputVector) {
        String[] s1 = new String[OUTPUT_VECTOR_SIZE];
        String[] s2 = new String[OUTPUT_VECTOR_SIZE];
        String[] s3 = new String[OUTPUT_VECTOR_SIZE];

        int columnIndex = 0;
        for (double value : outputVector) {
            if (columnIndex + 1 <= OUTPUT_VECTOR_CELLHITSCORE_LEGNTH) {
                s1[columnIndex] = "C" + columnIndex;
                s2[columnIndex] = "";
                if(value>0.0) {
                    s2[columnIndex] = "X";
                }

                s3[columnIndex] = value + "%";
            } else if (columnIndex + 1 <= OUTPUT_VECTOR_CELLHITSCORE_LEGNTH + 1) {
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

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// [ Network assembling  ] //////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static NeuralNetwork<?> assembleNeuralNetworkByData(int inputSize, int outputSize) {

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

    private NeuralNetwork<?> assembleNeuralNetworkByNetworkFile(String userUuid) {
        // Load file
        String filename = generateFilePath(userUuid);
        File fileObj = new File(filename);
        if (!fileObj.exists()) {
            System.out.println("Neural network file does not exist:" + filename);
            return null;
        }

        System.out.println("Neural network load from:" + filename);
        return NeuralNetwork.createFromFile(filename);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// [ Build methods  ] //////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    private DataSet buildDataSet(List<HelgaInputData> trainingDataList) {
        DataSet trainingSet = new DataSet(INPUT_VECTOR_SIZE, OUTPUT_VECTOR_SIZE);
        for (HelgaInputData trainingData : trainingDataList) {
            trainingSet.addRow(
                new DataSetRow(
                    buildInputVector(trainingData),
                    buildOutputVector(trainingData)
                )
            );
        }
        return trainingSet;
    }

    public double[] buildInputVector(HelgaInputData trainingData) {

        // Input checking - attackPower
        if (trainingData.getAttackPower() < configuration.getAttackPowerMin()) {
            throw new RuntimeException("Value of attackPower('" + trainingData.getAttackPower() + "') must not be under than interval('" + configuration.getAttackPowerMin() + "') minimum!");
        }
        if (trainingData.getAttackPower() > configuration.getAttackPowerMax()) {
            throw new RuntimeException("Value of maxAttackPower('" + trainingData.getAttackPower() + "')must not be above than interval('" + configuration.getAttackPowerMax() + "') maximum!");
        }

        // Input checking - Enemies count
        if (trainingData.getEnemiesCount() < configuration.getEnemiesCountMin()) {
            throw new RuntimeException("Value of enemiesCount('" + trainingData.getEnemiesCount() + "') must not be under than interval('" + configuration.getEnemiesCountMin() + "') minimum!");
        }
        if (trainingData.getEnemiesCount() > configuration.getEnemiesCountMax()) {
            throw new RuntimeException("Value of enemiesCount('" + trainingData.getEnemiesCount() + "')must not be above than interval('" + configuration.getEnemiesCountMax() + "') maximum!");
        }

        // Input checking - cell owners
        if (trainingData.getCellOwners().length != INPUT_VECTOR_CELLOWNER_LEGNTH) {
            throw new RuntimeException("Invalid length of cell owners!");
        }
        if (trainingData.getCellDefendersSize().length != INPUT_VECTOR_DEFENDERS_LEGNTH) {
            throw new RuntimeException("Invalid length of cell defenders!");
        }

        // Initialisation
        ArrayList<Double> inputNodeValuesList = new ArrayList<>();

        // Input nodes - Reserve size
        inputNodeValuesList.add(normaliseValue(trainingData.getAttackPower(), configuration.getAttackPowerMin(), configuration.getAttackPowerMax()));

        // Input nodes - Enemies count
        inputNodeValuesList.add(normaliseValue(trainingData.getEnemiesCount(), configuration.getEnemiesCountMin(), configuration.getEnemiesCountMax()));

        // Input nodes - Cell owners
        for (int ownerKey : trainingData.getCellOwners()) {
            int swappedKey = ownerKey + 1; // so -1 will be 0 and will mean empty. (Originally, in game state representation, -1 was referred to empty cell)
            double normalisedKey = normaliseValue(swappedKey, configuration.getOwnersCountMin(), configuration.getOwnersCountMax());
            inputNodeValuesList.add(normalisedKey);
        }

        // Input nodes - Defending troop size
        for (int defenderSize : trainingData.getCellDefendersSize()) {
            inputNodeValuesList.add(normaliseValue(defenderSize, configuration.getDefendersSizeMin(), configuration.getDefendersSizeMax()));
        }

        return inputNodeValuesList.stream().mapToDouble(i -> i).toArray();
    }

    private double[] buildOutputVector(HelgaInputData trainingData) {
        // Input checking - Cell owners
        if (trainingData.getCellOwners().length != INPUT_VECTOR_CELLOWNER_LEGNTH) {
            throw new RuntimeException("Invalid length of cell owners!");
        }
        if (trainingData.getCellDefendersSize().length != INPUT_VECTOR_DEFENDERS_LEGNTH) {
            throw new RuntimeException("Invalid length of cell defenders!");
        }

        // Initialise
        int cellCount = trainingData.getCellOwners().length;

        // Output nodes - Targeted cell percentage
        double[] outputNodeValuesArray = new double[cellCount + 1];     // By default, all values are 0 (because array default values) which means 0% probability
        if (trainingData.getChosenTarget() != -1) {        // When attack action happened and not reserve action
            outputNodeValuesArray[trainingData.getChosenTarget()] = normaliseValue(100, 0, 100);        // The potentiality id 100%, others 0%
        }

        // Output nodes - attacking troop size
        outputNodeValuesArray[outputNodeValuesArray.length - 1] = normaliseValue(trainingData.getChosenTroopSize(), configuration.getAttackPowerMin(), configuration.getAttackPowerMax());

        return outputNodeValuesArray;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// [ Print methods  ] //////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static void printTrainingDataList(List<HelgaInputData> trainingDataList) {
        for (HelgaInputData trainingData : trainingDataList) {
            System.out.println(trainingData);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// [ INNER CLASSES ] ////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static class Configuration {

        private int enemiesCountMin;
        private int enemiesCountMax;

        private int ownersCountMin;
        private int ownersCountMax;

        private int defendersSizeMin;
        private int defendersSizeMax;

        private int attackPowerMin;
        private int attackPowerMax;


        public Configuration() {

        }

        private static void validation(Configuration configuration) {
            // Validations
            if (configuration.enemiesCountMax <= configuration.enemiesCountMin) {
                throw new RuntimeException("Parameter ...max must be grater then parameter ...min!");
            }

            if (configuration.ownersCountMax <= configuration.ownersCountMin) {
                throw new RuntimeException("Parameter ...max must be grater then parameter ...min!");
            }

            if (configuration.defendersSizeMax <= configuration.defendersSizeMin) {
                throw new RuntimeException("Parameter ...max must be grater then parameter ...min!");
            }

            if (configuration.attackPowerMax <= configuration.attackPowerMin) {
                throw new RuntimeException("Parameter ...max must be grater then parameter ...min!");
            }


        }

        public int getEnemiesCountMin() {
            return enemiesCountMin;
        }

        public int getEnemiesCountMax() {
            return enemiesCountMax;
        }

        public int getOwnersCountMin() {
            return ownersCountMin;
        }

        public int getOwnersCountMax() {
            return ownersCountMax;
        }

        public int getDefendersSizeMin() {
            return defendersSizeMin;
        }

        public int getDefendersSizeMax() {
            return defendersSizeMax;
        }

        public int getAttackPowerMin() {
            return attackPowerMin;
        }

        public int getAttackPowerMax() {
            return attackPowerMax;
        }


        public Configuration setEnemiesCountMin(@PositiveOrZero int enemiesCountMin) {
            this.enemiesCountMin = enemiesCountMin;
            return this;
        }

        public Configuration setEnemiesCountMax(@Positive int enemiesCountMax) {
            this.enemiesCountMax = enemiesCountMax;
            return this;
        }

        public Configuration setOwnersCountMin(@PositiveOrZero int ownersCountMin) {
            this.ownersCountMin = ownersCountMin;
            return this;
        }

        public Configuration setOwnersCountMax(@Positive int ownersCountMax) {
            this.ownersCountMax = ownersCountMax;
            return this;
        }

        public Configuration setDefendersSizeMin(@PositiveOrZero int defendersSizeMin) {
            this.defendersSizeMin = defendersSizeMin;
            return this;
        }

        public Configuration setDefendersSizeMax(@Positive int defendersSizeMax) {
            this.defendersSizeMax = defendersSizeMax;
            return this;
        }

        public Configuration setAttackPowerMin(@PositiveOrZero int attackPowerMin) {
            this.attackPowerMin = attackPowerMin;
            return this;
        }

        public Configuration setAttackPowerMax(@Positive int attackPowerMax) {
            this.attackPowerMax = attackPowerMax;
            return this;
        }


        public void validate() {
            Configuration.validation(this);
        }

    }
}

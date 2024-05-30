package hu.kalmancheysandor.application.dominion.api.ai.liz;

import hu.kalmancheysandor.application.dominion.api.ai.common.AiRequest;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiResponse;
import hu.kalmancheysandor.application.dominion.api.ai.common.neural.INeuralAiEngine;
import hu.kalmancheysandor.application.dominion.api.ai.common.neural.NeuralTrainingData;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;

import java.util.List;

public abstract class NeuralAiEngine implements INeuralAiEngine {

    private final static String FILENAME = "LizAi.nnet";

    @Override
    public AiResponse generateResponse(AiRequest request) {
        return new AiResponse(0, 0);
    }

    @Override
    public void train(List<NeuralTrainingData> trainingDataList) {
        // Step 1: Create and train the neural network
        DataSet trainingSet = new DataSet(2, 1);
        trainingSet.addRow(new DataSetRow(new double[]{0, 0}, new double[]{0}));
        trainingSet.addRow(new DataSetRow(new double[]{0, 1}, new double[]{1}));
        trainingSet.addRow(new DataSetRow(new double[]{1, 0}, new double[]{1}));
        trainingSet.addRow(new DataSetRow(new double[]{1, 1}, new double[]{0}));

        NeuralNetwork neuralNet = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 2, 2, 1);
        BackPropagation backPropagation = new BackPropagation();
        backPropagation.setMaxIterations(10000);

        neuralNet.learn(trainingSet, backPropagation);

        // Optional: Save the trained neural network
        neuralNet.save(FILENAME);
        System.out.println("Neural network saved to '" + FILENAME + "'");

        // Optional: Load the neural network
        NeuralNetwork<?> loadedNeuralNet = NeuralNetwork.createFromFile(FILENAME);

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

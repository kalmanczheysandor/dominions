package hu.kalmancheysandor.applications.dominions.apis.ai.liz;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.Neuron;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.nnet.learning.LMS;

public class NetworkTrainer {
//
//    public static NeuralNetwork<?> trainWithCheckpoints(NeuralNetwork<?> network, DataSet trainingSet, DataSet validationSet, int totalIterations, int iterationCountInEachBlock) {
//        NeuralNetwork<?> bestNetwork = network;
//        double bestAccuracy = 0;
//
//        int blocksCount = totalIterations / iterationCountInEachBlock;
//
//        for (int currentBlockIndex = 0; currentBlockIndex < blocksCount; currentBlockIndex++) {
//            System.out.println("Starting block " + (currentBlockIndex + 1) + "/" + blocksCount);
//
//            // Tanítás a blokknyi iterációra
//            network.learn(trainingSet, iterationCountInEachBlock);
//
//            // Tesztelés a validációs adatokon
//            double accuracy = evaluateNetwork(network, validationSet);
//            System.out.println("Block " + (currentBlockIndex + 1) + " accuracy: " + accuracy);
//
//            // Ha ez a háló a legjobb eddig, elmentjük
//            if (accuracy > bestAccuracy) {
//                bestAccuracy = accuracy;
//                bestNetwork = network.clone(); // klónozás, hogy ne írjuk felül később
//                System.out.println("New best network saved with accuracy: " + bestAccuracy);
//            }
//        }
//
//        System.out.println("Training finished. Best accuracy: " + bestAccuracy);
//        return bestNetwork;
//    }
//
//    private static double evaluateNetwork(NeuralNetwork<?> network, DataSet validationSet) {
//        int correct = 0;
//        for (DataSetRow row : validationSet.getRows()) {
//            network.setInput(row.getInput());
//            network.calculate();
//            double[] output = network.getOutput();
//
//            // Példa: bináris kimenet, küszöb 0.5
//            boolean rowCorrect = true;
//            for (int i = 0; i < output.length; i++) {
//                int predicted = output[i] > 0.5 ? 1 : 0;
//                if (predicted != (int) row.getDesiredOutput()[i]) {
//                    rowCorrect = false;
//                    break;
//                }
//            }
//            if (rowCorrect) correct++;
//        }
//        return (double) correct / validationSet.size();
//    }
}

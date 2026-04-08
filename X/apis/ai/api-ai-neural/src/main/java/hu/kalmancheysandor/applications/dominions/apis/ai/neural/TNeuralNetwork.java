package hu.kalmancheysandor.applications.dominions.apis.ai.neural;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

import java.util.Arrays;

public abstract class TNeuralNetwork implements INeuralNetwork {

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Normalisation methods ////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    protected static double normaliseValue(double value, double intervalMin, double intervalMax) {
        // Input checking
        if(intervalMin>=intervalMax) {
            throw new RuntimeException("Interval max and min are overlapping each other!");
        }

        if(value<intervalMin) {
            throw new RuntimeException("Value('"+value+"') must not be under than interval('"+intervalMin+"') minimum!");
        }
        if(value>intervalMax) {
            throw new RuntimeException("Value('"+value+"') must not be above than interval('"+intervalMax+"') maximum!");
        }

        // Execution
        return (value - intervalMin) / (intervalMax - intervalMin);
    }

    protected static double[] normaliseArray(double[] array, double intervalMin, double intervalMax) {
        // Input checking
        if(intervalMin>=intervalMax) {
            throw new RuntimeException("Interval max and min are overlapping each other!");
        }

        // Execution
        double[] normalisedArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            normalisedArray[i] = normaliseValue(array[i], intervalMin, intervalMax);
        }
        return normalisedArray;
    }

    protected static double denormaliseValue(double normalisedValue, double intervalMin, double intervalMax) {
        // Input checking
        if(intervalMin>=intervalMax) {
            throw new RuntimeException("Interval max and min are overlapping each other!");
        }

        // Execution
        return normalisedValue * (intervalMax - intervalMin) + intervalMin;
    }

    protected static double[] denormaliseArray(double[] normalisedArray, double intervalMin, double intervalMax) {
        // Input checking
        if(intervalMin>=intervalMax) {
            throw new RuntimeException("Interval max and min are overlapping each other!");
        }

        // Execution
        double[] denormalizedArray = new double[normalisedArray.length];
        for (int i = 0; i < normalisedArray.length; i++) {
            denormalizedArray[i] = denormaliseValue(normalisedArray[i], intervalMin, intervalMax);
        }
        return denormalizedArray;
    }



    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Print methods //////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected static void printDataSet(DataSet trainingSet) {
        for (DataSetRow row : trainingSet.getRows()) {
            printTrainingInput(row.getInput(), row.getDesiredOutput());
        }
    }

    protected static void printTrainingInput(double[] inputNodeValuesArray, double[] outputNodeValuesArray) {
        StringBuilder sb = new StringBuilder();
        sb.append(Arrays.toString(inputNodeValuesArray));
        sb.append(" >>> ");
        sb.append(Arrays.toString(outputNodeValuesArray));
        System.out.println(sb.toString());
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Convert methods //////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    protected static String convertArrayToString(double[] array) {
        StringBuilder sb = new StringBuilder();
        for (double value : array) {
            sb.append(String.format("%.2f|", value));
        }
        return sb.toString().trim();
    }





}

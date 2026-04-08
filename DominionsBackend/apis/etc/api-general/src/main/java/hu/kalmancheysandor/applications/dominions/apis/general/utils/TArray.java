package hu.kalmancheysandor.applications.dominions.apis.general.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TArray {


    public static int[] convertIntegerArrayList(ArrayList<Integer> arrayList) {
        int[] intArray = new int[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            intArray[i] = arrayList.get(i);
        }
        return intArray;
    }


    public static <T> void printMatrix(T[][] matrix) {
        Map<Integer, Integer> x = new HashMap<>();

        for (T[] row : matrix) {
            int columnIndex = 0;
            for (T value : row) {
                Integer characterCount = String.valueOf(value).length();

                if (!x.containsKey(columnIndex)) {
                    x.put(columnIndex, characterCount);
                } else {
                    if (x.get(columnIndex) < characterCount) { // overrides previous when current is greater
                        x.put(columnIndex, characterCount);
                    }
                }
                columnIndex++;
            }
        }

        for (T[] row : matrix) {
            int columnIndex = 0;
            Integer columnLength;
            for (T value : row) {
                columnLength = x.get(columnIndex);
                System.out.printf("%-" + (columnLength + 0) + "s | ", value);
                columnIndex++;
            }
            System.out.println();
        }
    }

}

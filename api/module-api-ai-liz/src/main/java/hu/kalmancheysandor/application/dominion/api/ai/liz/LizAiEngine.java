package hu.kalmancheysandor.application.dominion.api.ai.liz;

import hu.kalmancheysandor.application.dominion.api.ai.common.AiRequest;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiResponse;
import hu.kalmancheysandor.application.dominion.api.ai.common.neural.NeuralTrainingData;
import org.neuroph.core.data.DataSetRow;

import java.util.*;

public class LizAiEngine extends NeuralAiEngine {
    @Override
    public AiResponse generateResponse(AiRequest request) {
        Integer yourKey = request.getYourKey();
        Integer reserveSize = request.getReserveSize();
        Integer attackPower = reserveSize;

        Map<Integer, AiRequest.LandCell> landCells = request.getLandCells();
        Set<Integer> myLandKeys = collectAllYourLand(yourKey, landCells);

        Set<Integer> conflictZoneKeys = determineConflictZoneKeys(reserveSize, landCells);

        System.out.println("\nConflict zone Keys:"+conflictZoneKeys+"\n");
        Integer targetLandKey = null;
        if ((targetLandKey = findEmptyLand(landCells, yourKey, conflictZoneKeys)) != null) {
            return new AiResponse(targetLandKey, attackPower);
        }

        if ((targetLandKey = findWeakerLand(landCells, yourKey, attackPower, conflictZoneKeys)) != null) {
            return new AiResponse(targetLandKey, attackPower);
        }

        if ((targetLandKey = findStrongLand(landCells, yourKey, attackPower, conflictZoneKeys)) != null) {
            return new AiResponse(targetLandKey, attackPower);
        }

        return new AiResponse(0, 0);
    }

    private Set<Integer> determineConflictZoneKeys(int reserveSize, Map<Integer, AiRequest.LandCell> landCells) {
        int alivePlayerCount = alivePlayerCount(landCells);


        Set<Integer> conflictZoneKeys = new HashSet<>();
        List<String> playerCodes = List.of("HUMAN_KALMANCZHEYSANDOR@GAMIL-COM", "ARTIFICIAL_OTTO");


        DataSetRow circumstances = converterToDataSetRow(reserveSize, alivePlayerCount, landCells);

        double[] calculation;
        for (String playerCode : playerCodes) {
            calculation = calculate(playerCode, circumstances);
            for (int cellIndex = 0; cellIndex < calculation.length - 1 - 1; cellIndex++) {
                if(calculation[cellIndex]>0) {
                    conflictZoneKeys.add(cellIndex);
                }
            }
        }
        return conflictZoneKeys;
    }

    private DataSetRow converterToDataSetRow(int reserveSize, int playersCount, Map<Integer, AiRequest.LandCell> landCells) {

        int[] cellOwnersList = new int[landCells.size()];
        int[] cellDefenderSizeList = new int[landCells.size()];

        for (Map.Entry<Integer, AiRequest.LandCell> landCellEntry : landCells.entrySet()) {
            int cellIndex = landCellEntry.getKey();
            Integer playerKey = landCellEntry.getValue().getPlayerKey();
            if (playerKey == null) {
                playerKey = -1;
            }
            cellOwnersList[cellIndex] = playerKey;
            cellDefenderSizeList[cellIndex] = landCellEntry.getValue().getTroopSize();
        }

        NeuralTrainingData trainingData = new NeuralTrainingData();
        trainingData.setReserveSize(reserveSize);
        trainingData.setEnemiesCount(playersCount - 1);
        trainingData.setCellOwners(cellOwnersList);
        trainingData.setCellDefendersSize(cellDefenderSizeList);

        return new DataSetRow(buildInputNodeValuesArray(trainingData));
    }

    private static Set<Integer> alivePlayerKeys(Map<Integer, AiRequest.LandCell> landCells) {

        Set<Integer> alivePlayerKeys = new HashSet<>();

        for (Map.Entry<Integer, AiRequest.LandCell> landCellEntry : landCells.entrySet()) {
            Integer playerKey = landCellEntry.getValue().getPlayerKey();
            if (playerKey != null && playerKey >= 0) {
                alivePlayerKeys.add(playerKey);
            }
        }
        return alivePlayerKeys;
    }

    private static int alivePlayerCount(Map<Integer, AiRequest.LandCell> landCells) {
        return alivePlayerKeys(landCells).size();
    }


    protected static Integer findEmptyLand(Map<Integer, AiRequest.LandCell> landCells, Integer yourKey, Set<Integer> conflictZoneKeys) {
        Set<Integer> myLandKeys = collectAllYourLand(yourKey, landCells);

        System.out.println("findEmptyLand (yourKey:" + yourKey + ")");

        Set<Integer> attackZoneKeys = collectCellKeysOfCurrentAttackZone(landCells, yourKey);


        // Attempt to find an empty land to attack
        for (Integer landKey : myLandKeys) {
            Set<Integer> neighbourKeys = collectNeighbourKeysOfALand(landKey, landCells);
            System.out.println("--LandKey::" + landKey + "");

            for (Integer neighbourKey : neighbourKeys) {
                AiRequest.LandCell neighbourCell = landCells.get(neighbourKey);
                System.out.println("-- ---(" + landKey + ")neighbourKey::" + neighbourKey + " owner:" + neighbourCell.getPlayerKey());


                if (
                    attackZoneKeys.contains(neighbourKey) &&
                        !conflictZoneKeys.contains(neighbourKey) &&
                        neighbourCell.isEmpty()
                ) {
                    System.out.println("return:" + neighbourKey + " playerKey:" + neighbourCell.getPlayerKey() + " yourKey" + yourKey);
                    return neighbourKey;
                }
            }
        }
        return null;
    }

    protected static Integer findWeakerLand(Map<Integer, AiRequest.LandCell> landCells, Integer yourKey, Integer attackPower, Set<Integer> conflictZoneKeys) {
        Set<Integer> myLandKeys = collectAllYourLand(yourKey, landCells);

        System.out.println("findWeakerLand (yourKey:" + yourKey + ")");

        Set<Integer> attackZoneKeys = collectCellKeysOfCurrentAttackZone(landCells, yourKey);

        // Attempt to find an empty land to attack
        for (Integer landKey : myLandKeys) {
            Set<Integer> neighbourKeys = collectNeighbourKeysOfALand(landKey, landCells);
            System.out.println("--LandKey::" + landKey + "");
            for (Integer neighbourKey : neighbourKeys) {
                AiRequest.LandCell neighbourCell = landCells.get(neighbourKey);

                System.out.println("-- ---(" + landKey + ")neighbourKey::" + neighbourKey + " owner:" + neighbourCell.getPlayerKey());

                if (
                    attackZoneKeys.contains(neighbourKey) &&
                        !conflictZoneKeys.contains(neighbourKey) &&
                        neighbourCell.getPlayerKey() != yourKey &&
                        !neighbourCell.isEmpty() &&
                        isLandWeakerThan(neighbourKey, attackPower, landCells)
                ) {
                    System.out.println("return:" + neighbourKey + " playerKey:" + neighbourCell.getPlayerKey() + " yourKey" + yourKey);
                    return neighbourKey;
                }
            }
        }

        return null;
    }

    protected static Integer findStrongLand(Map<Integer, AiRequest.LandCell> landCells, Integer yourKey, Integer attackPower, Set<Integer> conflictZoneKeys) {
        Set<Integer> myLandKeys = collectAllYourLand(yourKey, landCells);
        System.out.println("findStrongLand (yourKey:" + yourKey + ")");

        Set<Integer> attackZoneKeys = collectCellKeysOfCurrentAttackZone(landCells, yourKey);

        // Attempt to find an empty land to attack
        for (Integer landKey : myLandKeys) {
            Set<Integer> neighbourKeys = collectNeighbourKeysOfALand(landKey, landCells);
            System.out.println("--LandKey::" + landKey + "");

            for (Integer neighbourKey : neighbourKeys) {
                AiRequest.LandCell neighbourCell = landCells.get(neighbourKey);

                System.out.println("-- ---(" + landKey + ")neighbourKey::" + neighbourKey + " owner:" + neighbourCell.getPlayerKey());


                if (
                    attackZoneKeys.contains(neighbourKey) &&
                        !conflictZoneKeys.contains(neighbourKey) &&
                        neighbourCell.getPlayerKey() != yourKey &&
                        !neighbourCell.isEmpty() &&
                        isLandStrongerThan(neighbourKey, attackPower, landCells)) {
                    System.out.println("return:" + neighbourKey + " playerKey:" + neighbourCell.getPlayerKey() + " yourKey" + yourKey);
                    return neighbourKey;
                }
            }
        }

        return null;
    }


}

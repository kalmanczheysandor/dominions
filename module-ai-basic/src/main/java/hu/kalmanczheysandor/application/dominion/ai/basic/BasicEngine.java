package hu.kalmanczheysandor.application.dominion.ai.basic;

import hu.kalmanczheysandor.application.dominion.ai.common.AiEngine;
import hu.kalmanczheysandor.application.dominion.ai.common.AiRequest;
import hu.kalmanczheysandor.application.dominion.ai.common.exception.LandKeyNotExistAiException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class BasicEngine extends AiEngine {


    protected static Set<Integer> collectCellKeysOfCurrentAttackZone(Map<Integer, AiRequest.LandCell> landCells, Integer yourKey) {
        boolean isCausingDoughnutEffect = isPlayerCausingDoughnutEffect(landCells, yourKey);
        Set<Integer> attackZone = new HashSet<>();
        Set<Integer> myLandKeys = collectAllYourLand(yourKey, landCells);
        for (Integer landKey : myLandKeys) {
            Set<Integer> neighbourKeys = collectNeighbourKeysOfALand(landKey, landCells);

            for (Integer neighbourKey : neighbourKeys) {

                AiRequest.LandCell neighbourCell = landCells.get(neighbourKey);

                if (neighbourCell.isEmpty()) {
                    attackZone.add(neighbourKey);
                }
                else if (neighbourCell.getPlayerKey() != yourKey && !isCausingDoughnutEffect) {
                    attackZone.add(neighbourKey);
                }
            }
        }
        return attackZone;
    }

    private static boolean isPlayerCausingDoughnutEffect(Map<Integer, AiRequest.LandCell> landCells, int playerKey) {
        for (Integer emptyCellKey : getEmptyCellKeys(landCells)) {
            if (isCellBlockedByPlayer(landCells, emptyCellKey, playerKey)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isCellBlockedByPlayer(Map<Integer, AiRequest.LandCell> landCells, int cellKey, int playerKey) {
        for (Integer neighbourKey : landCells.get(cellKey).getNeighbours()) {
            AiRequest.LandCell neighbourCell = landCells.get(neighbourKey);

            if (neighbourCell.getPlayerKey() == null || neighbourCell.getPlayerKey() != playerKey) {
                return false;
            }
        }
        return true;
    }


    private static Set<Integer> getEmptyCellKeys(Map<Integer, AiRequest.LandCell> landCells) {
        Set<Integer> emptyCellKeys = new HashSet<>();
        for (Map.Entry<Integer, AiRequest.LandCell> entry : landCells.entrySet()) {
            AiRequest.LandCell cell = entry.getValue();
            if (cell.isEmpty()) {
                emptyCellKeys.add(entry.getKey());
            }
        }
        return emptyCellKeys;
    }


    protected static Integer findEmptyLand(Map<Integer, AiRequest.LandCell> landCells, Integer yourKey) {
        Set<Integer> myLandKeys = collectAllYourLand(yourKey, landCells);

        Set<Integer> attackZoneKeys = collectCellKeysOfCurrentAttackZone(landCells, yourKey);

        // Attempt to find an empty land to attack
        for (Integer landKey : myLandKeys) {
            Set<Integer> neighbourKeys = collectNeighbourKeysOfALand(landKey, landCells);

            for (Integer neighbourKey : neighbourKeys) {
                if (attackZoneKeys.contains(neighbourKey) &&
                        isEmptyLand(neighbourKey, landCells)) {
                    return neighbourKey;
                }
            }
        }
        return null;
    }

    protected static Integer findWeakerLand(Map<Integer, AiRequest.LandCell> landCells, Integer yourKey, Integer attackPower) {
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

    protected static Integer findStrongLand(Map<Integer, AiRequest.LandCell> landCells, Integer yourKey, Integer attackPower) {
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

    protected static Set<Integer> collectAllYourLand(Integer yourKey, Map<Integer, AiRequest.LandCell> landCells) {
        Set<Integer> output = new HashSet<>();

        for (Map.Entry<Integer, AiRequest.LandCell> entry : landCells.entrySet()) {
            AiRequest.LandCell cell = entry.getValue();

            if (cell.getPlayerKey() == yourKey) {
                output.add(entry.getKey());
            }
        }
        return output;
    }

    protected static boolean isEmptyLand(Integer landKey, Map<Integer, AiRequest.LandCell> landCells) {
        if (!landCells.containsKey(landKey)) {
            throw new LandKeyNotExistAiException(landKey);
        }
        AiRequest.LandCell landCell = landCells.get(landKey);
        if (landCell.getTroopSize() == 0) {
            return true;
        }
        return false;
    }

    protected static boolean isLandWeakerThan(Integer landKey, Integer attackPower, Map<Integer, AiRequest.LandCell> landCells) {
        if (!landCells.containsKey(landKey)) {
            throw new LandKeyNotExistAiException(landKey);
        }
        AiRequest.LandCell landCell = landCells.get(landKey);
        if (landCell.getTroopSize() < attackPower) {
            return true;
        }
        return false;
    }

    protected static boolean isLandStrongerThan(Integer landKey, Integer attackPower, Map<Integer, AiRequest.LandCell> landCells) {
        if (!landCells.containsKey(landKey)) {
            throw new LandKeyNotExistAiException(landKey);
        }
        AiRequest.LandCell landCell = landCells.get(landKey);
        if (landCell.getTroopSize() >= attackPower) {
            return true;
        }
        return false;
    }


    protected static Set<Integer> collectNeighbourKeysOfALand(Integer landKey, Map<Integer, AiRequest.LandCell> landCells) {
        if (!landCells.containsKey(landKey)) {
            throw new LandKeyNotExistAiException(landKey);
        }
        AiRequest.LandCell landCell = landCells.get(landKey);
        return landCell.getNeighbours();
    }
}

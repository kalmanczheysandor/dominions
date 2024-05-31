package hu.kalmancheysandor.application.dominion.api.ai.common;

import hu.kalmancheysandor.application.dominion.api.ai.common.exception.LandKeyNotExistAiException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AiEngine implements IAiEngine {

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
//                else if (neighbourCell.getPlayerKey() != yourKey && !isCausingDoughnutEffect) {
                else if (neighbourCell.getPlayerKey() != yourKey && !isCausingDoughnutEffect) {
                    attackZone.add(neighbourKey);
                }
            }
        }
        return attackZone;
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

    private static boolean isCellBlockedByPlayer(Map<Integer, AiRequest.LandCell> landCells, int observedCellKey, int playerKey) {
        Set<Integer> neighbourKeys = landCells.get(observedCellKey).getNeighbours();

        for (Integer neighbourKey : neighbourKeys) {
            AiRequest.LandCell neighbourCell = landCells.get(neighbourKey);

            if (neighbourCell.getPlayerKey() == null || neighbourCell.getPlayerKey() != playerKey) {
                return false;
            }
        }
        return true;
    }

    protected static Set<Integer> collectNeighbourKeysOfALand(Integer landKey, Map<Integer, AiRequest.LandCell> landCells) {
        if (!landCells.containsKey(landKey)) {
            throw new LandKeyNotExistAiException(landKey);
        }
        AiRequest.LandCell landCell = landCells.get(landKey);
        return landCell.getNeighbours();
    }

    protected static boolean isPlayerCausingDoughnutEffect(Map<Integer, AiRequest.LandCell> landCells, int playerKey) {
        System.out.println("isPlayerCausingDoughnutEffect(player:" + playerKey + ")");


        for (Integer emptyCellKey : getEmptyCellKeys(landCells)) {
            System.out.println("----EmptyKey:" + emptyCellKey);
            if (isCellBlockedByPlayer(landCells, emptyCellKey, playerKey)) {
                return true;
            }
        }
        return false;
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


}

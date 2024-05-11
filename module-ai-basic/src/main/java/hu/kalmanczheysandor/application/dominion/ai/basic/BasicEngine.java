package hu.kalmanczheysandor.application.dominion.ai.basic;

import hu.kalmanczheysandor.application.dominion.ai.common.AiEngine;
import hu.kalmanczheysandor.application.dominion.ai.common.AiRequest;
import hu.kalmanczheysandor.application.dominion.ai.common.AiResponse;
import hu.kalmanczheysandor.application.dominion.ai.common.exception.LandKeyNotExistAiException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BasicEngine extends AiEngine {
    @Override
    public AiResponse generateResponse(AiRequest request) {
        Integer yourKey = request.getYourKey();
        Integer reserveSize = request.getReserveSize();
        Integer attackPower = reserveSize;

        Map<Integer, AiRequest.LandCell> landCells = request.getLandCells();
        Set<Integer> myLandKeys = collectAllYourLand(yourKey, landCells);

        Integer targetLandKey   = null;
        if((targetLandKey=findEmptyLand(landCells,yourKey))!=null)  {
            return new AiResponse(targetLandKey,attackPower);
        }

        if((targetLandKey=findWeakerLand(landCells,yourKey,attackPower))!=null)  {
            return new AiResponse(targetLandKey,attackPower);
        }

        return new AiResponse(0,0);
    }

    private static Integer findEmptyLand(Map<Integer, AiRequest.LandCell> landCells, Integer yourKey) {
        Set<Integer> myLandKeys = collectAllYourLand(yourKey, landCells);

        // Attempt to find an empty land to attack
        for (Integer landKey : myLandKeys) {
            Set<Integer> neighbourKeys=collectNeighbourKeysOfALand(landKey,landCells);

            for (Integer neighbourKey : neighbourKeys) {
                if (isEmptyLand(neighbourKey,landCells)) {
                    return neighbourKey;
                }
            }
        }
        return null;
    }

    private static Integer findWeakerLand(Map<Integer, AiRequest.LandCell> landCells, Integer yourKey, Integer attackPower) {
        Set<Integer> myLandKeys = collectAllYourLand(yourKey, landCells);

        // Attempt to find an empty land to attack
        for (Integer landKey : myLandKeys) {
            Set<Integer> neighbourKeys=collectNeighbourKeysOfALand(landKey,landCells);

            for (Integer neighbourKey : neighbourKeys) {
                if (isLandWeakerThan(neighbourKey,attackPower,landCells)) {
                    return neighbourKey;
                }
            }
        }

        return null;
    }

    private static Set<Integer> collectAllYourLand(Integer yourKey, Map<Integer, AiRequest.LandCell> landCells) {
        Set<Integer> output = new HashSet<>();

        for (Map.Entry<Integer, AiRequest.LandCell> entry: landCells.entrySet()) {
            output.add(entry.getKey());
        }
        return output;
    }

    private static boolean isEmptyLand(Integer landKey, Map<Integer, AiRequest.LandCell> landCells) {
        if (!landCells.containsKey(landKey)) {
            throw new LandKeyNotExistAiException(landKey);
        }
        AiRequest.LandCell landCell = landCells.get(landKey);
        if (landCell.getTroopSize() == 0) {
            return true;
        }
        return false;
    }

    private static boolean isLandWeakerThan(Integer landKey, Integer attackPower, Map<Integer, AiRequest.LandCell> landCells) {
        if (!landCells.containsKey(landKey)) {
            throw new LandKeyNotExistAiException(landKey);
        }
        AiRequest.LandCell landCell = landCells.get(landKey);
        if (landCell.getTroopSize() < attackPower ) {
            return true;
        }
        return false;
    }

    private static Set<Integer> collectNeighbourKeysOfALand(Integer landKey, Map<Integer, AiRequest.LandCell> landCells) {
        if (!landCells.containsKey(landKey)) {
            throw new LandKeyNotExistAiException(landKey);
        }
        AiRequest.LandCell landCell = landCells.get(landKey);
        return landCell.getNeighbours();
    }
}

package hu.kalmancheysandor.application.dominion.api.ai.otto;

import hu.kalmancheysandor.application.dominion.api.ai.common.AiEngine;
import hu.kalmancheysandor.application.dominion.api.ai.common.IAiEngine;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiRequest;
import hu.kalmancheysandor.application.dominion.api.ai.common.exception.LandKeyNotExistAiException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class PseudoAiEngine extends AiEngine implements IAiEngine {





    protected static Integer findEmptyLand(Map<Integer, AiRequest.LandCell> landCells, Integer yourKey) {
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



                if (attackZoneKeys.contains(neighbourKey) &&
                        neighbourCell.isEmpty()
                ) {
                    System.out.println("return:" + neighbourKey + " playerKey:" + neighbourCell.getPlayerKey() + " yourKey" + yourKey);
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





}

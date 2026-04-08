package hu.kalmancheysandor.applications.dominions.apis.ai.otto;


import hu.kalmancheysandor.applications.dominions.apis.ai.common.TAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionContext;
import hu.kalmancheysandor.applications.dominions.apis.ai.common.IAiEngine;

import java.util.Map;
import java.util.Set;

public abstract class PseudoAiEngine extends TAiEngine implements IAiEngine {

    protected static Integer findEmptyLand(Map<Integer, AiDecisionContext.LandCell> landCells, Integer yourKey) {
        Set<Integer> myLandKeys = collectAllYourLand(yourKey, landCells);

        System.out.println("findEmptyLand (yourKey:" + yourKey + ")");
        Set<Integer> attackZoneKeys = collectCellKeysOfCurrentAttackZoneOfPlayer(landCells, yourKey);

        // Attempt to find an empty land to attack
        for (Integer landKey : myLandKeys) {
            Set<Integer> neighbourKeys = collectNeighbourKeysOfALand(landKey, landCells);
            System.out.println("--LandKey::" + landKey + "");

            for (Integer neighbourKey : neighbourKeys) {
                AiDecisionContext.LandCell neighbourCell = landCells.get(neighbourKey);
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

    protected static Integer findWeakerLand(Map<Integer, AiDecisionContext.LandCell> landCells, Integer yourKey, Integer attackPower) {
        Set<Integer> myLandKeys = collectAllYourLand(yourKey, landCells);

        System.out.println("findWeakerLand (yourKey:" + yourKey + ")");

        Set<Integer> attackZoneKeys = collectCellKeysOfCurrentAttackZoneOfPlayer(landCells, yourKey);

        // Attempt to find an empty land to attack
        for (Integer landKey : myLandKeys) {
            Set<Integer> neighbourKeys = collectNeighbourKeysOfALand(landKey, landCells);
            System.out.println("--LandKey::" + landKey + "");
            for (Integer neighbourKey : neighbourKeys) {
                AiDecisionContext.LandCell neighbourCell = landCells.get(neighbourKey);

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

    protected static Integer findStrongLand(Map<Integer, AiDecisionContext.LandCell> landCells, Integer yourKey, Integer attackPower) {
        Set<Integer> myLandKeys = collectAllYourLand(yourKey, landCells);
        System.out.println("findStrongLand (yourKey:" + yourKey + ")");

        Set<Integer> attackZoneKeys = collectCellKeysOfCurrentAttackZoneOfPlayer(landCells, yourKey);

        // Attempt to find an empty land to attack
        for (Integer landKey : myLandKeys) {
            Set<Integer> neighbourKeys = collectNeighbourKeysOfALand(landKey, landCells);
            System.out.println("--LandKey::" + landKey + "");

            for (Integer neighbourKey : neighbourKeys) {
                AiDecisionContext.LandCell neighbourCell = landCells.get(neighbourKey);

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

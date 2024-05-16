package hu.kalmanczheysandor.application.dominion.ai.basic;

import hu.kalmanczheysandor.application.dominion.ai.common.AiEngine;
import hu.kalmanczheysandor.application.dominion.ai.common.AiRequest;
import hu.kalmanczheysandor.application.dominion.ai.common.AiResponse;
import hu.kalmanczheysandor.application.dominion.ai.common.exception.LandKeyNotExistAiException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Basic1Engine extends BasicEngine {
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

        if((targetLandKey=findStrongLand(landCells,yourKey,attackPower))!=null)  {
            return new AiResponse(targetLandKey,attackPower);
        }

        return new AiResponse(0,0);
    }


}

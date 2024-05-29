package hu.kalmancheysandor.application.dominion.api.ai.otto;

import hu.kalmancheysandor.application.dominion.api.ai.common.AiRequest;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiResponse;

import java.util.Map;
import java.util.Set;

public class OttoAiEngine extends PseudoAiEngine {
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

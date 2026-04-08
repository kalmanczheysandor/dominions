package hu.kalmancheysandor.applications.dominions.apis.ai.otto;


import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionContext;
import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionResult;

import java.util.Map;
import java.util.Set;

public class OttoAiEngine extends PseudoAiEngine {
    @Override
    public AiDecisionResult makeDecision(AiDecisionContext request) {
        Integer yourKey = request.getYourPlayerKey();
        Integer reserveSize = request.getYourReserveSize();
        Integer attackPower = reserveSize;

        Map<Integer, AiDecisionContext.LandCell> landCells = request.getLandCells();
        Set<Integer> myLandKeys = collectAllYourLand(yourKey, landCells);

        Integer targetLandKey   = null;
        if((targetLandKey=findEmptyLand(landCells,yourKey))!=null)  {
            return new AiDecisionResult(targetLandKey,attackPower);
        }

        if((targetLandKey=findWeakerLand(landCells,yourKey,attackPower))!=null)  {
            return new AiDecisionResult(targetLandKey,attackPower);
        }

        if((targetLandKey=findStrongLand(landCells,yourKey,attackPower))!=null)  {
            return new AiDecisionResult(targetLandKey,attackPower);
        }

        return new AiDecisionResult(0,0);// Reserve
    }


}

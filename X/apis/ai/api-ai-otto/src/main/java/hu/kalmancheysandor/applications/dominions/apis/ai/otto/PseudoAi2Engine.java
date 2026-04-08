package hu.kalmancheysandor.applications.dominions.apis.ai.otto;


import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionContext;
import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionResult;

import java.util.Map;
import java.util.Set;


public class PseudoAi2Engine extends PseudoAiEngine {
    @Override
    public AiDecisionResult makeDecision(AiDecisionContext request) {
        Integer yourKey = request.getYourPlayerKey();
        Integer reserveSize = request.getYourReserveSize();
        Integer attackPower = reserveSize-1;



        if(isPlayerCausingDoughnutEffect(request.getLandCells(),yourKey)) {
            System.out.println("!!!!!!!!!!!Causing Lock["+yourKey+"]:true");
        } else {
            System.out.println("!!!!!!!!!!!Causing Lock["+yourKey+"]:NO");

        }








        Map<Integer, AiDecisionContext.LandCell> landCells = request.getLandCells();
        Set<Integer> myLandKeys = collectAllYourLand(yourKey, landCells);
        System.out.println(request);
        Integer targetLandKey   = null;

        if((targetLandKey=findStrongLand(landCells,yourKey,attackPower))!=null)  {
            System.out.println("AI2 - Find stronger");
            return new AiDecisionResult(targetLandKey,attackPower);
        }

        if((targetLandKey=findWeakerLand(landCells,yourKey,attackPower))!=null)  {
            System.out.println("AI2 - Find weaker");
            return new AiDecisionResult(targetLandKey,attackPower);
        }



        if((targetLandKey=findEmptyLand(landCells,yourKey))!=null)  {
            System.out.println("AI2 - Find empty");
            return new AiDecisionResult(targetLandKey,attackPower);
        }



        return new AiDecisionResult(0,0);
    }

}

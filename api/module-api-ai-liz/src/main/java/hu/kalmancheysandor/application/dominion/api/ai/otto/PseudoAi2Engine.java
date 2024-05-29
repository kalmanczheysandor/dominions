package hu.kalmancheysandor.application.dominion.api.ai.otto;

import hu.kalmancheysandor.application.dominion.api.ai.common.AiRequest;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiResponse;

import java.util.Map;
import java.util.Set;

public class PseudoAi2Engine extends PseudoAiEngine {
    @Override
    public AiResponse generateResponse(AiRequest request) {
        Integer yourKey = request.getYourKey();
        Integer reserveSize = request.getReserveSize();
        Integer attackPower = reserveSize-1;



        if(isPlayerCausingDoughnutEffect(request.getLandCells(),yourKey)) {
            System.out.println("!!!!!!!!!!!Causing Lock["+yourKey+"]:true");
        } else {
            System.out.println("!!!!!!!!!!!Causing Lock["+yourKey+"]:NO");

        }








        Map<Integer, AiRequest.LandCell> landCells = request.getLandCells();
        Set<Integer> myLandKeys = collectAllYourLand(yourKey, landCells);
        System.out.println(request);
        Integer targetLandKey   = null;

        if((targetLandKey=findStrongLand(landCells,yourKey,attackPower))!=null)  {
            System.out.println("AI2 - Find stronger");
            return new AiResponse(targetLandKey,attackPower);
        }

        if((targetLandKey=findWeakerLand(landCells,yourKey,attackPower))!=null)  {
            System.out.println("AI2 - Find weaker");
            return new AiResponse(targetLandKey,attackPower);
        }



        if((targetLandKey=findEmptyLand(landCells,yourKey))!=null)  {
            System.out.println("AI2 - Find empty");
            return new AiResponse(targetLandKey,attackPower);
        }



        return new AiResponse(0,0);
    }

}

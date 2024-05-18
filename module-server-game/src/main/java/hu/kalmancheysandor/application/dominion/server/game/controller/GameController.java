package hu.kalmancheysandor.application.dominion.server.game.controller;

import hu.kalmancheysandor.application.dominion.api.game.common.GameEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class GameController {

    @Autowired
    private GameEngine gameEngine;

    @GetMapping("/join/{joinKey}")
    public GameJoinResponse joinToAGame(@PathVariable String joinKey) {
        return new GameJoinResponse(GameJoinResponse.StatusCode.ACCEPTED,joinKey);
    }















    //
//    @GetMapping("/calculate")
//    @ResponseStatus(HttpStatus.OK)
//    public CalculationResponse calculate() {
//        log.info("Hello darling");
//        return new CalculationResponse(1,2,3);
//    }
//
//
//    @GetMapping("/attack")
//    @ResponseStatus(HttpStatus.OK)
//    public AiResponse attack() {
//
//        Map<Integer, AiRequest.LandCell> landCells = new HashMap<>();
//        landCells.put(1,new AiRequest.LandCell(1,2, Set.of(2,3)));
//        landCells.put(2,new AiRequest.LandCell(0,0, Set.of(1,4)));
//        landCells.put(3,new AiRequest.LandCell(0,0, Set.of(2,4)));
//        landCells.put(4,new AiRequest.LandCell(2,3, Set.of(3,2)));
//
//        AiRequest request= new AiRequest();
//        request.setYourKey(1);
//        request.setReserveSize(9);
//        request.setLandCells(landCells);
//
//        return engine.generateResponse(request);
//    }

}

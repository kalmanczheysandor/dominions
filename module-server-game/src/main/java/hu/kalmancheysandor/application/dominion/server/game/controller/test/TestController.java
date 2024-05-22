package hu.kalmancheysandor.application.dominion.server.game.controller.test;

import hu.kalmancheysandor.application.dominion.api.ai.common.AiRequest;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiResponse;
import hu.kalmancheysandor.application.dominion.server.game.service.game.dto.GameJoinResponse;
import hu.kalmancheysandor.application.dominion.server.game.service.test.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
public class TestController {

//    @Autowired
//    private GameEngine gameEngine;

    @Autowired
    private TestService gameService;


    @GetMapping("/join/{joinKey}")
    public GameJoinResponse joinToAGame(@PathVariable String joinKey) {
        //return new GameJoinResponse(GameJoinResponse.AnswerCode.ACCEPTED, joinKey);
        return null;
    }


    @GetMapping("/play")
    public String play() {

        gameService.play(5, 5);

        return "Results: okkk";
    }


    @GetMapping("/test")
    public String test() {

        Map<Integer, AiRequest.LandCell> landCells = new HashMap<>();
        landCells.put(1, new AiRequest.LandCell(1, 2, Set.of(2, 3)));
        landCells.put(2, new AiRequest.LandCell(0, 0, Set.of(1, 4)));
        landCells.put(3, new AiRequest.LandCell(0, 0, Set.of(2, 4)));
        landCells.put(4, new AiRequest.LandCell(2, 3, Set.of(3, 2)));

        AiRequest request1 = new AiRequest();
        request1.setYourKey(1);
        request1.setReserveSize(9);
        request1.setLandCells(landCells);

        AiRequest request2 = new AiRequest();
        request2.setYourKey(2);
        request2.setReserveSize(9);
        request2.setLandCells(landCells);


        CompletableFuture<AiResponse> future1 = gameService.callMicroservice1(request1);
        CompletableFuture<AiResponse> future2 = gameService.callMicroservice2(request2);

        // Wait for both futures to complete
        CompletableFuture.allOf(future1, future2).join();

        // Combine the results
        AiResponse response1 = future1.join();
        AiResponse response2 = future2.join();

        return "Results: " + response1.toString() + ", " + response2.toString();
    }

//
//
//    public String test() {
//
//        Map<Integer, AiRequest.LandCell> landCells = new HashMap<>();
//        landCells.put(1,new AiRequest.LandCell(1,2, Set.of(2,3)));
//        landCells.put(2,new AiRequest.LandCell(0,0, Set.of(1,4)));
//        landCells.put(3,new AiRequest.LandCell(0,0, Set.of(2,4)));
//        landCells.put(4,new AiRequest.LandCell(2,3, Set.of(3,2)));
//
//        AiRequest request1= new AiRequest();
//        request1.setYourKey(1);
//        request1.setReserveSize(9);
//        request1.setLandCells(landCells);
//
//        AiRequest request2= new AiRequest();
//        request2.setYourKey(2);
//        request2.setReserveSize(9);
//        request2.setLandCells(landCells);
//
//
//
//        AiResponse response3=gameService.callMicroservice3(request1);
//
//        return "Results: " + response3.toString();
//    }
//
//


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

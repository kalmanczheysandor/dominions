package hu.kalmancheysandor.applications.dominion.server.web.controller.play;

import hu.kalmancheysandor.application.dominion.server.game.service.game.dto.*;
import hu.kalmancheysandor.applications.dominion.server.web.configuration.mvc.AllowAjaxRequest;
import hu.kalmancheysandor.applications.dominion.server.web.configuration.mvc.AllowStandardRequest;
import hu.kalmancheysandor.applications.dominion.server.web.service.play.PlayService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/map/{sessionKey}")
//@BlockAllRequestByDefault
public class MapController {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PlayService playService;

    @GetMapping("/play/{playerIndex}")
    @AllowStandardRequest
    public String showPage(@PathVariable String sessionKey,@PathVariable int playerIndex) {
        return "play/PlayPage";
    }



    @PostMapping("/join")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public GameJoinResponse join(@PathVariable String sessionKey, @RequestBody GameJoinRequest request ) {
        return playService.join(sessionKey,request);
    }


    @PostMapping("/play/{playerIndex}/step")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public GameStateResponse action(@PathVariable String sessionKey,@PathVariable int playerIndex, @RequestBody GameStepRequest request ) {
        System.out.println("Go to Action");

        return playService.action(sessionKey, playerIndex, request);
    }

    @GetMapping("/current")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public GameStateResponse getGameState(@PathVariable String sessionKey) {
        return playService.currentGameState(sessionKey);
    }
}

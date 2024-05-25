package hu.kalmancheysandor.applications.dominion.server.web.controller.play;

import hu.kalmancheysandor.application.dominion.server.game.service.game.dto.GameStateResponse;
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

    @GetMapping("/play")
    @AllowStandardRequest
    public String showPage(@PathVariable String sessionKey) {
        return "play/PlayPage";
    }


    @GetMapping("/current")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public GameStateResponse getGameState(@PathVariable String sessionKey) {
        return playService.currentGameState(sessionKey);
    }
}

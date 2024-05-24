package hu.kalmancheysandor.applications.dominion.server.web.controller.game;


import hu.kalmancheysandor.application.dominion.server.game.service.game.dto.GameSessionItemResponse;
import hu.kalmancheysandor.applications.dominion.server.web.configuration.mvc.AllowAjaxRequest;
import hu.kalmancheysandor.applications.dominion.server.web.configuration.mvc.AllowStandardRequest;
import hu.kalmancheysandor.applications.dominion.server.web.service.game.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/game")
//@BlockAllRequestByDefault
//@CrossOrigin(origins = "*")

public class GameController {


    @Autowired
    private GameService gameService;

    @GetMapping("")
    @AllowStandardRequest
    public String showPage() {
        return "game/GameListPage";
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////// DATA METHODS //////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/data/list")
    @ResponseBody
    @AllowAjaxRequest
    public List<GameSessionItemResponse> listAllSession() {
        return gameService.listAllSession();
    }
}

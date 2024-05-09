package hu.kalmanczheysandor.applications.dominion.controller.game;

import hu.kalmanczheysandor.applications.dominion.configuration.mvc.AllowStandardRequest;
import hu.kalmanczheysandor.applications.dominion.configuration.mvc.BlockAllRequestByDefault;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web/game")
@BlockAllRequestByDefault
public class GameController {
  @Autowired
  private ModelMapper modelMapper;

  @GetMapping("")
  @AllowStandardRequest
  public String showPage() {
    return "game/GameListPage";
  }

}

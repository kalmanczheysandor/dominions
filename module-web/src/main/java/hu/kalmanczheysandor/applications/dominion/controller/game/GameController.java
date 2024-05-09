package hu.kalmanczheysandor.applications.dominion.controller.game;

import hu.kalmanczheysandor.application.dominion.service.CalculationResponse;
import hu.kalmanczheysandor.applications.dominion.configuration.mvc.AllowAjaxRequest;
import hu.kalmanczheysandor.applications.dominion.configuration.mvc.AllowStandardRequest;
import hu.kalmanczheysandor.applications.dominion.configuration.mvc.BlockAllRequestByDefault;
import hu.kalmanczheysandor.applications.dominion.controller.user.data.UserData;
import hu.kalmanczheysandor.applications.dominion.controller.user.data.UserUpdateFormData;
import hu.kalmanczheysandor.applications.dominion.proxy.ai.Ai1ServiceProxy;
import hu.kalmanczheysandor.applications.dominion.service.user.UserResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/game")
//@BlockAllRequestByDefault
public class GameController {
  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private Ai1ServiceProxy proxy;


  @GetMapping("")
  @AllowStandardRequest
  public String showPage() {
    return "game/GameListPage";
  }


  @GetMapping("/hello")
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  @AllowAjaxRequest
  public CalculationResponse hello() {
    return proxy.calculate();
  }


}

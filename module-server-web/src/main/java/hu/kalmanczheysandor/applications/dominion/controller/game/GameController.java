package hu.kalmanczheysandor.applications.dominion.controller.game;

import hu.kalmanczheysandor.application.dominion.ai.common.AiRequest;
import hu.kalmanczheysandor.application.dominion.ai.common.AiResponse;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/game")
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

//
//  @GetMapping("/attack")
//  @ResponseBody
//  @ResponseStatus(HttpStatus.OK)
//  @AllowAjaxRequest
//  public AiResponse attack() {
//    return proxy.attack();
//  }
//




  @GetMapping("/attack")
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  @AllowAjaxRequest
  public AiRequest attack() {

    Map<Integer, AiRequest.LandCell> landCells = new HashMap<>();
    landCells.put(1,new AiRequest.LandCell(1,2,Set.of(2,3)));
    landCells.put(2,new AiRequest.LandCell(0,0, Set.of(1,4)));
    landCells.put(3,new AiRequest.LandCell(0,0, Set.of(2,4)));
    landCells.put(4,new AiRequest.LandCell(2,3, Set.of(3,2)));

    AiRequest request= new AiRequest();
    request.setYourKey(1);
    request.setReserveSize(9);
    request.setLandCells(landCells);

    return request;
  }



}

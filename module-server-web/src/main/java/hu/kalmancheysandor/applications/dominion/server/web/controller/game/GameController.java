package hu.kalmancheysandor.applications.dominion.server.web.controller.game;

import hu.kalmancheysandor.application.dominion.api.ai.common.AiRequest;
import hu.kalmancheysandor.application.dominion.server.game.service.game.dto.GameStateResponse;
import hu.kalmancheysandor.applications.dominion.server.web.configuration.mvc.AllowAjaxRequest;
import hu.kalmancheysandor.applications.dominion.server.web.configuration.mvc.AllowStandardRequest;
import hu.kalmancheysandor.applications.dominion.server.web.proxy.ai.Ai1ServiceProxy;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/game")
//@BlockAllRequestByDefault
//@CrossOrigin(origins = "*")
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




















  @MessageMapping("/chat")
  @SendTo("/topic/messages")
  public OutputMessage send(Message message) throws Exception {
    System.out.println("HELLO: "+message);

    String time = new SimpleDateFormat("HH:mm").format(new Date());
    return new OutputMessage(message.getFrom(), message.getText(), time);
  }


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

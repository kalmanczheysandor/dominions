package hu.kalmancheysandor.applications.dominions.servers.site.service.game;


import hu.kalmancheysandor.applications.dominions.apis.server.common.component.config.ApplicationConfig;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.*;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.action.GamePlayAttackActionRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.action.GamePlayReserveActionRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.scenario.GameScenarioItemResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.entity.game.GameScenario;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.exception.game.scenario.GameScenarioNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.repository.game.GameScenarioRepository;
import hu.kalmancheysandor.applications.dominions.apis.util.file.filehandler.FileHandler;
import hu.kalmancheysandor.applications.dominions.servers.site.proxy.game.GameServerProxy;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GameService {

    @Autowired
    private GameServerProxy gameServerProxy;

    @Autowired
    private GameScenarioRepository gameScenarioRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ApplicationConfig applicationConfig;

    public GamePlayCreateResponse createGamePlay(@Valid @NotNull GamePlayCreateRequest request) {

        // Execution
        GamePlayCreateResponse response = gameServerProxy.createGamePlay(request);

        // Broadcast state to all belonging participants
        broadcastGamePlayStateToParticipants(response.getGameSessionUuid(), request.getEndpointKey(), request.getUserUuid());

        return response;
    }

    public GamePlayJoinResponse joinToGamePlay(@Valid @NotNull GamePlayJoinRequest request) {
        // Execution
        GamePlayJoinResponse response = gameServerProxy.joinToGamePlay(request);

        // Broadcast state to all belonging participants
        broadcastGamePlayStateToParticipants(request.getGameSessionUuid(), request.getEndpointKey(), request.getUserUuid());

        return response;
    }

    public List<GamePlayItemResponse> listAllRecruitingGamePlay() {
        return gameServerProxy.listAllRecruitingGamePlay();
    }

    public List<GameScenarioItemResponse> listAllPublishedGameScenario() {
        return gameServerProxy.listAllPublishedGameScenario();
    }



    private void broadcastGamePlayStateToParticipants(@NotBlank String gameSessionUuid, @NotBlank String endpointKey, @NotBlank String userUuid) {
        GamePlayStateRequest r = new GamePlayStateRequest();
        r.setGameSessionUuid(gameSessionUuid);
        r.setEndpointKey(endpointKey);
        r.setUserUuid(userUuid);
        GamePlayStateResponse response = gameServerProxy.stateOfGamePlay(r);
        System.out.println("Invoke state proxy");
        System.out.println(response);


        String outputChanel = "/queue/state/" + gameSessionUuid;
        for (String principalName : response.getParticipants()) {
            System.out.println("Broadcast-state: " + principalName + " TO:" + outputChanel);
            messagingTemplate.convertAndSendToUser(principalName, outputChanel, response);
        }
    }

    public GamePlayStateResponse stateOfGamePlay(@Valid @NotNull GamePlayStateRequest request) {
        return gameServerProxy.stateOfGamePlay(request);
    }

    public void sendAttackActionToGamePlay(@Valid @NotNull GamePlayAttackActionRequest request) {
        gameServerProxy.sendAttackActionToGamePlay(request);
        System.out.println("Site-attack:2");
        // Broadcast state to all belonging participants
        broadcastGamePlayStateToParticipants(request.getGameSessionUuid(), request.getEndpointKey(), request.getUserUuid());
        System.out.println("Site-attack:3");
    }

    public void sendReserveActionToGamePlay(@Valid @NotNull GamePlayReserveActionRequest request) {
        gameServerProxy.sendReserveActionToGamePlay(request);

        // Broadcast state to all belonging participants
        broadcastGamePlayStateToParticipants(request.getGameSessionUuid(), request.getEndpointKey(), request.getUserUuid());
    }


    public FileHandler.Result scenarioImage(@NotBlank String gameScenarioUuid) {

        // Access entity via repository
        GameScenario gameScenario = gameScenarioRepository.findByUuid(gameScenarioUuid);
        if (gameScenario == null) {
            throw new GameScenarioNotFoundByUuidException(gameScenarioUuid);
        }

        // Access image file
        int gameScenarioId = gameScenario.getId();
//        String fullPath = "d:/gameScenarios/" + gameScenarioId + "/main.jpg";
        String fullPath = getMainImagesFolder() + "/" + gameScenarioId + "/main.jpg";
        System.out.println("FULL PATH: " + fullPath);
        return FileHandler.accessFileContentIfExists(fullPath);
    }

    private String getMainImagesFolder() {
        return applicationConfig.getSiteServer().getGameScenario().getMainImagePath();
    }

}

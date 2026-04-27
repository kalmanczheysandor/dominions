package hu.kalmancheysandor.applications.dominions.servers.game.service.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.GameMap;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.GameMapEngineType;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.state.GameMapState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.*;
import hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.player.ArtificialPlayer;
import hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.player.HumanPlayer;
import hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.player.PlayerData;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.action.GameAction;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStatePlayer;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.exception.GeneralGameStateException;
import hu.kalmancheysandor.applications.dominions.apis.general.exceptions.IllegalPointOfExecution;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.queue.AiHistoryQueueItem;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.proxy.HugoAgentServerProxy;

//import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.proxy.hugo.HugoAgentServerProxy;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.proxy.LizAgentServerProxy;
import hu.kalmancheysandor.applications.dominions.apis.server.common.component.config.ApplicationConfig;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.action.*;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.scenario.GameScenarioItemResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.entity.game.GameScenario;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.entity.history.History;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.exception.game.scenario.GameScenarioNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.exception.game.scenario.GameScenarioNotFoundException;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.exception.game.scenario.GameScenarioNotPublishedException;

import hu.kalmancheysandor.applications.dominions.apis.server.game.common.repository.game.GameScenarioRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.repository.history.HistoryRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.entity.account.SiteUser;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.repository.account.SiteUserRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.*;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.entity.game.GameSession;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.exception.game.session.GameSessionNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.exception.game.session.GameSessionNotRecruitingException;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.exception.game.session.GameSessionStillRecruitingException;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.repository.game.GameSessionRepository;
import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDGenerator;
import hu.kalmancheysandor.applications.dominions.servers.game.queue.HistoryQueueItem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class GameService {

    @Autowired
    private GameSessionRepository gameSessionRepository;

    @Autowired
    private GameScenarioRepository gameScenarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UUIDGenerator uuidGenerator;

//    @Autowired
//    private PlayOrchestrator playOrchestrator;

//    @Autowired
//    private OttoAiAgenServerProxy ottoAiAgenServerProxy;
//
//    @Autowired
//    private EgonAiAgentServerProxy egonAiServerProxy;

    @Autowired
    private LizAgentServerProxy lizAgentServerProxy;

    @Autowired
    private HugoAgentServerProxy hugoAgentServerProxy;

//    @Autowired
//    private HelgaAiAgentServerProxy helgaAiAgentServerProxy;

    @Autowired
    private SiteUserRepository siteUserRepository;

    @Autowired
    private HistoryRepository historyRepository;


    @Autowired
    private StreamBridge streamBridge;

    @Autowired
    private ApplicationConfig applicationConfig;

    public GamePlayCreateResponse createGamePlay(@NotNull GamePlayCreateRequest request) {

        // Remove all expired sessions
        removeAllExpiredSessions();

        // Checking
        assertGameScenarioUuidExists(request.getScenarioUuid());
        assertGameScenarioIsPublished(request.getScenarioUuid());

        // Access scenario entity via repository
        GameScenario gameScenario = gameScenarioRepository.findByUuid(request.getScenarioUuid());

        // Initialise play state
//        GameMap gameMapObj = GameMap.createByJsonFile("maps/map26-H1A1-LizDecisionMaking-001.json");
        GameMap gameMapObj = GameMap.createByJsonContent(gameScenario.getGameMap());
        PlayState playStateObj = PlayState.createByGameMap(gameMapObj);

        // Orchestrator: Initialise + add a new human player
        PlayOrchestrator playOrchestrator = PlayOrchestrator.create(null, gameMapObj, playStateObj);
        HumanPlayer humanPlayer = playOrchestrator.addNewHumanPlayer(request.getUserUuid(), request.getPlayerName(), request.getEndpointKey());

        // Orchestrator: do some process
        playOrchestrator.process();

        // Orchestrator: get some values
        boolean isStillRecruiting = playOrchestrator.isAnyFreeHumanPlayerSlotsAvailable();
        int freeSlotCount = playOrchestrator.freeHumanPlayerSlotCount();

        // Convert objects to JsonString
        String playStateObjString = convertPlayStateObjToJson(playOrchestrator.getPlayState());
        String gameMapObjString = convertGameMapObjToJson(playOrchestrator.getGameMap());

        //
        GameMapState gameMapState = GameMapState.createByPlayState(playStateObj);

        // Save game
        GameSession gameSessionToSave = new GameSession();
        gameSessionToSave.setPlayState(playStateObjString);
        gameSessionToSave.setGameMap(gameMapObjString);
        gameSessionToSave.setFreeSlotCount(freeSlotCount);
        gameSessionToSave.setRecruiting(isStillRecruiting);
        gameSessionToSave.setVisible(isStillRecruiting);
        gameSessionToSave.setEnabled(true);
        gameSessionToSave.setScenarioId(gameScenario.getId());
        gameSessionToSave.setDateCreated(LocalDateTime.now());
        gameSessionToSave.setDateExpiration((LocalDateTime.now()).plusHours(applicationConfig.getGameServer().getSessionManagement().getExpirationHours()));
        GameSession gameSessionSaved = uuidGenerator.saveWithRetry(gameSessionRepository, gameSessionToSave);

        // Generate response
        GamePlayCreateResponse response = new GamePlayCreateResponse();
        response.setGameSessionUuid(gameSessionSaved.getUuid());
        response.setPlayerIndex(humanPlayer.getIndex());
        response.setSecretKey("ASecretKey");   //TODO
        response.setGameMapState(gameMapState);
        response.setGameMapConfiguration(gameMapObj.getConfiguration());
        response.setMaxHumanPlayerCount(playStateObj.getMaxHumanPlayerSlotCount());
        response.setCurrentHumanPlayerCount(playStateObj.getHumanPlayerCount());
        response.setStatusCode(playStateObj.getStatus());
        return response;
    }

    public GamePlayJoinResponse joinToGamePlay(@NotNull GamePlayJoinRequest request) {
        // Remove all expired sessions
        removeAllExpiredSessions();

        // Checking
        assertGameSessionUuidExists(request.getGameSessionUuid());
        assertGameSessionIsRecruiting(request.getGameSessionUuid());

        // Access entity via repository
        GameSession gameSessionToModify = gameSessionRepository.findByUuid(request.getGameSessionUuid());

        // Convert json fields to object
        PlayState playStateObj = convertJsonToPlayStateObj(gameSessionToModify.getPlayState());
        GameMap gameMapObj = convertJsonToGameMapObj(gameSessionToModify.getGameMap());

        // Orchestrator: Initialise + add a new human player
        PlayOrchestrator playOrchestrator = PlayOrchestrator.create(request.getGameSessionUuid(), gameMapObj, playStateObj);
        HumanPlayer humanPlayer = playOrchestrator.addNewHumanPlayer(request.getUserUuid(), request.getPlayerName(), request.getEndpointKey());

        // Orchestrator: do some process
        playOrchestrator.process();

        // Orchestrator: get some values
        boolean isStillRecruiting = playOrchestrator.isAnyFreeHumanPlayerSlotsAvailable();
        int freeSlotCount = playOrchestrator.freeHumanPlayerSlotCount();

        // Modify entity properties and save
        gameSessionToModify.setPlayState(convertPlayStateObjToJson(playOrchestrator.getPlayState()));
        gameSessionToModify.setFreeSlotCount(freeSlotCount);
        gameSessionToModify.setRecruiting(isStillRecruiting);
        gameSessionToModify.setVisible(isStillRecruiting);
        gameSessionToModify.setDateModified(LocalDateTime.now());
        gameSessionToModify.setDateExpiration((LocalDateTime.now()).plusHours(applicationConfig.getGameServer().getSessionManagement().getExpirationHours()));
        GameSession gameSessionModified = uuidGenerator.saveWithRetry(gameSessionRepository, gameSessionToModify);

        PlayState modifiedPlayState = playOrchestrator.getPlayState();

        // Generate response
        GamePlayJoinResponse response = new GamePlayJoinResponse();
        response.setGameSessionUuid(gameSessionModified.getUuid());
        response.setPlayerIndex(humanPlayer.getIndex());
        response.setSecretKey("ASecretKey");   //TODO

        response.setGameMapState(GameMapState.createByPlayState(modifiedPlayState)); // Already containing the joined player
        response.setGameMapConfiguration(gameMapObj.getConfiguration());

        response.setMaxHumanPlayerCount(modifiedPlayState.getMaxHumanPlayerSlotCount());
        response.setCurrentHumanPlayerCount(modifiedPlayState.getHumanPlayerCount());
        response.setStatusCode(modifiedPlayState.getStatus());

        return response;
    }


    public GamePlayStateResponse stateOfGamePlay(@NotNull GamePlayStateRequest request) {
        // Remove all expired sessions
        removeAllExpiredSessions();

        // Checking
        assertGameSessionUuidExists(request.getGameSessionUuid());

        // Access entity via repository
        GameSession gameSessionToFind = gameSessionRepository.findByUuid(request.getGameSessionUuid());

        // Convert json fields to object
        PlayState playStateObj = convertJsonToPlayStateObj(gameSessionToFind.getPlayState());
        GameMap gameMapObj = convertJsonToGameMapObj(gameSessionToFind.getGameMap());

        // Collect users
        List<String> participants = new ArrayList<>();
        for (PlayerData playerData : playStateObj.getPlayers().values()) {
            if (playerData.isArtificial()) {
                continue;
            }
            String uuid = playerData.getUserUuid();
            SiteUser siteUser = siteUserRepository.findByUuid(uuid);
            if (siteUser == null) {
                continue;
            }
            participants.add(siteUser.getIdentifier());
        }

        //
        int missingIntentionCount = calculateMissingIntentionCount(playStateObj);

        // Generate response
        GamePlayStateResponse response = new GamePlayStateResponse();
        response.setGameMapState(GameMapState.createByPlayState(playStateObj)); // Already containing the joined player
        response.setGameMapConfiguration(gameMapObj.getConfiguration());

        response.setMaxHumanPlayerCount(playStateObj.getMaxHumanPlayerSlotCount());
        response.setCurrentHumanPlayerCount(playStateObj.getHumanPlayerCount());
        response.setStatusCode(playStateObj.getStatus());
        response.setParticipants(participants);
        response.setTurn(playStateObj.getTurn());
        response.setWinnerKeys(playStateObj.getGameState().getWinnerKeys());
        response.setMissingPlayerRespondCount(missingIntentionCount);
        return response;
    }

    public List<GamePlayItemResponse> listAllRecruitingGamePlay() {

        // Remove all expired sessions
        removeAllExpiredSessions();

        // Access entity via repository
        List<GameSession> gameSessionList = gameSessionRepository.listAllRecruiting();

        // Generate response
        List<GamePlayItemResponse> response = new ArrayList<>();
        for (GameSession gameSession : gameSessionList) {

            GameScenario gameScenario = gameScenarioRepository.findById(gameSession.getScenarioId());
            // New item
            GamePlayItemResponse responseItem = new GamePlayItemResponse();
            responseItem.setUuid(gameSession.getUuid());
            responseItem.setFreeSlotCount(gameSession.getFreeSlotCount());
            responseItem.setScenarioUuid(gameScenario.getUuid());
            responseItem.setTitle(gameScenario.getTitle());
            responseItem.setDifficulty(gameScenario.getDifficulty());
            responseItem.setPlayerAiCount(gameScenario.getPlayerAiCount());
            responseItem.setPlayerHumanCount(gameScenario.getPlayerHumanCount());

            // Add to list
            response.add(responseItem);
        }
        return response;
    }

    public List<GameScenarioItemResponse> listAllPublishedGameScenario() {

        // Access entity via repository
        List<GameScenario> gameSessionList = gameScenarioRepository.listAllPublished();

        // Generate response
        List<GameScenarioItemResponse> response = new ArrayList<>();
        for (GameScenario gameScenario : gameSessionList) {

            // New item
            GameScenarioItemResponse responseItem = new GameScenarioItemResponse();
            responseItem.setUuid(gameScenario.getUuid());
            responseItem.setTitle(gameScenario.getTitle());
            responseItem.setDifficulty(gameScenario.getDifficulty());
            responseItem.setDescription(gameScenario.getDescription());

            responseItem.setPlayerHumanCount(gameScenario.getPlayerHumanCount());
            responseItem.setPlayerAiCount(gameScenario.getPlayerAiCount());

            // Add to list
            response.add(responseItem);
        }
        return response;
    }


//    public GamePlayResignResponse sendResignActionToGamePlay(GamePlayResignRequest request) {
//        // Remove all expired sessions
//        removeAllExpiredSessions();
//
//        // Checking
//        assertGameSessionUuidExists(request.getGameSessionUuid());
//        assertGameSessionIsNotRecruiting(request.getGameSessionUuid());
//
//
//        // Access entity via repository
//        GameSession gameSessionToModify = gameSessionRepository.findByUuid(request.getGameSessionUuid());
//
//        // Convert json fields to object
//        PlayState playStateObj = convertJsonToPlayStateObj(gameSessionToModify.getPlayState());
//        GameMap gameMapObj = convertJsonToGameMapObj(gameSessionToModify.getGameMap());
//
//        // Orchestrator: Initialise
//        PlayOrchestrator playOrchestrator = PlayOrchestrator.create(request.getGameSessionUuid(), gameMapObj, playStateObj);
//
//        // Orchestrator: Collecting all Ai intention and store
//        if (playOrchestrator.getAiPlayerMaxSlotSize() > 0 && playOrchestrator.aiMissingIntentionCount() > 0) {
//            Map<Integer, GameAction> aiPlayerIntentions = communicateToAiPlayers(playOrchestrator.getAllAiPlayers(), playOrchestrator.getPlayState(), gameSessionToModify.getScenarioId());
//            playOrchestrator.saveMultipleIntention(aiPlayerIntentions);
//        }
//
//
//        // Orchestrator: Initialise human intention and store
//        if (targetCellKey != null && targetCellKey == 0 && attackingTroopSize == 0) { // When it is a reserve-action
//            targetCellKey = null;
//        }
//        GameAction action = new GameAction(playerIndex, targetCellKey, attackingTroopSize);
//        playOrchestrator.saveIntention(playerIndex, action);
//
//        // Save intention into history before collision
//        if (playOrchestrator.isInPlayingStatus() && !playOrchestrator.isAnyMissingIntention()) {
//            saveCurrentIntentionsIntoHistory(gameSessionToModify, playOrchestrator.getPlayState());
//        }
//
//        // Orchestrator: do some process
//        playOrchestrator.process();
//
//        // Modify entity properties and save
//        gameSessionToModify.setPlayState(convertPlayStateObjToJson(playOrchestrator.getPlayState()));
//        gameSessionToModify.setDateModified(LocalDateTime.now());
//        gameSessionToModify.setDateExpiration((LocalDateTime.now()).plusHours(applicationConfig.getGame().getSession().getExpirationHours()));
//        GameSession gameSessionModified = uuidGenerator.saveWithRetry(gameSessionRepository, gameSessionToModify);
//
//        PlayState modifiedPlayState = playOrchestrator.getPlayState();
//        //Generate response
//        GamePlayAttackActionResponse response = new GamePlayAttackActionResponse();
//        return response;
//    }


    public GamePlayAttackActionResponse sendAttackActionToGamePlay(GamePlayAttackActionRequest request) {
        // Remove all expired sessions
        removeAllExpiredSessions();

        // Checking
        assertGameSessionUuidExists(request.getGameSessionUuid());
        assertGameSessionIsNotRecruiting(request.getGameSessionUuid());

        // Execution
        sendActionToGamePlay(request.getGameSessionUuid(), request.getPlayerIndex(), request.getTargetCellKey(), request.getAttackingTroopSize());

        //Generate response
        GamePlayAttackActionResponse response = new GamePlayAttackActionResponse();
        return response;
    }

    public GamePlayReserveActionResponse sendReserveActionToGamePlay(GamePlayReserveActionRequest request) {
        // Remove all expired sessions
        removeAllExpiredSessions();

        // Checking
        assertGameSessionUuidExists(request.getGameSessionUuid());
        assertGameSessionIsNotRecruiting(request.getGameSessionUuid());

        // Execution
        sendActionToGamePlay(request.getGameSessionUuid(), request.getPlayerIndex(), null, 0);

        //Generate response
        GamePlayReserveActionResponse response = new GamePlayReserveActionResponse();
        return response;
    }

    public GamePlayResignActionResponse sendResignActionToGamePlay(GamePlayResignActionRequest request) {
        // Remove all expired sessions
        removeAllExpiredSessions();

        // Checking
        assertGameSessionUuidExists(request.getGameSessionUuid());
        assertGameSessionIsNotRecruiting(request.getGameSessionUuid());

        // Execution
        sendActionToGamePlay(request.getGameSessionUuid(), request.getPlayerIndex(), null, 0);

        //Generate response
        GamePlayResignActionResponse response = new GamePlayResignActionResponse();
        return response;
    }






    private void sendActionToGamePlay(String gameSessionUuid, int playerIndex, Integer targetCellKey, int attackingTroopSize) {

        // Checking
        assertGameSessionUuidExists(gameSessionUuid);
        assertGameSessionIsNotRecruiting(gameSessionUuid);

        // Access entity via repository
        GameSession gameSessionToModify = gameSessionRepository.findByUuid(gameSessionUuid);

        // Access entity via repository
        GameScenario gameScenario = gameScenarioRepository.findById(gameSessionToModify.getScenarioId());
        if (gameScenario == null) {
            throw new GameScenarioNotFoundException(gameSessionToModify.getScenarioId());
        }


        // Convert json fields to object
        PlayState playStateObj = convertJsonToPlayStateObj(gameSessionToModify.getPlayState());
        GameMap gameMapObj = convertJsonToGameMapObj(gameSessionToModify.getGameMap());

        // Orchestrator: Initialise
        PlayOrchestrator playOrchestrator = PlayOrchestrator.create(gameSessionUuid, gameMapObj, playStateObj);

        // Orchestrator: Collecting all Ai intention and store
        if (playOrchestrator.getAiPlayerMaxSlotSize() > 0 && playOrchestrator.aiMissingIntentionCount() > 0) {
            Map<Integer, GameAction> aiPlayerIntentions = communicateToAiPlayers(
                    playOrchestrator.getAllAiPlayers(),
                    playOrchestrator.getPlayState(),
                    gameScenario.getUuid(),
                    gameScenario.getTitle(),
                    gameSessionToModify.getUuid()
            );
            playOrchestrator.saveMultipleIntention(aiPlayerIntentions);
        }


        // Orchestrator: Initialise human intention and store
        if (targetCellKey != null && targetCellKey == 0 && attackingTroopSize == 0) { // When it is a reserve-action
            targetCellKey = null;
        }
        GameAction action = new GameAction(playerIndex, targetCellKey, attackingTroopSize);
        playOrchestrator.saveIntention(playerIndex, action);

        // Save intention into history before collision
        if (playOrchestrator.isInPlayingStatus() && !playOrchestrator.isAnyMissingIntention()) {
            saveCurrentIntentionsIntoHistory(gameSessionToModify, playOrchestrator.getPlayState());
        }

        // Orchestrator: do some process
        playOrchestrator.process();

        // Modify entity properties and save
        gameSessionToModify.setPlayState(convertPlayStateObjToJson(playOrchestrator.getPlayState()));
        gameSessionToModify.setDateModified(LocalDateTime.now());
        gameSessionToModify.setDateExpiration((LocalDateTime.now()).plusHours(applicationConfig.getGameServer().getSessionManagement().getExpirationHours()));
        GameSession gameSessionModified = uuidGenerator.saveWithRetry(gameSessionRepository, gameSessionToModify);

        PlayState modifiedPlayState = playOrchestrator.getPlayState();

    }

    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////
    /// [History methods /////////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////

    private void saveCurrentIntentionsIntoHistory(GameSession gameSession, PlayState playState) {
        // Init
        GameScenario gameScenario = gameScenarioRepository.findById(gameSession.getScenarioId());

        // Put items into queue
        streamBridge.send("queueHistoryBroadcast-out-0", AiHistoryQueueItem.builder()
                .playState(playState)
                .scenarioUuid(gameScenario.getUuid())
                .sessionUuid(gameSession.getUuid())
                .build()
        );
    }

    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////
    ///  QUEUE METHODS  //////////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////
//    public void queueHistoryRead(HistoryQueueItem item) {
//        log.info("QUEUE[-History-].Reader: {}\n", item);
//        PlayState playState = item.getPlayState();
//        GameState gameState = playState.getGameState();
//
//
//        Set<GameAction> intentions = new HashSet<>();
//        for (Map.Entry<Integer, PlayerData> playerEntry : playState.getPlayers().entrySet()) {
//            ///
//            Integer playerIndex = playerEntry.getKey();
//            PlayerData playerData = playerEntry.getValue();
//
//            ///
//            if (!gameState.getOpponent(playerIndex).isAlive()) {
//                continue;
//            }
//
//            if (!playerData.isIntentionAlreadyGiven()) {
//                throw new GeneralGameStateException("No intention is present for player! Player index:" + playerData.getIndex());
//            }
//
//            ///
//            GameAction playerIntention = playerData.getIntention();
//            int reserveSize = gameState.getOpponents()[playerIndex].getReserveSize();
//            int enemiesCount = gameState.getOpponents().length - 1;
//            String playerNameCode = playerData.getName();
//            String userUuid = playerData.getUserUuid();
//
//            PlayerDecision playerDecision = PlayerDecision.create(playerIndex, playerIntention.getTargetCellKey(), playerIntention.getAttackingTroopSize(), reserveSize, enemiesCount, gameState);
//
//            // Save history
//            History history = new History();
//            history.setPlayer(playerNameCode);
//            history.setUserUuid(userUuid);
//            history.setDecision(convertPlayerDecisionObjToJson(playerDecision));
//            historyRepository.save(history);
//        }
//
//
//    }
//

    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////
    /// /  Assert methods /////////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////

    private void assertGameScenarioUuidExists(@NotBlank String gameScenarioUuid) {
        GameScenario gameScenario = gameScenarioRepository.findByUuid(gameScenarioUuid);
        if (gameScenario == null) {
            throw new GameScenarioNotFoundByUuidException(gameScenarioUuid);
        }
    }

    private void assertGameScenarioIsPublished(@NotBlank String gameScenarioUuid) {
        GameScenario gameScenario = gameScenarioRepository.findByUuid(gameScenarioUuid);
        if (!gameScenario.isPublished()) {
            throw new GameScenarioNotPublishedException(gameScenarioUuid);
        }
    }

    private void assertGameSessionUuidExists(@NotBlank String gameSessionUuid) {
        GameSession gameSession = gameSessionRepository.findByUuid(gameSessionUuid);
        if (gameSession == null) {
            throw new GameSessionNotFoundByUuidException(gameSessionUuid);
        }
    }

    private void assertGameSessionIsRecruiting(@NotBlank String gameSessionUuid) {
        assertGameSessionUuidExists(gameSessionUuid);

        GameSession gameSession = gameSessionRepository.findByUuid(gameSessionUuid);
        if (!gameSession.isRecruiting()) {
            throw new GameSessionNotRecruitingException(gameSessionUuid);
        }
    }

    private void assertGameSessionIsNotRecruiting(@NotBlank String gameSessionUuid) {
        assertGameSessionUuidExists(gameSessionUuid);

        GameSession gameSession = gameSessionRepository.findByUuid(gameSessionUuid);
        if (gameSession.isRecruiting()) {
            throw new GameSessionStillRecruitingException(gameSessionUuid);
        }
    }

    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Convert methods //////////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////

    private String convertGameMapObjToJson(GameMap gameMapObj) {
        try {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            return objectMapper.writeValueAsString(gameMapObj);
        } catch (JsonProcessingException e) {
            throw new GeneralGameStateException("Error at parsing");
        }
    }

    private GameMap convertJsonToGameMapObj(String gameMapStr) {
        try {
            return objectMapper.readValue(gameMapStr, new TypeReference<GameMap>() {
            });
//            return objectMapper.readValue(gameMapStr, GameMap.class);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String convertPlayStateObjToJson(PlayState playStateObj) {
        try {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            return objectMapper.writeValueAsString(playStateObj);
        } catch (JsonProcessingException e) {
            throw new GeneralGameStateException("Error at parsing");
        }
    }

    private PlayState convertJsonToPlayStateObj(String playStateStr) {
        try {
            return objectMapper.readValue(playStateStr, new TypeReference<PlayState>() {
            });
//            return objectMapper.readValue(playStateStr, PlayState.class);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String convertPlayerDecisionObjToJson(PlayerDecision playerDecision) {
        try {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            return objectMapper.writeValueAsString(playerDecision);
        } catch (JsonProcessingException e) {
            throw new GeneralGameStateException("Error at parsing");
        }
    }


    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Request and Response generator methods ///////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////

    private AiDecisionRequest generateAiRequest(int yourPlayerKey, String yourUserUuid, String yourUserName, String yourCharacterCode, PlayState playStateObj, String scenarioUuid, String scenarioTitle, String sessionUuid) {

        GameState gameState = playStateObj.getGameState();

        AiDecisionRequest.Player aiDecisionplayer;
        Map<Integer, AiDecisionRequest.Player> decisionPlayersList = new HashMap<>();
        for (PlayerData playerData : playStateObj.getPlayers().values()) {

            // Add
            decisionPlayersList.put(playerData.getIndex(), AiDecisionRequest.Player.builder()
                    .playerKey(playerData.getIndex())
                    .playerType(playerData.getPlayerType())
                    .name(playerData.getName())
                    .userUuid(playerData.getUserUuid())
                    .build());
        }

        // Collect reserve size of each player
        GameStatePlayer[] players = gameState.getOpponents();
        GameStatePlayer player;
        Map<Integer, Integer> reserveSizeList = new HashMap<>();
        for (int playerKey = 0; playerKey < players.length; playerKey++) {
            player = players[playerKey];
            reserveSizeList.put(playerKey, player.getReserveSize());
        }

        // Generate return object
        return AiDecisionRequest.builder()
                .scenarioUuid(scenarioUuid)
                .scenarioName(scenarioTitle)
                .sessionUuid(sessionUuid)
                .playerCharacterCode(yourCharacterCode)
                .yourPlayerKey(yourPlayerKey)
                .yourUserUuid(yourUserUuid)
                .yourUserName(yourUserName)
                .yourReserveSize(gameState.getOpponents()[yourPlayerKey].getReserveSize())
                .reserves(reserveSizeList)
                .gameState(gameState)
                .enemyPlayers(decisionPlayersList)
                .build();
    }


    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Helper methods-Ai communications /////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////


    private Map<Integer, GameAction> communicateToAiPlayers(Map<Integer, PlayerData> players, PlayState playState, String scenarioUuid, String scenarioTitle, String sessionUuid) {

        Map<Integer, GameAction> intentions = new HashMap<>();
        for (Map.Entry<Integer, PlayerData> playerEntry : players.entrySet()) {

            // Jump to next player if this one is not an artificial one
            if (!playerEntry.getValue().isArtificial()) {
                continue;
            }

            //
            int aiPlayerIndex = playerEntry.getKey();
            ArtificialPlayer aiPlayer = (ArtificialPlayer) playerEntry.getValue();

            // Send request
            AiDecisionResponse aiDecisionResponseObj = waitingAiResponse(aiPlayer, generateAiRequest(
                            aiPlayerIndex,
                            aiPlayer.getUserUuid(),
                            aiPlayer.getName(),
                            aiPlayer.getCharacterCode(),
                            playState,
                            scenarioUuid,
                            scenarioTitle,
                            sessionUuid
                    )
            );

            // Create an Action by Ai response
            Integer targetCellKey = aiDecisionResponseObj.getTargetCellKey();
            Integer troopSize = aiDecisionResponseObj.getTroopSize();
            if (targetCellKey != null && targetCellKey == 0 && troopSize == 0) { // When it is a reserve-action
                targetCellKey = null;
            }
            GameAction aiAction = new GameAction(aiPlayerIndex, targetCellKey, troopSize);


            intentions.put(aiPlayerIndex, aiAction);


            System.out.println("Ai Intention is[" + aiPlayerIndex + "]:" + aiAction);
        }

        return intentions;
    }

    private AiDecisionResponse waitingAiResponse(ArtificialPlayer artificialPlayer, AiDecisionRequest aiDecisionRequestObj) {

        GameMapEngineType engineType = artificialPlayer.getEngineType();

        // Create an Ai request object
        AiDecisionResponse aiDecisionResponseObj;

        // Wait for ai response
//        if (engineType == GameMapEngineType.AI_OTTO) {
//        //    aiDecisionResponseObj = ottoAiAgenServerProxy.generateResponse(aiDecisionRequestObj);
//        } else if (GameMapEngineType.AI_EGON == engineType) {
//          //  aiDecisionResponseObj = egonAiServerProxy.generateResponse(aiDecisionRequestObj);
//        } else if (GameMapEngineType.AI_LIZ == engineType) {
//            //aiDecisionResponseObj = lizAgentServerProxy.generateResponse(aiDecisionRequestObj);
//
//        } else if (GameMapEngineType.AI_HUGO == engineType) {
//            //aiDecisionResponseObj = hugoAiAgentServerProxy.generateResponse(aiDecisionRequestObj);
//        } else if (GameMapEngineType.AI_HELGA == engineType) {
//        //aiDecisionResponseObj = helgaAiAgentServerProxy.generateResponse(aiDecisionRequestObj);
//        } else {
//            throw new IllegalPointOfExecution("Unknown ai engine:" + engineType.name());
//        }
//

        if (engineType == GameMapEngineType.AI_LIZ) {
            aiDecisionResponseObj = lizAgentServerProxy.generateResponse(aiDecisionRequestObj);
        } else if (engineType == GameMapEngineType.AI_HUGO) {
            aiDecisionResponseObj = hugoAgentServerProxy.generateResponse(aiDecisionRequestObj);
        } else {
            throw new IllegalPointOfExecution("Unknown ai engine:" + engineType.name());
        }

        return aiDecisionResponseObj;
    }

    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Helper methods //////////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////
    private int calculateMissingIntentionCount(PlayState playState) {
        int count = 0;
        for (PlayerData player : playState.getPlayers().values()) {
            int playerIndex = player.getIndex();

            if (!player.isIntentionAlreadyGiven() && playState.getGameState().getOpponent(playerIndex).isAlive()) {
                count++;
            }
        }
        return count;
    }

    private static Set<Integer> collectNeighboursOfACell(boolean[][] matrix, int cellKey) {
        Set<Integer> output = new HashSet<>();
        boolean[] row = matrix[cellKey];
        for (int neighbourKey = 0; neighbourKey < row.length; neighbourKey++) {
            if (row[neighbourKey] == true) {
                output.add(neighbourKey);
            }
        }
        return output;
    }

    private void removeAllExpiredSessions() {
        gameSessionRepository.deleteAllExpired(LocalDateTime.now());
    }

}

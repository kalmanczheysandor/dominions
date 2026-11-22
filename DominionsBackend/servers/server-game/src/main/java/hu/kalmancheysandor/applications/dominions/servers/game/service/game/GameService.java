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

import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.proxy.LizAgentServerProxy;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.proxy.OttoAgentServerProxy;
import hu.kalmancheysandor.applications.dominions.apis.server.common.component.config.ApplicationConfig;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.scenario.GameScenarioItemResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.entity.game.GameScenario;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.exception.game.scenario.GameScenarioNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.exception.game.scenario.GameScenarioNotFoundException;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.exception.game.scenario.GameScenarioNotPublishedException;

import hu.kalmancheysandor.applications.dominions.apis.server.game.common.repository.game.GameScenarioRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.repository.history.HistoryRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.entity.account.SiteUser;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.repository.account.SiteUserRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.*;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.action.GamePlayAttackActionRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.action.GamePlayAttackActionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.action.GamePlayReserveActionRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.action.GamePlayReserveActionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.entity.game.GameSession;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.exception.game.session.GameSessionNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.exception.game.session.GameSessionNotRecruitingException;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.exception.game.session.GameSessionStillRecruitingException;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.repository.game.GameSessionRepository;
import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDGenerator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

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


    @Autowired
    private LizAgentServerProxy lizAgentServerProxy;

    @Autowired
    private HugoAgentServerProxy hugoAgentServerProxy;

    @Autowired
    private OttoAgentServerProxy ottoAgentServerProxy;


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
        return GamePlayCreateResponse.builder()
                .gameSessionUuid(gameSessionSaved.getUuid())
                .playerIndex(humanPlayer.getIndex())
                .secretKey("ASecretKey")   //TODO
                .gameMapState(gameMapState)
                .gameMapConfiguration(gameMapObj.getConfiguration())
                .maxHumanPlayerCount(playStateObj.getMaxHumanPlayerSlotCount())
                .currentHumanPlayerCount(playStateObj.getHumanPlayerCount())
                .statusCode(playStateObj.getStatus())
                .build();
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
        return GamePlayJoinResponse.builder()
                .gameSessionUuid(gameSessionModified.getUuid())
                .playerIndex(humanPlayer.getIndex())
                .secretKey("ASecretKey")   //TODO
                .gameMapState(GameMapState.createByPlayState(modifiedPlayState)) // Already containing the joined player
                .gameMapConfiguration(gameMapObj.getConfiguration())
                .maxHumanPlayerCount(modifiedPlayState.getMaxHumanPlayerSlotCount())
                .currentHumanPlayerCount(modifiedPlayState.getHumanPlayerCount())
                .statusCode(modifiedPlayState.getStatus())
                .build();
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
        return GamePlayStateResponse.builder()
                .gameMapState(GameMapState.createByPlayState(playStateObj))
                .gameMapConfiguration(gameMapObj.getConfiguration())
                .maxHumanPlayerCount(playStateObj.getMaxHumanPlayerSlotCount())
                .currentHumanPlayerCount(playStateObj.getHumanPlayerCount())
                .statusCode(playStateObj.getStatus())
                .participants(participants)
                .turn(playStateObj.getTurn())
                .winnerKeys(playStateObj.getGameState().getWinnerKeys())
                .missingPlayerRespondCount(missingIntentionCount)
                .build();
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

            // Add to list
            response.add(GamePlayItemResponse.builder()
                    .uuid(gameSession.getUuid())
                    .freeSlotCount(gameSession.getFreeSlotCount())
                    .scenarioUuid(gameScenario.getUuid())
                    .title(gameScenario.getTitle())
                    .difficulty(gameScenario.getDifficulty())
                    .playerAiCount(gameScenario.getPlayerAiCount())
                    .playerHumanCount(gameScenario.getPlayerHumanCount())
                    .build()
            );
        }
        return response;
    }

    public List<GameScenarioItemResponse> listAllPublishedGameScenario() {

        // Access entity via repository
        List<GameScenario> gameSessionList = gameScenarioRepository.listAllPublished();

        // Generate response
        List<GameScenarioItemResponse> response = new ArrayList<>();
        for (GameScenario gameScenario : gameSessionList) {

            // Add to list
            response.add(GameScenarioItemResponse.builder()
                    .uuid(gameScenario.getUuid())
                    .title(gameScenario.getTitle())
                    .difficulty(gameScenario.getDifficulty())
                    .description(gameScenario.getDescription())
                    .playerHumanCount(gameScenario.getPlayerHumanCount())
                    .playerAiCount(gameScenario.getPlayerAiCount())
                    .build()
            );
        }
        return response;
    }


    public GamePlayAttackActionResponse sendAttackActionToGamePlay(GamePlayAttackActionRequest request) {
        // Remove all expired sessions
        removeAllExpiredSessions();

        // Checking
        assertGameSessionUuidExists(request.getGameSessionUuid());
        assertGameSessionIsNotRecruiting(request.getGameSessionUuid());

        // Execution
        sendActionToGamePlay(
                request.getGameSessionUuid(),
                request.getPlayerIndex(),
                request.getTargetCellKey(),
                request.getAttackingTroopSize()
        );

        //Generate response
        return GamePlayAttackActionResponse.builder()
                .build();
    }

    public GamePlayReserveActionResponse sendReserveActionToGamePlay(GamePlayReserveActionRequest request) {
        // Remove all expired sessions
        removeAllExpiredSessions();

        // Checking
        assertGameSessionUuidExists(request.getGameSessionUuid());
        assertGameSessionIsNotRecruiting(request.getGameSessionUuid());

        // Execution
        sendActionToGamePlay(
                request.getGameSessionUuid(),
                request.getPlayerIndex(),
                null,
                0
        );

        //Generate response
        return GamePlayReserveActionResponse.builder()
                .build();
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
            playOrchestrator.saveMultipleIntention(communicateToAiPlayers(
                    playOrchestrator.getAllAiPlayers(),
                    playOrchestrator.getPlayState(),
                    gameScenario.getUuid(),
                    gameScenario.getTitle(),
                    gameSessionToModify.getUuid()
            ));
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
                .scenarioName(gameScenario.getTitle())
                .build()
        );
    }


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
            return objectMapper.readValue(gameMapStr, new TypeReference<GameMap>() {});
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
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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

        class AiAnswer {
            private int playerKey;
            private AiDecisionResponse response;

            AiAnswer(int playerKey, AiDecisionResponse response) {
                this.playerKey = playerKey;
                this.response = response;
            }

            public int getPlayerKey() {
                return playerKey;
            }

            public AiDecisionResponse getResponse() {
                return response;
            }
        }
        List<CompletableFuture<AiAnswer>> futures = new ArrayList<>();

        // Call asynchronously all ai endpoints
        for (Map.Entry<Integer, PlayerData> playerEntry : players.entrySet()) {
            // Jump to next player if this one is not an artificial one
            if (!playerEntry.getValue().isArtificial()) {
                continue;
            }

            //
            int aiPlayerIndex = playerEntry.getKey();
            ArtificialPlayer aiPlayer = (ArtificialPlayer) playerEntry.getValue();

            // Send request
            futures.add(CompletableFuture.supplyAsync(() -> {
                AiDecisionRequest requestObj = generateAiRequest(
                        aiPlayerIndex,
                        aiPlayer.getUserUuid(),
                        aiPlayer.getName(),
                        aiPlayer.getCharacterCode(),
                        playState,
                        scenarioUuid,
                        scenarioTitle,
                        sessionUuid
                );
                AiDecisionResponse responseObj = waitingAiResponse(aiPlayer, requestObj);

                return new AiAnswer(aiPlayerIndex, responseObj);
            }));

        }

        // Wait for and join all async responses
        List<AiAnswer> responseList = futures.stream()
                .map(CompletableFuture::join)
                .toList();

        // Save Intentions
        Map<Integer, GameAction> intentions = new HashMap<>();
        for (AiAnswer answer : responseList) {
            AiDecisionResponse decisionObj = answer.getResponse();
            int aiPlayerIndex = answer.getPlayerKey();

            // Create an Action by Ai response
            Integer targetCellKey = decisionObj.getTargetCellKey();
            Integer troopSize = decisionObj.getTroopSize();
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
        if (engineType == GameMapEngineType.AI_LIZ) {
            aiDecisionResponseObj = lizAgentServerProxy.generateResponse(aiDecisionRequestObj);
        } else if (engineType == GameMapEngineType.AI_HUGO) {
            aiDecisionResponseObj = hugoAgentServerProxy.generateResponse(aiDecisionRequestObj);
        } else if (engineType == GameMapEngineType.AI_OTTO) {
            aiDecisionResponseObj = ottoAgentServerProxy.generateResponse(aiDecisionRequestObj);
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

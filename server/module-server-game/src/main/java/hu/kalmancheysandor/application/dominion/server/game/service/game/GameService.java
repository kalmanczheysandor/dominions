package hu.kalmancheysandor.application.dominion.server.game.service.game;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiRequest;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiResponse;
import hu.kalmancheysandor.application.dominion.api.game.common.engine.GameEngine;
import hu.kalmancheysandor.application.dominion.api.game.common.engine.GameMap;
import hu.kalmancheysandor.application.dominion.api.game.common.engine.GameState;
import hu.kalmancheysandor.application.dominion.api.game.common.engine.exception.GeneralGameException;
import hu.kalmancheysandor.application.dominion.api.game.common.session.*;
import hu.kalmancheysandor.application.dominion.api.game.common.session.exception.NoMoreFreePlayerSlotSessionException;
import hu.kalmancheysandor.application.dominion.api.game.common.session.exception.NotExistingInstanceSessionException;
import hu.kalmancheysandor.application.dominion.api.game.common.session.exception.PendingTurnSessionException;
import hu.kalmancheysandor.application.dominion.api.general.IllegalPointOfExecution;
import hu.kalmancheysandor.application.dominion.server.game.domain.History;
import hu.kalmancheysandor.application.dominion.server.game.proxy.OttoAiServerProxy;
import hu.kalmancheysandor.application.dominion.server.game.proxy.LizAiServerProxy;
import hu.kalmancheysandor.application.dominion.server.game.repository.game.HistoryRepository;
import hu.kalmancheysandor.application.dominion.server.game.repository.game.SessionRepository;
import hu.kalmancheysandor.application.dominion.server.game.service.game.dto.*;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class GameService {

    private static final Logger log = LoggerFactory.getLogger(GameService.class);
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private GameEngine gameEngine;

    @Autowired
    private OttoAiServerProxy ottoAiServerProxy;

    @Autowired
    private LizAiServerProxy lizAiServerProxy;

    @Autowired
    private ObjectMapper objectMapper;


    public GameCreateResponse create() {
        PlaySession newSession = sessionRepository.createSession();
        return new GameCreateResponse(newSession.getSessionKey());
    }

    public List<GameSessionItemResponse> list() {
        return sessionRepository.list().stream().map(item -> {
            GameSessionItemResponse response = new GameSessionItemResponse();
            response.setSessionKey(item.getSessionKey());
            response.setTitle(item.getSessionKey());
            response.setStatus(item.getStatus());
            response.setMapName(item.getGameMap().getMapName());
            return response;
        }).collect(Collectors.toList());
    }


    public GameJoinResponse join(String sessionKey, GameJoinRequest request) {
        // Find session
        validateSessionAccess(sessionKey);
        PlaySession session = sessionRepository.findSession(sessionKey);

        if (!session.isAnyFreeHumanPlayerSlotsAvailable()) {

            throw new NoMoreFreePlayerSlotSessionException(session.getSessionKey());
        }

        int playerIndex = session.nextAvailableHumanPlayerIndex();
        HumanPlayer player = new HumanPlayer(playerIndex, request.getName());
        session.addHumanPlayer(player);

        return new GameJoinResponse(playerIndex, "YourSecretKey");
    }


    public GameStateResponse currentState(String sessionKey) {
        validateSessionAccess(sessionKey);
        return generateGameStateResponse(sessionKey);
    }

    public GameStateResponse doStep(String sessionKey, int playerIndex, GameStepRequest request) {

        // Find session
        validateSessionAccess(sessionKey);
        PlaySession session = sessionRepository.findSession(sessionKey);

        // Save intention
        GameEngine.Action action = new GameEngine.Action(playerIndex, request.getTargetCellKey(), request.getAttackingTroopSize());
        saveIntention(sessionKey, playerIndex, action);

        // Ai calls after all human send their actions
        if (session.humanPendingCount() == 0 && session.getAiPlayerMaxSlotSize() > 0) {
            for (Map.Entry<Integer, PlayerData> playerEntry : session.getPlayers().entrySet()) {
                int aiPlayerIndex = playerEntry.getKey();
                PlayerData player = playerEntry.getValue();
                if (player.isArtificial()) {
                    ArtificialPlayer.EngineType engineType = ((ArtificialPlayer) player).getEngineType();
                    AiResponse aiResponse;
                    if (engineType == ArtificialPlayer.EngineType.OTTO) {
                        aiResponse = ottoAiServerProxy.generateResponse(createRequest(aiPlayerIndex, session.getGameState()));
                    } else if (engineType == ArtificialPlayer.EngineType.LIZ) {
                        aiResponse = lizAiServerProxy.generateResponse(createRequest(aiPlayerIndex, session.getGameState()));
                    } else {
                        throw new IllegalPointOfExecution("Unknown ai engin:" + engineType.name());
                    }

                    System.out.println("AIIII(" + engineType.name() + ";" + aiPlayerIndex + "):" + aiResponse);
                    GameEngine.Action aiAction = new GameEngine.Action(aiPlayerIndex, aiResponse.getTargetCellKey(), aiResponse.getTroopSize());
                    saveIntention(sessionKey, aiPlayerIndex, aiAction);
                    System.out.println("Ai saved intention");


                }
            }
        }

        if (!session.isPending()) {
            try {
                for (GameEngine.Action intention : session.getAllIntention()) {
                    int reserveSize = session.getGameState().getOpponents()[intention.getPlayerKey()].getReserveSize();
                    int enemiesCount = session.getGameState().getOpponents().length - 1;

                    //GameMap.Opponent mapPlayer = session.getGameMap().getPlayers().get(intention.getPlayerKey());

                    String playerNameCode = "HUMAN_KALMANCZHEYSANDOR@GAMIL-COM";
                    PlayerData playerData = session.findPlayer(intention.getPlayerKey());
                    if (playerData.isArtificial()) {
                        playerNameCode = "ARTIFICIAL_" + ((ArtificialPlayer) playerData).getEngineType();
                    }


                    PlayerDecision playerDecision = PlayerDecision.create(intention.getTargetCellKey(), intention.getAttackingTroopSize(), reserveSize, enemiesCount, session.getGameState());

                    String playerDecisionString;
                    try {
                        playerDecisionString = objectMapper.writeValueAsString(playerDecision);
                    } catch (JsonProcessingException e) {
                        throw new GeneralGameException("Error at parsing");
                    }

                    History history = new History();
                    history.setPlayer(playerNameCode);
                    history.setDecision(playerDecisionString);
                    historyRepository.save(history);
                }

                GameState newState = gameEngine.doAction(session.getAllIntention(), session.getGameState());
                session.setGameState(newState);
                if (newState.getStatusCode() == GameState.StatusCode.FINISHED) {
                    session.setStatus(SessionStatusCode.ENDED);
                }

                session.eliminateAllIntention();
                session.incrementTurn();
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }

        return generateGameStateResponse(sessionKey);
    }

    private void saveIntention(String sessionKey, int playerIndex, GameEngine.Action intention) {
        // Find session
        validateSessionAccess(sessionKey);
        PlaySession session = sessionRepository.findSession(sessionKey);

        GameEngine.validatePlayerAction(session.getGameState(), intention);
        session.saveIntention(playerIndex, intention);
    }


    private static AiRequest createRequest(int yourKey, GameState gameState) {
        Map<Integer, AiRequest.LandCell> landCells = new HashMap<>();
        GameState.Cell[] cells = gameState.getCells();
        for (int cellKey = 0; cellKey < cells.length; cellKey++) {
            GameState.Cell cell = cells[cellKey];

            Integer occupierKey = null;
            if (cell.getOccupierKey() > -1) {
                occupierKey = cell.getOccupierKey();
            }


            Set<Integer> neighbours = neighbours(gameState.getNeighboursMatrix(), cellKey);
            landCells.put(cellKey, new AiRequest.LandCell(occupierKey, cell.getDefendingTroopSize(), neighbours));
        }

        AiRequest request = new AiRequest();
        request.setYourKey(yourKey);
        request.setReserveSize(gameState.getOpponents()[yourKey].getReserveSize());
        request.setLandCells(landCells);
        return request;
    }

    private static Set<Integer> neighbours(boolean[][] matrix, int cellKey) {
        Set<Integer> output = new HashSet<>();
        boolean[] row = matrix[cellKey];
        for (int neighbourKey = 0; neighbourKey < row.length; neighbourKey++) {
            if (row[neighbourKey] == true) {
                output.add(neighbourKey);
            }
        }
        return output;
    }


    private GameStateResponse generateGameStateResponse(String sessionKey) {
        // Find session
        validateSessionAccess(sessionKey);
        PlaySession session = sessionRepository.findSession(sessionKey);

        GameState theGameState = session.getGameState();
        GameMap gameMap = session.getGameMap();

        // Players
        Map<Integer, GameMap.Opponent> mapPlayers = (Map<Integer, GameMap.Opponent>) SerializationUtils.clone((Serializable) gameMap.getPlayers());
        GameState.Opponent[] gameStatePlayers = theGameState.getOpponents();
        for (int playerIndex = 0; playerIndex < gameStatePlayers.length; playerIndex++) {
            GameState.Opponent gameStatePlayer = gameStatePlayers[playerIndex];

            mapPlayers.get(playerIndex).setAlive(gameStatePlayer.isAlive());
            mapPlayers.get(playerIndex).setReserveSize(gameStatePlayer.getReserveSize());
        }

        // Cells
        Map<Integer, GameMap.MapCell> mapCells = (Map<Integer, GameMap.MapCell>) SerializationUtils.clone((Serializable) gameMap.getCells());
        for (int cellIndex = 0; cellIndex < theGameState.getCells().length; cellIndex++) {
            GameState.Cell gameStateCell = theGameState.getCells()[cellIndex];
            GameMap.MapCell mapCell = mapCells.get(cellIndex);
            mapCell.setArmySize(gameStateCell.getDefendingTroopSize());

            if (gameStateCell.isEmpty()) {
                mapCell.setPlayerKey(null);
            } else {
                mapCell.setPlayerKey(gameStateCell.getOccupierKey());
            }
        }

        // Generate response
        GameStateResponse response = new GameStateResponse();
        response.setCurrentTurn(session.getTurn());
        response.setPendingCount(session.pendingCount());
        response.setPlayerCount(session.playerCount());
        response.setSessionStatus(session.getStatus());
        response.setGameStatus(theGameState.getStatusCode());
        response.setWinnerKey(theGameState.getWinnerKey());
        response.setAlivePlayers(theGameState.getAlivePlayers());
        response.setCells(mapCells);
        response.setPlayers(mapPlayers);

        return response;
    }

    private void doTurnIfPossible(String sessionKey) {
        // Find session
        validateSessionAccess(sessionKey);
        PlaySession session = sessionRepository.findSession(sessionKey);

        if (session.isPending()) {
            return;
        }
        goToNextTurn(sessionKey);
    }

    private void goToNextTurn(String sessionKey) {
        // Find session
        validateSessionAccess(sessionKey);
        PlaySession session = sessionRepository.findSession(sessionKey);

        if (session.isPending()) {
            throw new PendingTurnSessionException(sessionKey);
        }
        session.eliminateAllIntention();
        session.incrementTurn();
    }

    private void validateSessionAccess(String sessionKey) {
        if (!sessionRepository.isSessionExistWithKey(sessionKey)) {
            throw new NotExistingInstanceSessionException(sessionKey);
        }
    }
}

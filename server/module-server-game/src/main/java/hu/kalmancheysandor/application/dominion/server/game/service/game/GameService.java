package hu.kalmancheysandor.application.dominion.server.game.service.game;


import hu.kalmancheysandor.application.dominion.api.ai.common.AiRequest;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiResponse;
import hu.kalmancheysandor.application.dominion.api.game.common.engine.GameEngine;
import hu.kalmancheysandor.application.dominion.api.game.common.engine.GameMap;
import hu.kalmancheysandor.application.dominion.api.game.common.engine.GameState;
import hu.kalmancheysandor.application.dominion.api.game.common.session.HumanPlayer;
import hu.kalmancheysandor.application.dominion.api.game.common.session.PlayerData;
import hu.kalmancheysandor.application.dominion.api.game.common.session.exception.NoMoreFreePlayerSlotSessionException;
import hu.kalmancheysandor.application.dominion.api.game.common.session.exception.NotExistingInstanceSessionException;
import hu.kalmancheysandor.application.dominion.api.game.common.session.exception.PendingTurnSessionException;
import hu.kalmancheysandor.application.dominion.server.game.proxy.AiPlayer1ServerProxy;
import hu.kalmancheysandor.application.dominion.server.game.repository.game.SessionRepository;
import hu.kalmancheysandor.application.dominion.api.game.common.session.PlaySession;
import hu.kalmancheysandor.application.dominion.server.game.service.game.dto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class GameService {

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private GameEngine gameEngine;

    //    @Autowired
//    private AiPlayer1ServerProxy proxy;
//
    @Autowired
    private AiPlayer1ServerProxy proxyAiPlayer1;

//    @Autowired
//    private AiPlayer2ServerProxy proxyAiPlayer2;


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
        session.saveIntention(playerIndex, action);


        // Ai calls after all human send their actions
        if (session.pendingCount() == session.getAiPlayerMaxSlotSize()) {
            for (Map.Entry<Integer, PlayerData> playerEntry : session.getPlayers().entrySet()) {
                int aiPlayerIndex = playerEntry.getKey();
                PlayerData  player = playerEntry.getValue();
                if(player.isArtificial()) {
                    AiResponse aiResponse = proxyAiPlayer1.generateResponse(createRequest(aiPlayerIndex, session.getGameState()));
                    System.out.println("AIIII(" + aiPlayerIndex + "):" + aiResponse);
                    GameEngine.Action aiAction = new GameEngine.Action(aiPlayerIndex, aiResponse.getTargetCellKey(), aiResponse.getTroopSize());
                    session.saveIntention(aiPlayerIndex, aiAction);
                }
            }
        }


        if (!session.isPending()) {

            GameState newState = gameEngine.doAction(session.getAllIntention(), session.getGameState());
            session.setGameState(newState);

            session.eliminateAllIntention();
            session.incrementTurn();
        }

        return generateGameStateResponse(sessionKey);
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

        GameMap gameMap = session.getGameMap();
        Map<Integer, GameMap.MapCell> mapCells = (Map<Integer, GameMap.MapCell>) SerializationUtils.clone((Serializable) gameMap.getCells());
        Map<Integer, GameMap.Opponent> mapPlayers = (Map<Integer, GameMap.Opponent>) SerializationUtils.clone((Serializable) gameMap.getPlayers());

        GameState theGameState = session.getGameState();

        GameState.Opponent[] gameStatePlayers = theGameState.getOpponents();

        //Players
        for (int playerIndex = 0; playerIndex < gameStatePlayers.length; playerIndex++) {
            GameState.Opponent gameStatePlayer = gameStatePlayers[playerIndex];

            mapPlayers.get(playerIndex).setAlive(gameStatePlayer.isAlive());
            mapPlayers.get(playerIndex).setReserveSize(gameStatePlayer.getReserveSize());
        }


        // cells
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
        response.setPlayerCount(session.playerCount());
        response.setPendingCount(session.pendingCount());
        response.setStatusCode(session.getStatus());
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

package hu.kalmancheysandor.application.dominion.server.game.service.game;


import hu.kalmancheysandor.application.dominion.api.ai.common.AiRequest;
import hu.kalmancheysandor.application.dominion.api.game.common.engine.GameMap;
import hu.kalmancheysandor.application.dominion.api.game.common.session.HumanPlayer;
import hu.kalmancheysandor.application.dominion.api.game.common.session.exception.NoMoreFreePlayerSlotSessionException;
import hu.kalmancheysandor.application.dominion.api.game.common.session.exception.NotExistingInstanceSessionException;
import hu.kalmancheysandor.application.dominion.api.game.common.session.exception.PendingTurnSessionException;
import hu.kalmancheysandor.application.dominion.server.game.repository.game.SessionRepository;
import hu.kalmancheysandor.application.dominion.api.game.common.session.PlaySession;
import hu.kalmancheysandor.application.dominion.server.game.service.game.dto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import java.io.*;
import java.util.Map;


@Service
public class GameService {

    @Autowired
    private SessionRepository sessionRepository;


//    @Autowired
//    private AiPlayer1ServerProxy proxy;
//
//    @Autowired
//    private AiPlayer1ServerProxy proxyAiPlayer1;
//
//    @Autowired
//    private AiPlayer2ServerProxy proxyAiPlayer2;


    public GameCreateResponse create(GameCreateRequest request) {
        PlaySession newSession = sessionRepository.createSession();
        return new GameCreateResponse(newSession.getSessionKey());
    }

    public GameJoinResponse join(String sessionKey, GameJoinRequest request) {
        // Find session
        validateSessionAccess(sessionKey);
        PlaySession session = sessionRepository.findSession(sessionKey);

        if (!session.isAnyFreePlayerSlotsAvailable()) {
            throw new NoMoreFreePlayerSlotSessionException(session.getSessionKey());
        }

        int playerIndex = session.nextAvailablePlayerIndex();
        HumanPlayer player = new HumanPlayer(playerIndex, request.getName());
        session.addPlayer(player);

        return new GameJoinResponse(playerIndex, "YourSecretKey");
    }


    public GameStateResponse currentState(String sessionKey) {
        validateSessionAccess(sessionKey);
        return generateGameStateResponse(sessionKey);
    }

    public GameStateResponse doStep(String sessionKey, GameStepRequest request) {
        int playerId = request.getPlayerId();

        // Find session
        validateSessionAccess(sessionKey);
        PlaySession session = sessionRepository.findSession(sessionKey);

        // Save intention
        session.saveIntention(playerId, request.getValue());

        // Calculations
        doTurnIfPossible(sessionKey);

        return generateGameStateResponse(sessionKey);
    }

    private GameStateResponse generateGameStateResponse(String sessionKey) {
        // Find session
        validateSessionAccess(sessionKey);
        PlaySession session = sessionRepository.findSession(sessionKey);

        // Determine status code
        GameStateResponse.StatusCode statusCode = GameStateResponse.StatusCode.RECRUITING;
        if (session.getStatus() == PlaySession.Status.PLAYING) {
            statusCode = GameStateResponse.StatusCode.PLAYING;
        } else if (session.getStatus() == PlaySession.Status.ENDED) {
            statusCode = GameStateResponse.StatusCode.ENDED;
        }

        GameMap gameMap = session.getGameMap();
        Map<Integer, GameMap.MapCell> cells = (Map<Integer, GameMap.MapCell>) SerializationUtils.clone((Serializable) gameMap.getCells());
        Map<Integer, GameMap.Opponent> players = (Map<Integer, GameMap.Opponent>) SerializationUtils.clone((Serializable) gameMap.getPlayers());


        // Generate response
        GameStateResponse response = new GameStateResponse();
        response.setCurrentTurn(session.getTurn());
        response.setPlayerCount(session.playerCount());
        response.setPendingCount(session.pendingCount());
        response.setStatusCode(statusCode);
        response.setCells(cells);
        response.setPlayers(players);
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

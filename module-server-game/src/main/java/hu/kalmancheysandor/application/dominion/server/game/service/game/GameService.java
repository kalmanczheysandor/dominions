package hu.kalmancheysandor.application.dominion.server.game.service.game;


import hu.kalmancheysandor.application.dominion.server.game.exception.NotExistingSession;
import hu.kalmancheysandor.application.dominion.server.game.exception.PendingTurnSessionException;
import hu.kalmancheysandor.application.dominion.server.game.proxy.AiPlayer1ServerProxy;
import hu.kalmancheysandor.application.dominion.server.game.proxy.AiPlayer2ServerProxy;
import hu.kalmancheysandor.application.dominion.server.game.repository.game.GameRepository;
import hu.kalmancheysandor.application.dominion.server.game.repository.game.PlaySession;
import hu.kalmancheysandor.application.dominion.server.game.service.game.dto.GameStepRequest;
import hu.kalmancheysandor.application.dominion.server.game.service.game.dto.GameStateResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private AiPlayer1ServerProxy proxy;

    @Autowired
    private AiPlayer1ServerProxy proxyAiPlayer1;

    @Autowired
    private AiPlayer2ServerProxy proxyAiPlayer2;

    public GameStateResponse currentState(String sessionKey) {
        validateSessionKeyAccess(sessionKey);
        PlaySession session = gameRepository.findSession(sessionKey);

        return generateGameStateResponse(sessionKey);
    }

    public GameStateResponse doStep(String sessionKey, GameStepRequest gameStepRequest) {
        int playerId = gameStepRequest.getPlayerId();

        // Find session
        validateSessionKeyAccess(sessionKey);
        PlaySession session = gameRepository.findSession(sessionKey);

        // Save intention
        session.saveIntention(playerId, gameStepRequest.getValue());

        // Calculations
        doTurnIfPossible(sessionKey);

        return generateGameStateResponse(sessionKey);
    }






    private GameStateResponse generateGameStateResponse(String sessionKey) {
        // Find session
        validateSessionKeyAccess(sessionKey);
        PlaySession session = gameRepository.findSession(sessionKey);

        return new GameStateResponse(session.getTurn(), session.pendingCount(), session.playerCount());
    }

    private void doTurnIfPossible(String sessionKey) {
        // Find session
        validateSessionKeyAccess(sessionKey);
        PlaySession session = gameRepository.findSession(sessionKey);

        if (session.isPending()) {
            return;
        }
        goToNextTurn(sessionKey);
    }

    private void goToNextTurn(String sessionKey) {
        // Find session
        validateSessionKeyAccess(sessionKey);
        PlaySession session = gameRepository.findSession(sessionKey);

        if (session.isPending()) {
            throw new PendingTurnSessionException(sessionKey);
        }
        session.eliminateAllIntention();
        session.incrementTurn();
    }

    private void validateSessionKeyAccess(String sessionKey) {
        if (!gameRepository.isSessionExistWithKey(sessionKey)) {
            throw new NotExistingSession(sessionKey);
        }
    }


}

package hu.kalmancheysandor.applications.dominion.server.web.service.play;

import hu.kalmancheysandor.application.dominion.server.game.service.game.dto.GameJoinRequest;
import hu.kalmancheysandor.application.dominion.server.game.service.game.dto.GameJoinResponse;
import hu.kalmancheysandor.application.dominion.server.game.service.game.dto.GameStateResponse;
import hu.kalmancheysandor.application.dominion.server.game.service.game.dto.GameStepRequest;
import hu.kalmancheysandor.applications.dominion.server.web.proxy.game.GameServerProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class PlayService {
    @Autowired
    private GameServerProxy gameServerProxy;

    public GameJoinResponse join(String sessionKey, GameJoinRequest request ) {
        return gameServerProxy.join(sessionKey,request);
    }

    public GameStateResponse currentGameState(String sessionKey) {
        return gameServerProxy.currentState(sessionKey);
    }


    public GameStateResponse action(String sessionKey, int playerIndex, GameStepRequest request ) {
        return gameServerProxy.action(sessionKey, playerIndex, request);
    }
}

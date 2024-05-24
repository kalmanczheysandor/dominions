package hu.kalmancheysandor.applications.dominion.server.web.service.play;

import hu.kalmancheysandor.application.dominion.server.game.service.game.dto.GameStateResponse;
import hu.kalmancheysandor.applications.dominion.server.web.proxy.game.GameServerProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayService {
    @Autowired
    private GameServerProxy gameServerProxy;


    public GameStateResponse currentGameState(String sessionKey) {
        return gameServerProxy.currentState(sessionKey);
    }
}

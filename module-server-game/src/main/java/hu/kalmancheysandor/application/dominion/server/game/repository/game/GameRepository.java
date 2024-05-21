package hu.kalmancheysandor.application.dominion.server.game.repository.game;




import hu.kalmancheysandor.application.dominion.server.game.exception.DuplicateGamePlaySessionException;
import hu.kalmancheysandor.application.dominion.server.game.exception.DuplicatePlayerInstanceSessionException;
import hu.kalmancheysandor.application.dominion.server.game.exception.NotExistingSession;
import org.springframework.stereotype.Repository;


import java.util.*;

@Repository
public class GameRepository {

    private static Map<String, PlaySession> sessions = new HashMap<>();

    public GameRepository() {
        PlaySession.PlayerData player1 = new PlaySession.PlayerData(1);
        PlaySession.PlayerData player2 = new PlaySession.PlayerData(2);

        PlaySession session = new PlaySession("aa1");
        session.addPlayer(player1);
        session.addPlayer(player2);

        sessions.put("aa1", session);
    }


    public void addSession(PlaySession newSession) {
        if (sessions.containsKey(newSession.getSessionKey())) {
            throw new DuplicateGamePlaySessionException(newSession.getSessionKey());
        }
        sessions.put(newSession.getSessionKey(), newSession);
    }

    public PlaySession findSession(String key) {
        if (!sessions.containsKey(key)) {
            throw new NotExistingSession(key);
        }
        return sessions.get(key);
    }

    public boolean isSessionExistWithKey(String key) {
        return sessions.containsKey(key);
    }


}

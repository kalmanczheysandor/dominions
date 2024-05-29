package hu.kalmancheysandor.application.dominion.server.game.repository.game;


import hu.kalmancheysandor.application.dominion.api.game.common.engine.GameEngine;
import hu.kalmancheysandor.application.dominion.api.game.common.engine.GameMap;
import hu.kalmancheysandor.application.dominion.api.game.common.session.HumanPlayer;
import hu.kalmancheysandor.application.dominion.api.game.common.session.PlaySession;
import hu.kalmancheysandor.application.dominion.api.game.common.session.PlayerData;
import hu.kalmancheysandor.application.dominion.api.game.common.session.exception.DuplicateGamePlaySessionException;
import hu.kalmancheysandor.application.dominion.api.game.common.session.exception.NotExistingInstanceSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.*;

@Repository
public class SessionRepository {



    private static final String SESSION_KEY_PREFIX = "AAA";
    private static Map<String, PlaySession> sessions = new HashMap<>();


    public SessionRepository() {
//        PlayerData player1 = new HumanPlayer(1);
//        PlayerData player2 = new HumanPlayer(2);
//
//        PlaySession session = new PlaySession("aa1");
//        session.addPlayer(player1);
//        session.addPlayer(player2);
//
//        sessions.put("aa1", session);
    }

    public List<PlaySession> list() {
        return new ArrayList<>(sessions.values());
    }

    public synchronized PlaySession createSession() {
        String sessionKey = SESSION_KEY_PREFIX + "-" + (sessions.size() + 1);

        GameMap map = GameMap.open("map3-a.json");
        PlaySession session = new PlaySession(sessionKey, map);

        addSession(session);

        return session;
    }

    public PlaySession findSession(String key) {
        if (!sessions.containsKey(key)) {
            throw new NotExistingInstanceSessionException(key);
        }
        return sessions.get(key);
    }

    public boolean isSessionExistWithKey(String key) {
        return sessions.containsKey(key);
    }


    public void addPlayerToSession(String sessionKey, HumanPlayer player) {
        if (!isSessionExistWithKey(sessionKey)) {
            throw new NotExistingInstanceSessionException(sessionKey);
        }
        PlaySession session = findSession(sessionKey);
        session.addHumanPlayer(player);
    }


    private synchronized void addSession(PlaySession newSession) {
        if (sessions.containsKey(newSession.getSessionKey())) {
            throw new DuplicateGamePlaySessionException(newSession.getSessionKey());
        }
        sessions.put(newSession.getSessionKey(), newSession);
    }
}

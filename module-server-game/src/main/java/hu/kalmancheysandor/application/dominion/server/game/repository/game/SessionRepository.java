package hu.kalmancheysandor.application.dominion.server.game.repository.game;


import hu.kalmancheysandor.application.dominion.api.game.common.session.PlaySession;
import hu.kalmancheysandor.application.dominion.api.game.common.session.exception.DuplicateGamePlaySessionException;
import hu.kalmancheysandor.application.dominion.api.game.common.session.exception.NotExistingSession;
import org.springframework.stereotype.Repository;


import java.util.*;

@Repository
public class SessionRepository {

    private static Map<String, PlaySession> sessions = new HashMap<>();

    public SessionRepository() {
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


    public void loadFile() {

//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            // Path to your JSON file
//            File jsonFile = new File("path/to/your/file.json");
//            // Convert JSON file to Object
//            Person person = objectMapper.readValue(jsonFile, Person.class);
//            // Output the person object
//            System.out.println("Name: " + person.getName());
//            System.out.println("Age: " + person.getAge());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}

package hu.kalmancheysandor.application.dominion.server.game.repository;


import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class GameRepository {

    private static Map<String, GamePlay> gamePlays = new HashMap<>();;


    public static class GamePlay {

    }
}

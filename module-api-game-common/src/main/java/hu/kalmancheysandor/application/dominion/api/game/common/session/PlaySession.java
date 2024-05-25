package hu.kalmancheysandor.application.dominion.api.game.common.session;

import hu.kalmancheysandor.application.dominion.api.game.common.engine.GameEngine;
import hu.kalmancheysandor.application.dominion.api.game.common.engine.GameMap;
import hu.kalmancheysandor.application.dominion.api.game.common.engine.GameState;
import hu.kalmancheysandor.application.dominion.api.game.common.engine.exception.GeneralGameException;
import hu.kalmancheysandor.application.dominion.api.game.common.session.exception.DuplicatePlayerInstanceSessionException;
import hu.kalmancheysandor.application.dominion.api.game.common.session.exception.IntentionIsAlreadyGivenException;
import hu.kalmancheysandor.application.dominion.api.game.common.session.exception.NoMoreFreePlayerSlotSessionException;
import hu.kalmancheysandor.application.dominion.api.game.common.session.exception.NotExistingPlayerSessionException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.SerializationUtils;


import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class PlaySession {
    @Getter
    private final String sessionKey;
    private final int maxPlayerSize;
    @Getter
    @Setter
    private int turn = 0;
    private Map<Integer, PlayerData> players = new HashMap<>();

    @Getter @Setter
    private GameState gameState = null;
    @Getter
    private GameMap gameMap;
    @Getter
    private SessionStatusCode status;

    //private final GameEngine gameEngine;

    public PlaySession(String sessionKey, GameMap gameMap) {
        this.sessionKey = sessionKey;
        this.gameMap = gameMap;
        this.maxPlayerSize = gameMap.playerCount();
        this.status = SessionStatusCode.RECRUITING;
        //this.gameEngine = gameEngine;
    }

    public PlayerData findPlayer(int playerId) {
        if (!players.containsKey(playerId)) {
            throw new NotExistingPlayerSessionException(sessionKey, playerId);
        }
        return players.get(playerId);
    }

    public boolean isPlayerExistWithId(int id) {
        return players.containsKey(id);
    }

    public void addPlayer(PlayerData data) {
        if (players.containsKey(data.getIndex())) {
            throw new DuplicatePlayerInstanceSessionException(sessionKey, data.getIndex());
        }
        players.put(data.getIndex(), data);

        // After the final player is added
        if (!isAnyFreePlayerSlotsAvailable()) {
            start();
        }
    }


    private void start() {
        this.status = SessionStatusCode.PLAYING;
        this.gameState = new GameState(gameMap);
    }


    public int freePlayerSlotsCount() {
        return maxPlayerSize - players.size();
    }

    public boolean isAnyFreePlayerSlotsAvailable() {
        return freePlayerSlotsCount() > 0;
    }

    public int nextAvailablePlayerIndex() {
        if (!isAnyFreePlayerSlotsAvailable()) {
            throw new NoMoreFreePlayerSlotSessionException(sessionKey);
        }
        return players.size();
    }


    public void saveIntention(int playerIndex, GameEngine.Action intention) {
        System.out.println("PlayerIndex at save intention: " + playerIndex);
        if (!players.containsKey(playerIndex)) {
            throw new NotExistingPlayerSessionException(sessionKey, playerIndex);
        }
        if (players.get(playerIndex).isIntentionAlreadyGiven()) {
            throw new IntentionIsAlreadyGivenException(sessionKey);
        }
        players.get(playerIndex).setIntention(intention);
        players.get(playerIndex).setIntentionGiven(true);
    }


    public Set<GameEngine.Action> getAllIntention() {
        Set<GameEngine.Action> intentions = new HashSet<>();

        for (PlayerData player : players.values()) {
            if (!player.isIntentionAlreadyGiven()) {
                throw new GeneralGameException("No intention is present for player! Player index:"+player.getIndex());
            }
            intentions.add(player.getIntention());
        }
        return intentions;
    }


    public boolean isPending() {
        for (PlayerData player : players.values()) {
            if (!player.isIntentionAlreadyGiven()) {
                return true;
            }
        }
        return false;
    }

    public int pendingCount() {
        int count = 0;
        for (PlayerData player : players.values()) {
            if (!player.isIntentionAlreadyGiven()) {
                count++;
            }
        }
        return count;
    }

    public int playerCount() {
        return players.size();
    }

    public void eliminateAllIntention() {
        for (PlayerData player : players.values()) {
            player.eliminateIntention();
        }
    }

    public int incrementTurn() {
        return ++turn;
    }


//    public GameMap getCurrentStateAsGameMap() {
//        GameMap newGameMap = (GameMap) SerializationUtils.clone((Serializable) getGameMap());
//
//        Map<Integer, GameMap.MapCell> mapCells = (Map<Integer, GameMap.MapCell>) SerializationUtils.clone((Serializable) gameMap.getCells());
//Map<Integer, GameMap.Opponent> mapPlayers = (Map<Integer, GameMap.Opponent>) SerializationUtils.clone((Serializable) gameMap.getPlayers());
//
//        GameState gameState = getGameState();
//
//        // Players
//        for(int playerIndex=0;playerIndex<gameState.getOpponents().length;playerIndex++) {
//            GameState.Opponent gameStatePlayer = gameState.getOpponents()[playerIndex];
//            GameMap.Opponent mapPlayer = mapPlayers.get(playerIndex);
//            mapPlayer.setAlive(gameStatePlayer.isAlive());
//            mapPlayer.setReserveSize(gameStatePlayer.getReserveSize());
//        }
//
//
//        // cells
//        for(int cellIndex=0;cellIndex<gameState.getCells().length;cellIndex++) {
//            GameState.Cell gameStateCell = gameState.getCells()[cellIndex];
//            GameMap.MapCell mapCell = mapCells.get(cellIndex);
//            mapCell.setArmySize(gameStateCell.getDefendingTroopSize());
//
//            if(gameStateCell.isEmpty()) {
//                mapCell.setPlayerKey(null);
//            }
//            else {
//                mapCell.setPlayerKey(gameStateCell.getOccupierKey();
//            }
//        }
//    }
//


//    public GameMap getCurrentStateAsGameMap() {
//        System.out.println("GameMap:"+this.gameMap);
//        GameMap newGameMap = SerializationUtils.clone(this.gameMap);
//
//        Map<Integer, GameMap.MapCell> mapCells = newGameMap.getCells();
//        Map<Integer, GameMap.Opponent> mapPlayers = newGameMap.getPlayers();
//
//        GameState gameState = getGameState();
//
//        // Players
//        for (int playerIndex = 0; playerIndex < gameState.getOpponents().length; playerIndex++) {
//            GameState.Opponent gameStatePlayer = gameState.getOpponents()[playerIndex];
//            GameMap.Opponent mapPlayer = mapPlayers.get(playerIndex);
//            mapPlayer.setAlive(gameStatePlayer.isAlive());
//            mapPlayer.setReserveSize(gameStatePlayer.getReserveSize());
//        }
//
//
//        // cells
//        for (int cellIndex = 0; cellIndex < gameState.getCells().length; cellIndex++) {
//            GameState.Cell gameStateCell = gameState.getCells()[cellIndex];
//            GameMap.MapCell mapCell = mapCells.get(cellIndex);
//            mapCell.setArmySize(gameStateCell.getDefendingTroopSize());
//
//            if (gameStateCell.isEmpty()) {
//                mapCell.setPlayerKey(null);
//            } else {
//                mapCell.setPlayerKey(gameStateCell.getOccupierKey());
//            }
//        }
//        return newGameMap;
//
//    }
}

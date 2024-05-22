package hu.kalmancheysandor.application.dominion.api.game.common.session;

import hu.kalmancheysandor.application.dominion.api.game.common.engine.GameEngine;
import hu.kalmancheysandor.application.dominion.api.game.common.engine.GameMap;
import hu.kalmancheysandor.application.dominion.api.game.common.engine.GameState;
import hu.kalmancheysandor.application.dominion.api.game.common.session.exception.DuplicatePlayerInstanceSessionException;
import hu.kalmancheysandor.application.dominion.api.game.common.session.exception.IntentionIsAlreadyGivenException;
import hu.kalmancheysandor.application.dominion.api.game.common.session.exception.NoMoreFreePlayerSlotSessionException;
import hu.kalmancheysandor.application.dominion.api.game.common.session.exception.NotExistingPlayerSessionException;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;


public class PlaySession {
    @Getter
    private final String sessionKey;
    private final int maxPlayerSize;
    @Getter
    @Setter
    private int turn = 0;
    private Map<Integer, PlayerData> players = new HashMap<>();

    private GameState gameState = null;
    private GameMap gameMap = null;
    @Getter
    private Status status;

    private final GameEngine gameEngine;

    public PlaySession(String sessionKey, GameMap gameMap, GameEngine gameEngine) {
        this.sessionKey = sessionKey;
        this.gameMap = gameMap;
        this.maxPlayerSize = gameMap.playerCount();
        this.status = Status.RECRUITING;
        this.gameEngine = gameEngine;
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
        this.status = Status.PLAYING;
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
        return players.size() + 1;
    }


    public void saveIntention(int playerId, String intention) {
        if (!players.containsKey(playerId)) {
            throw new NotExistingPlayerSessionException(sessionKey, playerId);
        }
        if (players.get(playerId).isIntentionAlreadyGiven()) {
            throw new IntentionIsAlreadyGivenException(sessionKey);
        }
        players.get(playerId).setIntention(intention);
        players.get(playerId).setIntentionGiven(true);
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

    public enum Status {
        RECRUITING,
        PLAYING,
        ENDED
    }

}

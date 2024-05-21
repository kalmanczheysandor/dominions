package hu.kalmancheysandor.application.dominion.server.game.repository.game;

import hu.kalmancheysandor.application.dominion.server.game.exception.IntentionIsAlreadyGivenException;
import hu.kalmancheysandor.application.dominion.server.game.exception.DuplicatePlayerInstanceSessionException;
import hu.kalmancheysandor.application.dominion.server.game.exception.NotExistingPlayerSessionException;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PlaySession {
    @Getter
    private final String sessionKey;
    @Getter
    @Setter
    private int turn = 0;
    private Map<Integer, PlayerData> players = new HashMap<>();

    public PlaySession(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public PlayerData findPlayer(int playerId) {
        if (!players.containsKey(playerId)) {
            throw new NotExistingPlayerSessionException(playerId);
        }
        return players.get(playerId);
    }

    public boolean isPlayerExistWithId(int id) {
        return players.containsKey(id);
    }

    public void addPlayer(PlayerData data) {
        if (players.containsKey(data.getId())) {
            throw new DuplicatePlayerInstanceSessionException(data.getId());
        }
        players.put(data.getId(), data);
    }

    public void saveIntention(int playerId, String intention) {
        if (!players.containsKey(playerId)) {
            throw new NotExistingPlayerSessionException(playerId);
        }
        if (players.get(playerId).isIntentionAlreadyGiven()) {
            throw new IntentionIsAlreadyGivenException();
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



    public static class PlayerData {
        @Getter
        private final int id;

        @Getter
        @Setter
        private String intention = null;
        @Setter
        private boolean intentionGiven = false;

        public PlayerData(int id) {
            this.id = id;
        }

        public boolean isIntentionAlreadyGiven() {
            return intentionGiven;
        }

        public void eliminateIntention() {
            intentionGiven = false;
            intention = null;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PlayerData that = (PlayerData) o;
            return id == that.id;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id);
        }
    }

}

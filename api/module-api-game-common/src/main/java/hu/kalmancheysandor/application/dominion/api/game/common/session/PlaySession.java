package hu.kalmancheysandor.application.dominion.api.game.common.session;

import hu.kalmancheysandor.application.dominion.api.game.common.engine.GameEngine;
import hu.kalmancheysandor.application.dominion.api.game.common.engine.GameMap;
import hu.kalmancheysandor.application.dominion.api.game.common.engine.GameState;
import hu.kalmancheysandor.application.dominion.api.game.common.engine.exception.GeneralGameException;
import hu.kalmancheysandor.application.dominion.api.game.common.session.exception.*;
import lombok.Getter;
import lombok.Setter;


import java.util.*;


public class PlaySession {
    @Getter
    private final String sessionKey;
    private final int playerSlotSize;
    @Getter
    @Setter
    private int turn = 0;
    @Getter
    private Map<Integer, PlayerData> players = new HashMap<>();

    @Getter
    @Setter
    private GameState gameState = null;
    @Getter
    private GameMap gameMap;
    @Getter
    private SessionStatusCode status;

    @Getter
    private int humanPlayerMaxSlotSize = 0;
    @Getter
    private int aiPlayerMaxSlotSize = 0;
    private Set<Integer> humanPlayerSlot = new LinkedHashSet<>();
    private Set<Integer> aiPlayerSlot = new LinkedHashSet<>();

    // private final GameEngine gameEngine;

    public PlaySession(String sessionKey, GameMap gameMap) {
        this.sessionKey = sessionKey;
        this.gameMap = gameMap;
        this.playerSlotSize = gameMap.playerCount();

        this.status = SessionStatusCode.RECRUITING;
        //this.gameEngine = gameEngine;

        for (Map.Entry<Integer, GameMap.Opponent> playerEntry : gameMap.getPlayers().entrySet()) {
            if (playerEntry.getValue().getType() == GameMap.Opponent.PlayerType.HUMAN) {
                humanPlayerSlot.add(playerEntry.getKey());
                this.humanPlayerMaxSlotSize++;
            } else {
                aiPlayerSlot.add(playerEntry.getKey());
                this.aiPlayerMaxSlotSize++;
            }
        }
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

    public void addHumanPlayer(HumanPlayer player) {
        addPlayer(player);

        // After the final player is added
        if (!isAnyFreeHumanPlayerSlotsAvailable()) {
            start();
        }
    }

    private void addPlayer(PlayerData data) {
        int playerIndex =data.getIndex();
        if (players.containsKey(playerIndex)) {
            throw new PlayerKeyAlreadyIssuedSessionException(sessionKey, playerIndex);
        }

        if(data.getPlayerType()== PlayerData.PlayerType.HUMAN) {
            if(!humanPlayerSlot.contains(playerIndex)) {
                throw new PlayerKeyNotNotMemberOfHumanPlayerSlotSessionException(sessionKey, playerIndex);
            }
            humanPlayerSlot.remove(data.getIndex());
        }
        else {
            if(!aiPlayerSlot.contains(playerIndex)) {
                throw new PlayerKeyNotNotMemberOfAiPlayerSlotSessionException(sessionKey, playerIndex);
            }
            aiPlayerSlot.remove(playerIndex);
        }
        players.put(data.getIndex(), data);
    }

    public int nextAvailableHumanPlayerIndex() {
        if (!isAnyFreeHumanPlayerSlotsAvailable()) {
            throw new NoMoreFreePlayerSlotSessionException(sessionKey);
        }

        Iterator<Integer> iterator = humanPlayerSlot.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        throw new NoMoreFreePlayerSlotSessionException(sessionKey);
    }

    private void addMissingAiPlayers() {
        for(int aiIndex:aiPlayerSlot) {
            ArtificialPlayer player =new ArtificialPlayer(aiIndex,"Mr AI-"+aiIndex);
            addPlayer(player);
        }
    }

    private void start() {
        addMissingAiPlayers();

        this.status = SessionStatusCode.PLAYING;
        this.gameState = new GameState(gameMap);
    }

    public int freeHumanPlayerSlotCount() {
        return humanPlayerSlot.size();
    }

    public int freeAiPlayerSlotCount() {
        return aiPlayerSlot.size();
    }

    public boolean isAnyFreeHumanPlayerSlotsAvailable() {
        return freeHumanPlayerSlotCount() > 0;
    }

    public boolean isAnyFreeAiPlayerSlotsAvailable() {
        return freeAiPlayerSlotCount() > 0;
    }


//    public int nextAvailablePlayerIndex() {
//        if (!isAnyFreeHumanPlayerSlotsAvailable()||!isAnyFreeAiPlayerSlotsAvailable()) {
//            throw new NoMoreFreePlayerSlotSessionException(sessionKey);
//        }
//        return players.size();
//    }


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
                throw new GeneralGameException("No intention is present for player! Player index:" + player.getIndex());
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
}

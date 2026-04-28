package hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration;

import hu.kalmancheysandor.applications.dominions.apis.game.common.map.*;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.configuration.GameMapConfigurationPlayer;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.action.GameAction;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.machine.GameStateMachine;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.exception.*;
import hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.player.ArtificialPlayer;
import hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.player.HumanPlayer;
import hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.player.PlayerData;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStateStatusCode;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.exception.GeneralGameStateException;
import hu.kalmancheysandor.applications.dominions.apis.general.exceptions.IllegalPointOfExecution;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.*;


public class PlayOrchestrator {

    @Getter
    private PlayState playState;

    @Getter
    private String sessionKey;

    @Getter
    private GameMap gameMap;

    private GameStateMachine gameStateMachine;


    public PlayOrchestrator() {
    }

    public static PlayOrchestrator create(String sessionKey, GameMap gameMap, PlayState playState) {
        PlayOrchestrator playOrchestrator = new PlayOrchestrator();
        playOrchestrator.load(sessionKey, gameMap, playState);
        return playOrchestrator;
    }

    public int getAiPlayerMaxSlotSize() {
        return this.playState.getMaxAiPlayerSlotCount();
    }


    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////

    private void load(String sessionKey, GameMap gameMap, PlayState playState) {
        this.sessionKey = sessionKey;
        this.gameMap = gameMap;
        this.playState = playState;
    }

    public PlayerData findPlayer(int playerId) {
        if (!this.playState.players.containsKey(playerId)) {
            throw new NotExistingPlayerSessionException(sessionKey, playerId);
        }
        return this.playState.players.get(playerId);
    }

    public boolean isPlayerExistWithId(int id) {
        return this.playState.players.containsKey(id);
    }

    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////
    /// ADD  PLAYER //////////////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////

    public HumanPlayer addNewHumanPlayer(String userUuid, String playerName, String endpointKey) {
        // StateEngine: Check whether any free slots remained
        if (!this.isAnyFreeHumanPlayerSlotsAvailable()) {
            throw new NoMoreFreePlayerSlotSessionException(this.sessionKey);
        }

        // StateEngine: Add a new human player
        int playerIndex = this.nextAvailableHumanPlayerIndex();
        HumanPlayer player = new HumanPlayer(playerIndex, userUuid, playerName, endpointKey);
        this.addHumanPlayer(player);

        return player;
    }

    private void addHumanPlayer(HumanPlayer player) {
        addPlayer(player);
    }

    private void addPlayer(PlayerData data) {

        int playerIndex = data.getIndex();
        String playerEndpointKey = data.getEndpointKey();
        String userUuid = data.getUserUuid();

        // Checking: Whether any player is already reserved a slot with the index
        if (this.playState.players.containsKey(playerIndex)) {
            throw new PlayerKeyAlreadyIssuedSessionException(sessionKey, playerIndex);
        }

        // Checking: Whether the frontend has already registered
        if (isPlayerEndpointKeyAlreadyRegistered(playerEndpointKey)) {
            throw new PlayerEndpointKeyAlreadyRegisteredSessionException(sessionKey, playerEndpointKey);
        }

        // Checking: Whether the user already registered
        if (isUserUuidAlreadyRegistered(userUuid)) {
            throw new UserUuidAlreadyRegisteredSessionException(sessionKey, userUuid);
        }

        if (data.getPlayerType() == GameMapPlayerType.HUMAN) {

            if (!this.playState.humanPlayerSlot.contains(playerIndex)) {
                throw new PlayerKeyNotMemberOfHumanPlayerSlotSessionException(sessionKey, playerIndex);
            }
            this.playState.humanPlayerSlot.remove(data.getIndex());
        } else {
            if (!this.playState.aiPlayerSlot.contains(playerIndex)) {
                throw new PlayerKeyNotNotMemberOfAiPlayerSlotSessionException(sessionKey, playerIndex);
            }
            this.playState.aiPlayerSlot.remove(playerIndex);
        }
        this.playState.players.put(data.getIndex(), data);
    }

    public int nextAvailableHumanPlayerIndex() {
        if (!isAnyFreeHumanPlayerSlotsAvailable()) {
            throw new NoMoreFreePlayerSlotSessionException(sessionKey);
        }

        Iterator<Integer> iterator = this.playState.humanPlayerSlot.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        throw new NoMoreFreePlayerSlotSessionException(sessionKey);
    }

    private void addMissingAiPlayers() {
        for (int aiPlayerIndex : new HashSet<>(this.playState.aiPlayerSlot)) {
            ArtificialPlayer player = null;
            GameMapConfigurationPlayer gameMapConfigurationPlayer = gameMap.getConfiguration().getPlayers().get(aiPlayerIndex);


            if (gameMapConfigurationPlayer.getEngine() == GameMapEngineType.AI_LIZ) {
                player = new ArtificialPlayer(aiPlayerIndex, "AI-LIZ-UserUuid"+aiPlayerIndex, "AiLiz["+ aiPlayerIndex+"]:" +gameMapConfigurationPlayer.getCharacter(), GameMapEngineType.AI_LIZ, gameMapConfigurationPlayer.getCharacter(), "AI-LIZ-EndpointKey-" + aiPlayerIndex);
            } else if (gameMapConfigurationPlayer.getEngine() == GameMapEngineType.AI_HUGO) {
                player = new ArtificialPlayer(aiPlayerIndex, "AI-HUGO-UserUuid"+aiPlayerIndex, "AiHugo["+ aiPlayerIndex+"]:" +gameMapConfigurationPlayer.getCharacter(), GameMapEngineType.AI_HUGO, gameMapConfigurationPlayer.getCharacter(), "AI-HUGO-EndpointKey-" + aiPlayerIndex);
            }  else {
                throw new IllegalPointOfExecution("Unknown ai engine type is found:" + gameMapConfigurationPlayer.getEngine().name());
            }

            if (player != null) {
                addPlayer(player);
            }
        }
    }


    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////
    ///  Intention Methods  ////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////

    public void saveMultipleIntention(Map<Integer, GameAction> intentions) {
        for (Map.Entry<Integer, GameAction> entry : intentions.entrySet()) {
            saveIntention(entry.getKey(), entry.getValue());
        }
    }

    public void saveIntention(int playerIndex, GameAction intention) {
        System.out.println("PlayerIndex at save intention: " + playerIndex);
        if (!this.playState.players.containsKey(playerIndex)) {
            throw new NotExistingPlayerSessionException(sessionKey, playerIndex);
        }
        if (this.playState.players.get(playerIndex).isIntentionAlreadyGiven()) {
            throw new IntentionIsAlreadyGivenException(sessionKey);
        }
        System.out.println("Intention saved: " + intention);

        this.playState.players.get(playerIndex).setIntention(intention);
        this.playState.players.get(playerIndex).setIntentionGiven(true);
    }

    public Set<GameAction> getAllIntention() {
        Set<GameAction> intentions = new HashSet<>();
        for (PlayerData player : this.playState.players.values()) {
            int playerIndex = player.getIndex();

            if (this.playState.getGameState().getOpponent(playerIndex).isAlive()) {
                if (!player.isIntentionAlreadyGiven()) {
                    throw new GeneralGameStateException("No intention is present for player! Player index:" + player.getIndex());
                }
                intentions.add(player.getIntention());
            }
        }
        return intentions;
    }

    public boolean isAnyMissingIntention() {
        return missingIntentionCount() > 0;
    }

    public int missingIntentionCount() {
        return missingIntentionCountWhen(null);
    }

    private int missingIntentionCountWhen(Boolean isArtificial) {
        int count = 0;
        for (PlayerData player : this.playState.players.values()) {
            int playerIndex = player.getIndex();

            if (!player.isIntentionAlreadyGiven() && this.playState.getGameState().getOpponent(playerIndex).isAlive()) {
                if (isArtificial == null) {
                    count++;
                } else if (isArtificial == true && player.isArtificial()) {
                    count++;
                } else if (isArtificial == false && !player.isArtificial()) {
                    count++;
                }
            }
        }
        return count;
    }

    public int humanMissingIntentionCount() {
        return missingIntentionCountWhen(false);
    }

    public int aiMissingIntentionCount() {
        return missingIntentionCountWhen(true);
    }

    public void flushAllIntention() {
        for (PlayerData player : this.playState.players.values()) {
            player.flushIntention();
        }
    }


    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////
    ///  Slot Methods  ////////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public int freeHumanPlayerSlotCount() {
        return this.playState.humanPlayerSlot.size();
    }

    public int freeAiPlayerSlotCount() {
        return this.playState.aiPlayerSlot.size();
    }

    public boolean isAnyFreeHumanPlayerSlotsAvailable() {
        return freeHumanPlayerSlotCount() > 0;
    }

    public boolean isAnyFreeAiPlayerSlotsAvailable() {
        return freeAiPlayerSlotCount() > 0;
    }


    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////
    /// ACTIONS /////////////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public void process() {
        // Stop recruiting when all players are in
        if (PlayStateStatusCode.RECRUITING == this.playState.status) {
            if (!isAnyFreeHumanPlayerSlotsAvailable()) {
                addMissingAiPlayers();
                this.playState.status = PlayStateStatusCode.PLAYING;
            }
        }

        // Attempt to collide
        if (isInPlayingStatus() && !isAnyMissingIntention()) {

            // Initialise state-machine
            if (this.gameStateMachine == null) {
                this.gameStateMachine = new GameStateMachine();
            }

            // Use state machine to create next game-state
            GameState newState = gameStateMachine.doSteps(getAllIntention(), getGameState());
            setGameState(newState);
            if (GameStateStatusCode.FINISHED == newState.getStatusCode()) {
                setStatusCode(PlayStateStatusCode.ENDED);
            }

            //
            flushAllIntention();
            if (PlayStateStatusCode.PLAYING == this.playState.status) {
                incrementTurn();
            }
        }


    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///  Helper methods   /////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////


    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////
    ///  ETC   /////////////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////


    public boolean isInPlayingStatus() {
        return PlayStateStatusCode.PLAYING == this.playState.status;
    }

//    private void start() {
//        addMissingAiPlayers();
//
//        this.playState.status = PlayStateStatusCode.PLAYING;
//        this.playState.gameState = new GameState(gameMap);
//    }


    public boolean isPlayerEndpointKeyAlreadyRegistered(@NotNull String endpointKey) {
        for (PlayerData player : this.playState.players.values()) {
            if (endpointKey.equals(player.getEndpointKey())) {
                return true;
            }
        }
        return false;
    }

    public boolean isUserUuidAlreadyRegistered(@NotNull String userUuid) {
        System.out.println("+++++++++++++++++isUserUuidAlreadyRegistered++++++++++");
        for (PlayerData player : this.playState.players.values()) {

            System.out.println("Check userUuid: " + userUuid + " against player: " + player.getUserUuid());
            if (userUuid.equals(player.getUserUuid())) {
                System.out.println("TRUE");
                return true;
            }
        }
        return false;
    }


//    public int nextAvailablePlayerIndex() {
//        if (!isAnyFreeHumanPlayerSlotsAvailable()||!isAnyFreeAiPlayerSlotsAvailable()) {
//            throw new NoMoreFreePlayerSlotSessionException(sessionKey);
//        }
//        return players.size();
//    }


    public int playerCount() {
        return this.playState.players.size();
    }


    public int incrementTurn() {
        return ++this.playState.turn;
    }


    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////
    ///  GetterSetter methods  /////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////

    public Map<Integer, PlayerData> getAllPlayers() {
        return this.playState.getPlayers();
    }

    public Map<Integer, PlayerData> getAllAiPlayers() {
        Map<Integer, PlayerData> aiPlayers = new HashMap<>();
        for (Map.Entry<Integer, PlayerData> entry : this.playState.getPlayers().entrySet()) {
            if (entry.getValue().isArtificial()) {
                aiPlayers.put(entry.getKey(), entry.getValue());
            }
        }
        return aiPlayers;
    }

    public GameState getGameState() {
        return this.playState.getGameState();
    }

    public void setGameState(GameState gameState) {
        this.playState.setGameState(gameState);
    }

    public PlayStateStatusCode getStatusCode() {
        return this.playState.getStatus();
    }

    public void setStatusCode(PlayStateStatusCode playStateStatusCode) {
        this.playState.setStatus(playStateStatusCode);
    }

    public int getTurn() {
        return this.playState.getTurn();
    }


}

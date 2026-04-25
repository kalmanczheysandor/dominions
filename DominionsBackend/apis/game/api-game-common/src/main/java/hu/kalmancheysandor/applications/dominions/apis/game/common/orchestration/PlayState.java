package hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.GameMap;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.GameMapEngineType;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.configuration.GameMapConfigurationPlayer;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.player.PlayerData;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE
)
public class PlayState {
    int playerSlotSize;
    int turn = 0;
    Map<Integer, PlayerData> players = new HashMap<>();
    GameState gameState = null;
    PlayStateStatusCode status;
    int maxHumanPlayerSlotCount = 0;
    int maxAiPlayerSlotCount = 0;
    Set<Integer> humanPlayerSlot = new LinkedHashSet<>();
    Set<Integer> aiPlayerSlot = new LinkedHashSet<>();


    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Factory methods ////////////////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static PlayState createByGameMap(GameMap gameMap) {
        PlayState newPlayState = new PlayState();

        newPlayState.playerSlotSize = gameMap.getConfiguration().getPlayerCount();
        newPlayState.status = PlayStateStatusCode.RECRUITING;
        for (Map.Entry<Integer, GameMapConfigurationPlayer> entryItem : gameMap.getConfiguration().getPlayers().entrySet()) {
            GameMapConfigurationPlayer configurationPlayer = entryItem.getValue();

            // Register player into the belonging slot
            if (configurationPlayer.getEngine() == GameMapEngineType.HUMAN) {
                newPlayState.humanPlayerSlot.add(entryItem.getKey());
                newPlayState.maxHumanPlayerSlotCount++;
            } else {
                newPlayState.aiPlayerSlot.add(entryItem.getKey());
                newPlayState.maxAiPlayerSlotCount++;
            }
        }

        // Generate and add the game-sate
        GameState gameState = GameState.createByGameMapState(gameMap);
        newPlayState.setGameState(gameState);

        return newPlayState;
    }


    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Getter & Setter methods ////////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public int getHumanPlayerCount() {
        int count = 0;
        for (PlayerData player : players.values()) {
            if (!player.isArtificial()) {
                count++;
            }
        }
        return count;
    }

    public int getAiPlayerCount() {
        int count = 0;
        for (PlayerData player : players.values()) {
            if (!player.isArtificial()) {
                count++;
            }
        }
        return count;
    }




}

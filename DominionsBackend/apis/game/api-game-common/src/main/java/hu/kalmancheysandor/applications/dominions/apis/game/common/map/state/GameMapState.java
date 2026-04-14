package hu.kalmancheysandor.applications.dominions.apis.game.common.map.state;


import hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.PlayState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.player.PlayerData;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStateCell;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStatePlayer;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class GameMapState implements Serializable {
    private Map<Integer, GameMapStatePlayer> players;
    private Map<Integer, GameMapStateCell> cells;

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Factory methods  //////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static GameMapState createByPlayState(PlayState playState) {
        GameMapState gameMapState = new GameMapState();
        GameState gameStateOfPlayState = playState.getGameState();

        // Loop: Players
        for (Map.Entry<Integer, PlayerData> playerEntryItem : playState.getPlayers().entrySet()) {
            PlayerData playerOfPlayState = playerEntryItem.getValue();
            int playerIndexOfPlayState = playerEntryItem.getKey();

            GameStatePlayer gameStateOpponent = gameStateOfPlayState.getOpponent(playerIndexOfPlayState);
            GameMapStatePlayer playerItem = new GameMapStatePlayer(playerOfPlayState.getName(),gameStateOpponent.getReserveSize(),gameStateOpponent.isAlive());
            playerItem.setIntentionGiven(playerOfPlayState.isIntentionGiven());

            // Add an Item
            gameMapState.addPlayer(playerIndexOfPlayState, playerItem);
        }

        // Cells
        for (int cellIndex = 0; cellIndex < gameStateOfPlayState.getCellCount(); cellIndex++) {
            GameStateCell gameStateCell = gameStateOfPlayState.getCells()[cellIndex];

            // Determine: occupier key
            Integer occupierKey = gameStateCell.getOccupierKey();
            if (occupierKey == -1) occupierKey = null;       // -1 means that there is no occupier

//            // Determine: neighbour indexes
//            boolean[] isNeighboursArr = gameState.getNeighboursMatrix()[cellIndex];
//            ArrayList<Integer> neighbourIndexList = new ArrayList<>();
//            for (int neighbourIndex = 0; neighbourIndex < isNeighboursArr.length; neighbourIndex++) {
//                if (isNeighboursArr[neighbourIndex] == true) {
//                    neighbourIndexList.add(neighbourIndex);
//                }
//            }
            // Create item and add
            GameMapStateCell gameMapStateCellItem = new GameMapStateCell();
            gameMapStateCellItem.setOccupierKey(occupierKey);
            gameMapStateCellItem.setArmySize(gameStateCell.getDefendingTroopSize());


            // Add a cell item to the map
            gameMapState.addCell(cellIndex, gameMapStateCellItem);
        }

        return gameMapState;
    }




    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// [ ??????? methods ] //////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    public GameMapStatePlayer addPlayer(int index, GameMapStatePlayer gameMapStatePlayer) {
        if (this.players == null) {
            this.players = new HashMap<>();
        }
        return this.players.put(index, gameMapStatePlayer);
    }

    public GameMapStateCell addCell(int index, GameMapStateCell cell) {

        if(this.cells==null) {
            this.cells = new HashMap<>();
        }

        return this.cells.put(index, cell);
    }

    public int getPlayerCount(){
        return this.players != null ? this.players.size() : 0;
    }

    public  int getCellCount() {
        return this.cells != null ? this.cells.size() : 0;
    }
}

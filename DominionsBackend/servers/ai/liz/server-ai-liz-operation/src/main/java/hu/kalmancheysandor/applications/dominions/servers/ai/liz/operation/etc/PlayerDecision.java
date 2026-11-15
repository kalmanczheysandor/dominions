package hu.kalmancheysandor.applications.dominions.servers.ai.liz.operation.etc;

import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStateCell;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStatePlayer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDecision {
    private int observedPlayerIndex;
    private Map<Integer, BoardField> boardFields;
    private Map<Integer, BoardPlayer> boardPlayers;
    private Move decision;

    /**
     * In PlayerDecision occupier -1 index means empty, 0 means first player index
     *      * -1 target means the skip action.
     *      * 0.. target means an attack agains a cell
     * @param observedPlayerIndex
     * @param target
     * @param attackingTroopSize
     * @param gameState
     * @return
     */
    public static PlayerDecision create(Integer observedPlayerIndex, Integer target, int attackingTroopSize, GameState gameState) {

        // Create player items
        int opponentsCount = gameState.getOpponents().length;
        Map<Integer, BoardPlayer> boardPlayers = new HashMap<>();
        for (int playerIndex = 0; playerIndex < opponentsCount; playerIndex++) {
            GameStatePlayer player = gameState.getOpponents()[playerIndex];
            int territorySize = gameState.getOccupiedCellsCountOfPlayer(playerIndex);
            boardPlayers.put(
                    playerIndex,
                    new BoardPlayer(
                            playerIndex,
                            player.getReserveSize(),
                            territorySize,
                            player.isAlive()
                    )
            );
        }


        // Create field items
        int cellCount = gameState.getOpponents().length;
        Map<Integer, BoardField> boardFields = new HashMap<>();
        for (int cellIndex = 0; cellIndex < cellCount; cellIndex++) {
            GameStateCell cell = gameState.getCells()[cellIndex];
            int occupierIndex = cell.getOccupierKey();        // -1 = empty, 0= first player index, ... other player indexes

            //
            boardFields.put(
                    cellIndex,
                    new BoardField(
                            cellIndex,
                            occupierIndex,
                            cell.getDefendingTroopSize()
                    )
            );
        }

        // Replace null target with -1 to indicate no move
        if (target == null) {
            target = -1;
        }


        //
        return new PlayerDecision(
                observedPlayerIndex,
                boardFields,
                boardPlayers,
                new Move(target, attackingTroopSize)
        );
    }

    @Data
    public static class BoardPlayer {
        private final int index;
        public int reserveSize;
        private int territorySize;
        private boolean alive;

        public BoardPlayer(int index, int reserveSize, int territorySize, boolean alive) {
            this.index = index;
            this.reserveSize = reserveSize;
            this.territorySize = territorySize;
            this.alive = alive;
        }


        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof BoardPlayer other)) return false;
            return index == other.index;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(index);
        }
    }

    @Data
    public static class BoardField {
        private final int index;
        private int occupierIndex;
        private int garrisonSize;

        public BoardField(int index, int occupierIndex, int garrisonSize) {
            this.index = index;
            this.occupierIndex = occupierIndex;
            this.garrisonSize = garrisonSize;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof BoardField other)) return false;
            return index == other.index;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(index);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Move {
        private int target;
        private int troops;
    }
}


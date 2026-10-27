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
    private Set<BoardField> boardFields = new HashSet<>();
    private Set<BoardPlayer> boardPlayers = new HashSet<>();
    private Move decision;

    public static PlayerDecision create(Integer observedPlayerIndex, Integer target, int attackingTroopSize, GameState gameState) {

        // Create player items
        Set<BoardPlayer> boardPlayers = new HashSet<>();
        for (int playerIndex = 0; playerIndex < gameState.getOpponents().length; playerIndex++) {
            GameStatePlayer player = gameState.getOpponents()[playerIndex];
            boardPlayers.add(
                    new BoardPlayer(playerIndex + 1, player.getReserveSize(), 0, player.isAlive())
            );
        }


        // Create field items
        Set<BoardField> boardFields = new HashSet<>();
        for (int cellIndex = 0; cellIndex < gameState.getCells().length; cellIndex++) {
            GameStateCell cell = gameState.getCells()[cellIndex];
            int occupierKey = cell.getOccupierKey();        // -1 = empty, 0= first player index, ... other player indexes
            int occupierIndex = occupierKey + 1;                //  0 = empty, 1= first player index, ... other player indexes

            //
            boardFields.add(new BoardField(cellIndex, occupierIndex, cell.getDefendingTroopSize()));

            // Find the player
            if (occupierIndex > 0) {// 0 would mean an empty cell
                BoardPlayer boardPlayer = boardPlayers.stream()
                        .filter(obj -> obj.getIndex() == occupierIndex)
                        .findFirst()
                        .orElse(null);
                if (boardPlayer == null) {
                    throw new RuntimeException("Occupier index(" + occupierIndex + ") not found in boardPlayers");
                }
                // Increase territory size of the player
                boardPlayer.territorySize++;
            }
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


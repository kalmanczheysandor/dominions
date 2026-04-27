package hu.kalmancheysandor.applications.dominions.apis.game.common.representation.machine;

import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.action.GameAction;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.machine.exception.action.*;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStateCell;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStatePlayer;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStateStatusCode;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.machine.exception.action.GameAlreadyEndedGameStateMachineException;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.machine.exception.action.PlayerAlreadyDeadGameStateMachineException;
import hu.kalmancheysandor.applications.dominions.apis.general.exceptions.IllegalPointOfExecution;

import java.util.*;

//@Component
public class GameStateMachine {
    private GameState gameState;

    public GameStateMachine() {
    }

    public GameStateMachine(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState doSteps(Set<GameAction> plannedActions, GameState gameState) {
        this.gameState = gameState;
        return doSteps(plannedActions);
    }

    public GameState doSteps(Set<GameAction> plannedActions) {
        //if (!isMoreActionPossible()) {
        if (isEndOfGame()) {
            throw new GameAlreadyEndedGameStateMachineException();
        }

        // Validations
        validatePlayersAction(plannedActions);

        // Grouping and resolving conflicting actions
        Map<Integer, Set<GameAction>> conflictGroups = new HashMap<>();
        for (GameAction observedAction : plannedActions) {
            if (!conflictGroups.containsKey(observedAction.getTargetCellKey())) {// The first action in the group
                Set<GameAction> actions = new HashSet<>();
                actions.add(observedAction);
                conflictGroups.put(observedAction.getTargetCellKey(), actions);     // Actions belonging to the same cell are members of the same conflict-group
            } else { // After the first action in the group
                Set<GameAction> actions = conflictGroups.get(observedAction.getTargetCellKey());
                actions.add(observedAction);    // It is a bit trick! it uses the reference of the list of the conflict group
            }
        }
        //  System.out.println(group);

        // Resolving conflict groups. Find the highest offer in each group
        // Under each index where more than one action are stored, conflict resolution has to be performed.
        Set<GameAction> actionsToProcess = new HashSet<>();
        for (Set<GameAction> actions : conflictGroups.values()) {
            GameAction a = resolveTheConflict(actions);
            if (a != null) {
                actionsToProcess.add(a);
            }
        }

        // Calculate the outcome of steps
        for (GameAction processedAction : actionsToProcess) {
            GameStateCell attackedGameStateCell = getCell(processedAction.getTargetCellKey());

            if (processedAction.getTargetCellKey() != null) {   // When it was an attack and not a reserve move

                if (attackedGameStateCell.getDefendingTroopSize() < processedAction.getAttackingTroopSize()) { // Defender are weaker
                    attackedGameStateCell.setDefendingTroopSize(processedAction.getAttackingTroopSize() - attackedGameStateCell.getDefendingTroopSize());
                    attackedGameStateCell.setOccupierKey(processedAction.getPlayerKey());
                } else if (attackedGameStateCell.getDefendingTroopSize() > processedAction.getAttackingTroopSize()) {    // Stronger defenders
                    attackedGameStateCell.setDefendingTroopSize(attackedGameStateCell.getDefendingTroopSize() - processedAction.getAttackingTroopSize());
                } else {// Attacker and defenders were equally strong
                    attackedGameStateCell.setDefendingTroopSize(0);
                    attackedGameStateCell.setOccupierKey(-1);
                }
            } else if (processedAction.getTargetCellKey() == null) {

            }
        }

        // Decreasing reserve of each attacker
        for (GameAction observedAction : plannedActions) {
            GameStatePlayer attackingPlayer = getPlayer(observedAction.getPlayerKey());
            attackingPlayer.setReserveSize(attackingPlayer.getReserveSize() - observedAction.getAttackingTroopSize());
        }


        // Deactivate players who lost their last cell
        GameStatePlayer[] opponents = gameState.getOpponents();
        for (int playerKey = 0; playerKey < opponents.length; playerKey++) {
            if (gameState.getOccupiedCellsCountOfPlayer(playerKey) <= 0) {
                opponents[playerKey].setAlive(false);
                opponents[playerKey].setReserveSize(0);
            }
        }


        if (!isNoMoreEmptyCell()&& countAlivePlayers()==1) {
            List<Integer> winnerKeys = listTheStrongestPlayerKeys();
            Integer theWinnerKey =winnerKeys.get(0);
            System.out.println("THE WINNERRRR:"+theWinnerKey);
            if(winnerKeys.size()!=1) {
                throw new IllegalPointOfExecution("Exactly one player key must be present.");
            }
            occupyEverythingForTheWinner(winnerKeys.get(0));
        }


        // Removal of the weakest player in case of no more empty cell
        if (isNoMoreEmptyCell()) {

            if (countAlivePlayers() > 1) {
                List<Integer> weakestPlayers = listTheWeakestPlayerKeys();
                int weakestPlayersCount = weakestPlayers.size();
                int alivePlayersCount = countAlivePlayers();
                if (weakestPlayersCount == 0) {
                    throw new IllegalPointOfExecution();
                }
                System.out.println("Weakest"+weakestPlayers);
                System.out.println("Weakest count:"+weakestPlayersCount);

                if (alivePlayersCount == weakestPlayersCount) { // All player equally strong/weak
                    // There is no removal of any player
                    // End of game
                    // Result: Tie for all alive players
                } else if ((alivePlayersCount - 1) == weakestPlayersCount) { // There is one who is stronger but other are equally strong/weak
                    // There is no removal of any player
                    // End of game
                    // Result: The strongest one is the winner remaining others are losers.
                } else if ((alivePlayersCount - 1) > weakestPlayersCount) { // There are more than one strong or stronger player, and there are one or more weakest player
                    // Weakest player(s) are removed.
                    // The game continues

                    // Demolish dominions of all weakest players
                    for(Integer weakestPlayerKey:weakestPlayers){
                        demolishPlayerDominion(weakestPlayerKey);
                        getPlayer(weakestPlayerKey).setAlive(false);
                        getPlayer(weakestPlayerKey).setReserveSize(0);
                    }
                } else {
                    throw new IllegalPointOfExecution("Unsupported case is found!");
                }
            }

            // ha ide eljutunk akkor el lett tavolitva a kiesok
            if (isNoMoreEmptyCell()) {
                List<Integer> winnerKeys = listTheStrongestPlayerKeys();
                gameState.setWinnerKeys(winnerKeys.stream().mapToInt(Integer::intValue).toArray());
                gameState.setStatusCode(GameStateStatusCode.FINISHED);
            }
        }

        if (!isEndOfGame()) {
            incrementAllAliveReserve();
            gameState.setStatusCode(GameStateStatusCode.PROCEEDED);
        }

        return gameState;
    }

    public static void validatePlayerAction(GameState state, GameAction action) {
        int playerKey = action.getPlayerKey();
        Integer targetCellKey = action.getTargetCellKey();
        GameStatePlayer player = state.getOpponent(playerKey);

        // No more action is allowed when game is finished
        if (state.getStatusCode() == GameStateStatusCode.FINISHED) {
            throw new GameAlreadyEndedGameStateMachineException();
        }

        // Player must be alive
        if (!player.isAlive()) {
            throw new PlayerAlreadyDeadGameStateMachineException(playerKey);
        }


        if (targetCellKey != null) {       // When itt is an attack action
            GameStateCell targetCell = state.getCell(targetCellKey);

            // When it is an attack without troops
            if (action.getAttackingTroopSize() <= 0) {
                throw new NoTroopsWereSentGameStateMachineException(playerKey);
            }

            // When it is an attack without troops
//            if (action.getAttackingTroopSize() > 100) {
//                throw new TooMuchTroopsWereSentGameStateMachineException(playerKey);
//            }

            // When it is an attack but the army size is over calculated
            if (player.getReserveSize() < action.getAttackingTroopSize()) {
                throw new NotEnoughSupplyGameStateMachineException(playerKey, action.getAttackingTroopSize(), player.getReserveSize());
            }

            // When the attacked cell belongs to the attacker and neither to the enemy and nor empty.
            if (targetCell.getOccupierKey() == action.getPlayerKey()) {
                throw new SelfAttackPlayerGameStateMachineException(playerKey, targetCellKey);
            }

            if (!isCellANeighbourOfPlayer(state, action.getPlayerKey(), action.getTargetCellKey())) {
                throw new OutOfAttackRangeGameStateMachineException(action.getPlayerKey(), action.getTargetCellKey());
            }

            if (isPlayerCausingDoughnutEffect(state, action.getPlayerKey())) {
//                System.out.println("DOUGHNUT ? ["+action.getPlayerKey()+"] >> yes ");
                if (!isCellAnEmptyNeighbourOfPlayer(state, action.getPlayerKey(), action.getTargetCellKey())) {
                    //throw new OutOfDoughnutAttackRangePlayerActionException(action.getPlayerKey(), action.getTargetCellKey());
                }
            }

        } else {

            if (action.getAttackingTroopSize() > 0) {
                throw new NoTroopsPermittedToSendGameStateMachineException(playerKey);
            }
        }

    }

    private void validatePlayersAction(Set<GameAction> actions) {
        GameStateCell targetCell;
        GameStatePlayer player;
        int playerKey;
        int targetCellKey;


        for (GameAction action : actions) {
            validatePlayerAction(gameState, action);
        }
    }

    private GameAction resolveTheConflict(Set<GameAction> actions) {
        int highestValue = 0;
        int count = 0;
        GameAction a = null;
        for (GameAction action : actions) {
            if (action.getAttackingTroopSize() == highestValue) {
                count++;
                a = null;
            } else if (action.getAttackingTroopSize() > highestValue) {
                highestValue = action.getAttackingTroopSize();
                count = 1;
                a = action;
            }
        }

        if (count == 1 && a != null) {
            return a;
        }
        return null;
    }

    private void incrementAllAliveReserve() {
        for (GameStatePlayer player : gameState.getOpponents()) {
            if (player.isAlive()) {
                player.incrementReserveSize(1);
            }
        }
        gameState.getOpponents()[1].incrementReserveSize(0);
    }

    private void demolishPlayerDominion(int playerKey) {
        for (GameStateCell cell : gameState.getCells()) {
            if (cell.getOccupierKey() == playerKey) {
                cell.free();
            }
        }
    }

    private void occupyEverythingForTheWinner(int playerKey) {

        for (GameStateCell cell : gameState.getCells()) {
            if (cell.isEmpty() || cell.getOccupierKey() != playerKey) {
                cell.setOccupierKey(playerKey);
                cell.setDefendingTroopSize(1);
            }
        }
    }

//    private Integer findTheWeakestPlayer() {
//        //System.out.println("findTheWeakestPlayer");
//        int weakestValue = Integer.MAX_VALUE;
//        int foundCount = 0;
//        Integer weakestKey = null;
//
//        GameStatePlayer[] opponents = gameState.getOpponents();
//        for (int playerKey = 0; playerKey < opponents.length; playerKey++) {
//            GameStatePlayer player = opponents[playerKey];
//
//            if (player.isAlive()) {
//                //System.out.println("--------" + playerKey+"--------");
//                int dominionSize = countPlayerCells(playerKey);
//
////                System.out.println("PlayerIndex:" + playerKey);
////                System.out.println("dominionSize:" + dominionSize);
//
//
//                if (dominionSize == weakestValue) {
//                    foundCount++;
//                    weakestKey = null;
//                } else if (dominionSize < weakestValue) {
//                    weakestValue = dominionSize;
//                    foundCount = 1;
//                    weakestKey = playerKey;
//                }
//            }
//        }
//
//        if (foundCount == 1 && weakestKey != null) {
//            return weakestKey;
//        }
//        return null;
//    }


    private List<Integer> listTheWeakestPlayerKeys() {
        int weakestValue = Integer.MAX_VALUE;
        List<Integer> weakestKeys = new ArrayList<>();

        GameStatePlayer[] opponents = gameState.getOpponents();
        for (int playerKey = 0; playerKey < opponents.length; playerKey++) {
            GameStatePlayer player = opponents[playerKey];

            if (player.isAlive()) {
                //System.out.println("--------" + playerKey+"--------");
                int currentSizeOfDominion = countPlayerCells(playerKey);


                if (currentSizeOfDominion == weakestValue) {
                    weakestKeys.add(playerKey);
                } else if (currentSizeOfDominion < weakestValue) {
                    weakestValue = currentSizeOfDominion;
                    weakestKeys.clear();
                    weakestKeys.add(playerKey);
                }
            }
        }

        return weakestKeys;
    }


    private List<Integer> listTheStrongestPlayerKeys() {
        // Initial values
        int strongestValue = Integer.MIN_VALUE;
        List<Integer> strongestPlayerKeys = new ArrayList<>();

        GameStatePlayer[] opponents = gameState.getOpponents();
        for (int playerKey = 0; playerKey < opponents.length; playerKey++) {
            GameStatePlayer player = opponents[playerKey];
            System.out.println("PlayerKey:"+playerKey);
            System.out.println(player);

            if (player.isAlive()) {
                int currentSizeOfDominion = countPlayerCells(playerKey);
                System.out.println("currentSizeOfDominion:"+currentSizeOfDominion);
                System.out.println("strongestValue:"+strongestValue);

                if (currentSizeOfDominion == strongestValue) {
                    System.out.println("equal");
                    strongestPlayerKeys.add(playerKey);
                } else if (currentSizeOfDominion > strongestValue) {
                    System.out.println("higher");
                    strongestValue = currentSizeOfDominion;
                    strongestPlayerKeys.clear();
                    System.out.println("-flushed");
                    strongestPlayerKeys.add(playerKey);
                    System.out.println(strongestPlayerKeys);
                }
            }
        }

        return strongestPlayerKeys;
    }

    private boolean isNoMoreEmptyCell() {
        for (GameStateCell cell : gameState.getCells()) {
            if (cell.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean isEndOfGame() {
        if (gameState.getStatusCode() == GameStateStatusCode.FINISHED) {
            return true;
        }
        return false;
    }


    private int countPlayerCells(int playerKey) {
        int count = 0;
        for (GameStateCell cell : gameState.getCells()) {
            if (cell.getOccupierKey() == playerKey) {
                count++;
            }
        }
        return count;
    }

    private int countAlivePlayers() {
        return findAlivePlayers().size();
    }


    private List<GameStatePlayer> findAlivePlayers() {
        List<GameStatePlayer> alivePlayers = new ArrayList<>();

        for (GameStatePlayer opponent : gameState.getOpponents()) {
            if (opponent.isAlive()) {
                alivePlayers.add(opponent);
            }
        }
        return alivePlayers;
    }


    private GameStateCell getCell(int cellKey) {
        GameStateCell[] cells = gameState.getCells();
        return cells[cellKey];
    }

    private static Set<GameStateCell> getNeighboursOfCell(GameState state, int cellKey) {
        Set<GameStateCell> realNeighbours = new HashSet<>();

        boolean[] neighboursRow = state.getNeighboursMatrix()[cellKey];
        for (int neighbourKey = 0; neighbourKey < neighboursRow.length; neighbourKey++) {
            if (neighboursRow[neighbourKey] == true) {
                realNeighbours.add(state.getCell(neighbourKey));
            }
        }
        return realNeighbours;
    }

//    private Set<Integer> getEmptyCellKeys() {
//        Set<Integer> emptyCellKeys = new HashSet<>();
//        GameState.Cell[] cells = gameState.getCells();
//
//        for (int cellKey = 0; cellKey < cells.length; cellKey++) {
//            if (cells[cellKey].isEmpty()) {
//                emptyCellKeys.add(cellKey);
//            }
//        }
//        return emptyCellKeys;
//    }

    private GameStatePlayer getPlayer(int playerKey) {
        GameStatePlayer[] opponents = gameState.getOpponents();
        return opponents[playerKey];
    }

    public GameStateStatusCode getGameStatusCode() {
        return gameState.getStatusCode();
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }


    private static boolean isCellANeighbourOfPlayer(GameState state, int playerKey, int observedCellKey) {
        GameStateCell observedCell = state.getCell(observedCellKey);

        // Any cell occupied by player is not a counted as a neighbour of that player
        if (observedCell.getOccupierKey() == playerKey) {
            return false;
        }

        for (GameStateCell neighbourCell : getNeighboursOfCell(state, observedCellKey)) {
            if (neighbourCell.getOccupierKey() == playerKey) { // When the neighbour cell is occupied by the player
                return true;
            }
        }
        return false;
    }

    private static boolean isCellAnEmptyNeighbourOfPlayer(GameState state, int playerKey, int cellKey) {
        GameStateCell observedCell = state.getCell(cellKey);

        // Any cell occupied by the player is not a counted as a neighbour of that player
        if (observedCell.getOccupierKey() == playerKey) {
            return false;
        }

        // Cell must be empty
        if (!observedCell.isEmpty()) {
            return false;
        }


        for (GameStateCell neighbourCell : getNeighboursOfCell(state, cellKey)) {
            if (neighbourCell.getOccupierKey() == playerKey) { // When the neighbour cell is occupied by the player
                return true;
            }
        }
        return false;
    }

    private static boolean isPlayerCausingDoughnutEffect(GameState state, int playerKey) {
        for (int emptyCellKey : state.getEmptyCellKeys()) {
            if (isCellBlockedByPlayer(state, emptyCellKey, playerKey)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isCellBlockedByPlayer(GameState state, int cellKey, int playerKey) {
        for (GameStateCell neighbourCell : getNeighboursOfCell(state, cellKey)) {
            if (neighbourCell.getOccupierKey() != playerKey) {
                return false;
            }
        }
        return true;
    }



}

package hu.kalmancheysandor.applications.dominions.apis.ai.liz;

import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionContext;
import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionResult;
import hu.kalmancheysandor.applications.dominions.apis.ai.common.TAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.INeuralAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.INeuralNetwork;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStateCell;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStatePlayer;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.*;


public class LizAiEngine extends TAiEngine implements INeuralAiEngine {

    private Map<String, INeuralNetwork> enemyNetworks;

    public LizAiEngine() {
        enemyNetworks = new LinkedHashMap<>();
    }

    @Override
    public void registerEnemyNetwork(String userUuid, INeuralNetwork neuralNetwork) {
        enemyNetworks.put(userUuid, neuralNetwork);
    }

    @Override
    public AiDecisionResult makeDecision(AiDecisionContext context) {
        // Preparation
        int myAttackPower = calculatePlayerMaxAttackPower(context.getGameState(), context.getYourPlayerKey());

        // Make decision
        Integer decisionCell = makeADecision(context.getGameState(), context.getYourPlayerKey(), context.getPlayers());
        System.out.println("Decision cell:" + decisionCell);
        System.out.println("myAttackPower:" + myAttackPower);
        if (decisionCell != null) {
            return new AiDecisionResult(decisionCell, myAttackPower);
        }
        return new AiDecisionResult(0, 0);
    }


    private Integer makeADecision(GameState gameState, int supportedPlayerKey, Map<Integer, AiDecisionContext.Player> players) {
        System.out.println("---------------makeADecision---------------");

        // Enemy players (by removing the supported player)
        Map<Integer, AiDecisionContext.Player> enemyPlayers = new HashMap<>(players);
        enemyPlayers.remove(supportedPlayerKey);

        // Preparation
        int myAttackPower = calculatePlayerMaxAttackPower(gameState, supportedPlayerKey);
        Set<Integer> myAttackZoneKeys = collectCellKeysOfCurrentAttackZoneOfPlayer(gameState, supportedPlayerKey);
        Map<Double, Set<Integer>> conflictGroups = new TreeMap<>(Comparator.naturalOrder()); // Providing increasing order of keys
        conflictGroups.putAll(determineConflictGroups(gameState, supportedPlayerKey, enemyPlayers));

        //
        Set<Integer> possibleConflictCellKeys;
        Double currentPossibilityScore;
        Set<Integer> observedAttackZoneKeys;
        GameStateCell observedCell;
        for (Map.Entry<Double, Set<Integer>> entry : conflictGroups.entrySet()) { // Increasing possibility of conflict
            currentPossibilityScore = entry.getKey();
            possibleConflictCellKeys = entry.getValue();

            System.out.println("AttackZoneKeys[Score:" + currentPossibilityScore + "%]" + myAttackZoneKeys);
            System.out.println("ConflictingGroup:[Score:" + currentPossibilityScore + "%] Cells:" + possibleConflictCellKeys);

            // Creates the intersection of the possible attack zones and the cell keys of related conflict group.
            // Any conflict score lower than the current one is no longer considered potential.
            observedAttackZoneKeys = new HashSet<>(myAttackZoneKeys);
            observedAttackZoneKeys.retainAll(possibleConflictCellKeys);

            System.out.println("Intercessed[Score:" + currentPossibilityScore + "%]" + observedAttackZoneKeys);


            // Option1: Empty ones - Trys to find an empty cell in the intersection. If there is no one, then check the following option.
            // Trys to find: The first cell
            for (int anObservedAttackZoneKeys : observedAttackZoneKeys) {
                observedCell = gameState.getCell(anObservedAttackZoneKeys);
                if (observedCell.isEmpty()) {
                    System.out.println("-Found:Empty");
                    return anObservedAttackZoneKeys;
                }
            }

            // Option2: Weaker ones - Trys to find a weaker cell in the intersection than attacking troops. If there is no one, then check the following option.
            // Trys to find: the most weak cell
            Integer mostWeakerCellKey = null;
            Integer lowestWeakness = Integer.MAX_VALUE;
            for (int anObservedAttackZoneKeys : observedAttackZoneKeys) {
                observedCell = gameState.getCell(anObservedAttackZoneKeys);
                if (!observedCell.isEmpty() && observedCell.getDefendingTroopSize() < myAttackPower) {
                    if (observedCell.getDefendingTroopSize() < lowestWeakness) {
                        lowestWeakness = observedCell.getDefendingTroopSize();
                        mostWeakerCellKey = anObservedAttackZoneKeys;
                    }
                }
            }
            if (mostWeakerCellKey != null) {
                System.out.println("-Found:Weaker");
                return mostWeakerCellKey;
            }

            // Option3: Equal ones - Trys to find an equally strong cell in the intersection than attacking troops. If there is no one, then check the following option.
            // Trys to find: The first cell
            for (int anObservedAttackZoneKeys : observedAttackZoneKeys) {
                observedCell = gameState.getCell(anObservedAttackZoneKeys);
                if (!observedCell.isEmpty() && observedCell.getDefendingTroopSize() == myAttackPower) {
                    System.out.println("-Found:Equal");
                    return anObservedAttackZoneKeys;
                }
            }

            // Option4: Stronger ones - Trys to find a stronger cell in the intersection than attacking troops. If there is no one, then check the following option.
            // Trys to find the most strong cell
            Integer lessStrongerCellKey = null;
            Integer lowestStrength = Integer.MAX_VALUE;
            for (int anObservedAttackZoneKeys : observedAttackZoneKeys) {
                observedCell = gameState.getCell(anObservedAttackZoneKeys);
                if (!observedCell.isEmpty() && observedCell.getDefendingTroopSize() > myAttackPower) {
                    if (observedCell.getDefendingTroopSize() < lowestStrength) {
                        lowestStrength = observedCell.getDefendingTroopSize();
                        lessStrongerCellKey = anObservedAttackZoneKeys;
                    }
                }
            }
            if (lessStrongerCellKey != null) {
                System.out.println("-Found:Stronger");
                return lessStrongerCellKey;
            }
        }
        System.out.println("-Found:Nothing");
        return null;
    }

    private Map<Double, Set<Integer>> determineConflictGroups(GameState gameState, int supportedPlayerKey, Map<Integer, AiDecisionContext.Player> enemyPlayers) {

        int boardCellCount = gameState.getCellCount();

        double[] modelOutput;
        Map<Double, Set<Integer>> probabilityGroups = new TreeMap<>();
        String enemyPlayerUuid;

        for (Map.Entry<Integer, AiDecisionContext.Player> enemyPlayerEntry : enemyPlayers.entrySet()) {
            enemyPlayerUuid = enemyPlayerEntry.getValue().getUserUuid();
            System.out.println("AskNeuralModel:" + enemyPlayerUuid);

            // Attempt to access the network of the enemy
            if (!enemyNetworks.containsKey(enemyPlayerUuid)) {
                throw new RuntimeException("No network object is registered at key:" + enemyPlayerUuid);
            }
            INeuralNetwork enemyPlayerNeuralNetwork = enemyNetworks.get(enemyPlayerUuid);

            // Raw response based on current situation
            LizInputData networkInputData = generateNetworkInputDataByGameState(gameState, supportedPlayerKey);
            modelOutput = enemyPlayerNeuralNetwork.calculate(networkInputData);
            if (modelOutput == null) { // When there is no network file
                modelOutput = new double[boardCellCount];   // By default, all value is 0
            }


            // Gather cell indexes into probability groups.
            double cellAttackProbability;
            for (int cellIndex = 0; cellIndex < boardCellCount; cellIndex++) {
                cellAttackProbability = modelOutput[cellIndex];

                // Gather cell keys into probability-value groups
                if (probabilityGroups.containsKey(cellAttackProbability)) { // When the group is already defined
                    Set<Integer> keyList = probabilityGroups.get(cellAttackProbability);
                    keyList.add(cellIndex);
                } else { // When the group is not defined yet
                    Set<Integer> keyList = new HashSet<>();
                    keyList.add(cellIndex);
                    probabilityGroups.put(cellAttackProbability, keyList);
                }
            }
        }
        return probabilityGroups;
    }


    private LizInputData generateNetworkInputDataByGameState( GameState gameState, int supportedPlayerKey) {

        // Initialise
        int boardCellCount = gameState.getCellCount();
        Map<Integer, Integer> rankMap = LizAiEngine.createRankMapOfOccupierIndexes(gameState, supportedPlayerKey);




        // Determine attack power
        int reserveSize = getPlayerReserveSize(gameState, supportedPlayerKey);
        int attackPower = reserveSize;
        if (attackPower > RESERVE_SIZE_MAX) {
            attackPower = RESERVE_SIZE_MAX;
        }

        // Collect owners to each cell
        int[] cellRankList = new int[boardCellCount];
        int cellIndex = 0;
        for (GameStateCell cell : gameState.getCells()) {
            int occupierKey = cell.getOccupierKey();

            // Find the rank-index related to the player-index
            if (!rankMap.containsKey(occupierKey)) {
                throw new RuntimeException("Occupier Key(" + occupierKey + ") not found in rank-map");
            }
            int rankIndex = rankMap.get(occupierKey);   // Empty cell always at 0 rank-index, and supported-player is always on 1 rank-index

            cellRankList[cellIndex] = rankIndex;
            cellIndex++;
        }

        // Collect defenders values to each cell
        int[] cellDefenderSizeList = new int[boardCellCount];
        cellIndex = 0;
        for (GameStateCell cell : gameState.getCells()) {
            cellDefenderSizeList[cellIndex] = cell.getDefendingTroopSize();
            cellIndex++;
        }

        // Output
        LizInputData trainingData = new LizInputData();
        trainingData.setAttackPower(attackPower);
        trainingData.setCellOwnerRanks(cellRankList);
        trainingData.setCellDefendersSize(cellDefenderSizeList);

        return trainingData;
    }

    private static LizNeuralNetwork.Configuration generateNetworkConfiguration() {
        return LizNeuralNetwork.Configuration.builder()
                .enemiesCountMin(TAiEngine.ENEMIES_COUNT_MIN)
                .enemiesCountMax(TAiEngine.ENEMIES_COUNT_MAX)
                .ownerRankMinIndex(TAiEngine.OWNER_RANKS_INDEX_MIN)
                .ownerRankMaxIndex(TAiEngine.OWNER_RANKS_INDEX_MAX)
                .defendersMinCount(TAiEngine.DEFENDERS_COUNT_MIN)
                .defendersMaxCount(TAiEngine.DEFENDERS_COUNT_MAX)
                .attackPowerMin(TAiEngine.RESERVE_SIZE_MIN)
                .attackPowerMax(TAiEngine.RESERVE_SIZE_MAX)
                .build();
    }


    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// ?????? methods //////////////////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static Map<Integer, Integer> createRankMapOfOccupierIndexes(GameState gameState, int supportedPlayerKey) {

        // Building list of playerIndex - power pairs
        LinkedHashMap<Integer, Integer> playerPowerList = new LinkedHashMap<>();
        int playerIndex = 0;
        for (GameStatePlayer player : gameState.getOpponents()) {
            if (player.isAlive()) {
                int power = gameState.getOccupiedCellsCountOfPlayer(playerIndex);
                playerPowerList.put(playerIndex, power);
            }
            playerIndex++;
        }

        // Store the original order of keys
        List<Integer> originalOrderOfKeys = new ArrayList<>(playerPowerList.keySet());

        // Sort player keys into descending order of power
        List<Integer> sortedPlayerKeys = new ArrayList<>(originalOrderOfKeys);
        sortedPlayerKeys.sort((k1, k2) -> {
            int difference = playerPowerList.get(k2).compareTo(playerPowerList.get(k1));
            if (difference != 0) {
                return difference;
            } else {        // When both player equally strong then their player index is used to determine which is ahead
                return Integer.compare(originalOrderOfKeys.indexOf(k1), originalOrderOfKeys.indexOf(k2)); // Smaller index is to be first
            }
        });

        // Descending order of players (except supported player)
        Map<Integer, Integer> rankMap = new HashMap<>();    // Structure: <PlayerIndex,RankIndex>

        int rankIndex = 0;
        rankMap.put(-1, rankIndex);                                 // Unoccupied(=-1) cell must be valued to 0 rank-index
        rankIndex = 1;
        rankMap.put(supportedPlayerKey, rankIndex);                 // Supported player must be valued to 1 rank-index
        for (Integer playerKey : sortedPlayerKeys) {                // RankMap is being filled in descending value of power, except the supported player
            if (!rankMap.containsKey(playerKey)) {
                rankIndex++;
                rankMap.put(playerKey, rankIndex);
            }
        }

        return rankMap;
    }


}

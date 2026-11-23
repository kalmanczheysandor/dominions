package hu.kalmancheysandor.applications.dominions.apis.ai.algorithm.riskmin;

import hu.kalmancheysandor.applications.dominions.apis.ai.general.common.AiDecisionContext;
import hu.kalmancheysandor.applications.dominions.apis.ai.general.common.AiDecisionResult;
import hu.kalmancheysandor.applications.dominions.apis.ai.general.common.TAiAlgorithm;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStateCell;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStatePlayer;

import java.util.*;

import static hu.kalmancheysandor.applications.dominions.apis.util.validate.asserted.VariableAssert.*;

public abstract class TRiskMinAlgorithm extends TAiAlgorithm implements IRiskMinAlgorithm {

    // Bias values of strategic groups
    private final static double SG_UNARMED_BIAS = 0.4;
    private final static double SG_WEAKER_BIAS = 0.3;
    private final static double SG_SKIP_BIAS = 0.2;
    private final static double SG_EQUAL_BIAS = 0.1;
    private final static double SG_STRONGER_BIAS = 0.0;

    // Precedence values of strategic groups 1- is the strongest
    private final static int SG_UNARMED_PRECEDENCE = 1;
    private final static int SG_WEAKER_PRECEDENCE = 2;
    private final static int SG_SKIP_PRECEDENCE = 3;
    private final static int SG_EQUAL_PRECEDENCE = 4;
    private final static int SG_STRONGER_PRECEDENCE = 5;

    protected abstract Map<Double, Set<Integer>> buildOperatorRiskGroups(GameState gameState, int supportedPlayerKey, Map<Integer, AiDecisionContext.Player> enemyPlayers);

    @Override
    public AiDecisionResult makeDecision(AiDecisionContext context) {

        // Parameter checking
        assertObjectIsNotNull(context, "context");
        assertObjectIsNotNull(context.getGameState(), "GameState");
        assertIntNotNegative(context.getYourPlayerKey(), "ObservedPlayerKey");

        //
        GameState gameState = context.getGameState();
        validateInitialState(gameState, context.getYourPlayerKey());

        System.out.println("CONTEXT:" + context);
        System.out.println("HELLO");

        // Make decision
        Operator choosenOperator = chooseAnOperator(
                context.getGameState(),
                context.getYourPlayerKey(),
                context.getPlayers()
        );
        if (choosenOperator == null) {
            throw new RuntimeException("Decision operator key is: null");
        }

        //
        System.out.println("Decision operator key is:" + choosenOperator.getTarget());
        System.out.println("supportedPlayerAttackPower:" + choosenOperator.attackPower);

        return new AiDecisionResult(choosenOperator.getTarget(), choosenOperator.attackPower);
    }


    private static void validateInitialState(GameState gameState, int supportedPlayerKey) {
        GameStatePlayer supportedPlayerObj = gameState.getOpponent(supportedPlayerKey); // Check if the player exists
        if (supportedPlayerObj == null) {
            throw new RuntimeException("Supported player is not found in the GameState");
        }

        //
        if (!supportedPlayerObj.isAlive()) {
            throw new RuntimeException("Supported player is not alive");
        }

        //
        if (supportedPlayerObj.getReserveSize()==0) {
            throw new RuntimeException("Supported player has no reserve units");
        }

        if (gameState.getOccupiedCellsCountOfPlayer(supportedPlayerKey) == 0) {
            throw new RuntimeException("Supported player has no occupied cells");
        }
    }



    private Operator chooseAnOperator(GameState gameState, int supportedPlayerKey, Map<Integer, AiDecisionContext.Player> players) {
        System.out.println("---------------chooseAnOperator---------------");

        // Determine enemy players (by removing the supported player)
        Map<Integer, AiDecisionContext.Player> enemyPlayers = new HashMap<>(players);
        enemyPlayers.remove(supportedPlayerKey);
        System.out.println("ENEMYPlayers:" + enemyPlayers);

        // Preparation
        int maxAttackPowerOfSupportedPlayer = calculatePlayerMaxAttackPower(gameState, supportedPlayerKey);

        // Build risk groups
        Map<Double, Set<Integer>> riskGroups = new TreeMap<>(Comparator.naturalOrder()); // Providing increasing order of keys
        riskGroups.putAll(
                buildOperatorRiskGroups(gameState, supportedPlayerKey, enemyPlayers)
        );
        System.out.println("AllRiskGroups:" + riskGroups);

        NNN choosen = null;
        Double foundStrategicValue = Double.MIN_VALUE;
        for (Map.Entry<Double, Set<Integer>> entry : riskGroups.entrySet()) { // Increasing possibility of conflict

            Set<Integer> riskGroup = entry.getValue();
            Double riskScore = entry.getKey();
            NNN found = null;

            System.out.println(">>>>>CurrentRiskScore:"+riskScore);



            // 1 Strategic Group: Unarmed
            found = findTheFirstEmptyCell(gameState, riskGroup, riskScore);
            if (found != null) {
                choosen = chooseBetweenNewlyFoundAndPreviouslyChosen(found, choosen);
                System.out.println("1SG.Found:"+found);
                System.out.println("1SG.Chosen:"+choosen);
            }

            // 2 Strategic Group: Weaker
            found = findTheFirstMostWeakCell(gameState, riskGroup, riskScore, maxAttackPowerOfSupportedPlayer);
            if (found != null) {
                choosen = chooseBetweenNewlyFoundAndPreviouslyChosen(found, choosen);
                System.out.println("2SG.Found:"+found);
                System.out.println("2SG.Chosen:"+choosen);
            }

            // 3 Strategic Group: Skip
            found = findTheSkipOperator(gameState, riskGroup, riskScore);
            if (found != null) {
                choosen = chooseBetweenNewlyFoundAndPreviouslyChosen(found, choosen);
                System.out.println("3SG.Found:"+found);
                System.out.println("3SG.Chosen:"+choosen);
            }

            // 4 Strategic Group: Equal
            found = findTheFirstEqualCell(gameState, riskGroup, riskScore, maxAttackPowerOfSupportedPlayer);
            if (found != null) {
                choosen = chooseBetweenNewlyFoundAndPreviouslyChosen(found, choosen);
                System.out.println("4SG.Found:"+found);
                System.out.println("4SG.Chosen:"+choosen);
            }

            // 5 Strategic Group: Stronger
            found = findTheFirstLessStrongCell(gameState, riskGroup,riskScore, maxAttackPowerOfSupportedPlayer);
            if (found != null) {
                choosen = chooseBetweenNewlyFoundAndPreviouslyChosen(found, choosen);
                System.out.println("5SG.Found:"+found);
                System.out.println("5SG.Chosen:"+choosen);
            }
        }

        //
        if (choosen != null) {
            if(choosen.getKey()==0) {
                return new Operator(choosen.getKey(), 0);
            }
            return new Operator(choosen.getKey(), maxAttackPowerOfSupportedPlayer);
        }

        return null;
    }


    private static NNN chooseBetweenNewlyFoundAndPreviouslyChosen(NNN found, NNN previouslyChosen) {
        // Parameter checking
        if (found == null) {
            throw new RuntimeException("Found cell is null");
        }

        // When the previously chosen going to be replaced by the newly found one
        if (previouslyChosen == null) {
            return found;
        } else if (found.getStrategicValue() > previouslyChosen.getStrategicValue()) {
            return found;
        } else if (found.getStrategicValue() == previouslyChosen.getStrategicValue() && found.getPrecedence() > previouslyChosen.getPrecedence()) {
            return found;
        }

        return previouslyChosen;    // When the previously chosen is not changed
    }

    private static class NNN {
        private int key;
        private double strategicValue;
        private int precedence;

        public NNN(int key, double strategicValue, int precedence) {
            this.key = key;
            this.strategicValue = strategicValue;
            this.precedence = precedence;
        }


        public int getKey() {
            return key;
        }

        public double getStrategicValue() {
            return strategicValue;
        }

        public int getPrecedence() {
            return precedence;
        }

        @Override
        public String toString() {
            return "NNN{" +
                    "key=" + key +
                    ", strategicValue=" + strategicValue +
                    ", precedence=" + precedence +
                    '}';
        }
    }

    double calculateStrategicValue(double riskScore, double bias) {
        return (bias - riskScore) * 100;
    }

    private NNN findTheFirstEmptyCell(
            GameState gameState,
            Set<Integer> possibleCellKeys,
            double currentRiskScore
    ) {
        for (int cellKey : possibleCellKeys) {
            GameStateCell cellObj = gameState.getCell(cellKey);
            if (cellObj.isEmpty()) {
                double strategicValue = calculateStrategicValue(currentRiskScore, SG_UNARMED_BIAS);
                return new NNN(cellKey, strategicValue, SG_UNARMED_PRECEDENCE);
            }
        }
        return null;
    }


    private NNN findTheFirstMostWeakCell(GameState gameState, Set<Integer> possibleCellKeys, double currentRiskScore, int currentAttackPower) {
        Integer mostWeakerCellKey = null;
        Integer lowestWeakness = Integer.MAX_VALUE;

        //
        for (int cellKey : possibleCellKeys) {
            GameStateCell cellObj = gameState.getCell(cellKey);

            if (!cellObj.isEmpty() && cellObj.getDefendingTroopSize() < currentAttackPower) {
                if (cellObj.getDefendingTroopSize() < lowestWeakness) {
                    lowestWeakness = cellObj.getDefendingTroopSize();
                    mostWeakerCellKey = cellKey;


                }
            }
        }

        //
        if (mostWeakerCellKey != null) {
            double strategicValue = calculateStrategicValue(currentRiskScore, SG_WEAKER_BIAS);
            return new NNN(mostWeakerCellKey, strategicValue, SG_WEAKER_PRECEDENCE);
        }

        return null;
    }

    private NNN findTheSkipOperator(GameState gameState, Set<Integer> possibleCellKeys, double currentRiskScore) {
        for (int cellKey : possibleCellKeys) {
            if(cellKey==0) {
                double strategicValue = calculateStrategicValue(currentRiskScore, SG_SKIP_BIAS);
                return new NNN(cellKey, strategicValue, SG_SKIP_PRECEDENCE);
            }
        }
        return null;
    }


    private NNN findTheFirstEqualCell(GameState gameState, Set<Integer> possibleCellKeys, double currentRiskScore, int currentAttackPower) {

        for (int cellKey : possibleCellKeys) {
            GameStateCell cellObj = gameState.getCell(cellKey);

            if (!cellObj.isEmpty() && cellObj.getDefendingTroopSize() == currentAttackPower) {
                double strategicValue = calculateStrategicValue(currentRiskScore, SG_EQUAL_BIAS);
                return new NNN(cellKey, strategicValue, SG_EQUAL_PRECEDENCE);
            }
        }
        return null;
    }

    private NNN findTheFirstLessStrongCell(GameState gameState, Set<Integer> possibleCellKeys, double currentRiskScore, int currentAttackPower) {
        //
        Integer lessStrongerCellKey = null;
        Integer lowestStrength = Integer.MAX_VALUE;

        for (int cellKey : possibleCellKeys) {
            GameStateCell cellObj = gameState.getCell(cellKey);

            if (!cellObj.isEmpty() && cellObj.getDefendingTroopSize() > currentAttackPower) {
                if (cellObj.getDefendingTroopSize() < lowestStrength) {
                    lowestStrength = cellObj.getDefendingTroopSize();
                    lessStrongerCellKey = cellKey;
                }
            }
        }

        //
        if (lessStrongerCellKey != null) {
            double strategicValue = calculateStrategicValue(currentRiskScore, SG_STRONGER_BIAS);
            return new NNN(lessStrongerCellKey, strategicValue, SG_STRONGER_PRECEDENCE);
        }

        return null;
    }


    private static class Operator {
        private int target;
        private int attackPower;

        public Operator(int target, int attackPower) {
            this.target = target;
            this.attackPower = attackPower;
        }

        public int getAttackPower() {
            return attackPower;
        }

        public int getTarget() {
            return target;
        }
    }
}

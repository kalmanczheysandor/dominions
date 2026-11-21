package hu.kalmancheysandor.applications.dominions.apis.ai.algorithm.simultan;

import java.util.*;

public class CompositeOperator {

    private int playerKey;
    private Integer targetIndex;
    private List<ElementaryOperator> combinations;

    public CompositeOperator(int playerKey, Integer targetIndex, List<ElementaryOperator> combinations) {
        this.playerKey = playerKey;
        this.targetIndex = targetIndex;
        this.combinations = combinations;
    }


    public int getPlayerKey() {
        return playerKey;
    }

    public Integer getTargetIndex() {
        return targetIndex;
    }

    public List<ElementaryOperator> getCombinations() {
        return combinations;
    }

    public static List<CompositeOperator> createMultiple(int leadPlayerKey, Collection<ElementaryOperator> unorderedOperators) {
        List<List<ElementaryOperator>> combinations = cartesianProduct(unorderedOperators);


        List<CompositeOperator> compositeOperators = new ArrayList<>();
        for (List<ElementaryOperator> combination : combinations) {
            compositeOperators.add(create(leadPlayerKey, combination));
        }
        return compositeOperators;
    }

    public static CompositeOperator create(int leadPlayerKey, List<ElementaryOperator> combination) {
        Integer targetIndex = null;
        boolean isFound = false;
        for (ElementaryOperator operator : combination) {
            if (operator.getPlayerKey() == leadPlayerKey) {
                isFound = true;
                targetIndex = operator.getTargetIndex();
                break;
            }
        }

        if(!isFound) {
            throw new RuntimeException("Lead player key '" + leadPlayerKey + "' not found in the combination of elementary operators.");
        }

        return new CompositeOperator(leadPlayerKey, targetIndex, combination);
    }


    private static List<List<ElementaryOperator>> cartesianProduct(Collection<ElementaryOperator> operators) {
        Map<Integer, List<ElementaryOperator>> groupedByPlayer = new LinkedHashMap<>();

        for (ElementaryOperator op : operators) {
            groupedByPlayer.computeIfAbsent(op.getPlayerKey(), k -> new ArrayList<>()).add(op);
        }

        List<List<ElementaryOperator>> result = new ArrayList<>();
        result.add(new ArrayList<>());

        for (List<ElementaryOperator> operatorsOfPlayer : groupedByPlayer.values()) {
            List<List<ElementaryOperator>> next = new ArrayList<>();

            for (List<ElementaryOperator> combination : result) {
                for (ElementaryOperator op : operatorsOfPlayer) {

                    List<ElementaryOperator> newCombination = new ArrayList<>(combination);

                    newCombination.add(op);
                    next.add(newCombination);
                }
            }

            result = next;
        }

        return result;
    }





    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CompositeOperator that = (CompositeOperator) o;
        return playerKey == that.playerKey && Objects.equals(targetIndex, that.targetIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerKey, targetIndex);
    }





    public static void main(String[] args) {
        List<ElementaryOperator> operators = Arrays.asList(
                new ElementaryOperator(1, 1),
                new ElementaryOperator(1, 2),
                new ElementaryOperator(2, 3),
                new ElementaryOperator(2, 4),
                new ElementaryOperator(4, 5)
        );

        List<CompositeOperator> compositeOperators = createMultiple(1,operators);

        for (CompositeOperator compositeOperator : compositeOperators) {
            System.out.println("--------------leadPlayer:" + compositeOperator.getPlayerKey() + " targetIndex:" + compositeOperator.getTargetIndex() + "-----------------------");

            for (ElementaryOperator op : compositeOperator.getCombinations()) {
                System.out.println("Player:" + op.getPlayerKey() + " Target:" + op.getTargetIndex());
            }
        }
    }


//    public static void main(String[] args) {
//        List<ElementaryOperator> operators = Arrays.asList(
//                new ElementaryOperator(1, 1),
//                new ElementaryOperator(1, 2),
//                new ElementaryOperator(2, 3),
//                new ElementaryOperator(2, 4),
//                new ElementaryOperator(4, 5)
//        );
//
//        List<List<ElementaryOperator>> combinations = cartesianProduct(operators);
//
//        for (List<ElementaryOperator> combination : combinations) {
//            System.out.println("-------------------------------------");
//            for (ElementaryOperator op : combination) {
//                System.out.println("Player:" + op.getPlayerKey() + " Target:" + op.getTargetIndex());
//            }
//        }
//    }
//


}

//
//    private static void ab(List<ElementaryOperator> elementaryOperators) {
//
//        for (ElementaryOperator operator : elementaryOperators) {
//            int targetIndex = operator.getTargetIndex();
//
//            List<List<ElementaryOperator>> group = result.computeIfAbsent(targetIndex, k -> new ArrayList<>());
//
//            for (List<ElementaryOperator> combination : combinations) {
//
//                List<ElementaryOperator> full = new ArrayList<>(combination);
//
//                full.add(operator);
//                group.add(full);
//            }
//        }
//
//        return result;
//    }

//
//    private static void generate(
//            int observedPlayerKey,
//            Map<Integer, Set<Integer>> playersAvailableSteps,
//            Set<Integer> playerKeys,
//            Map<Integer, Integer> stepsOfOneTurn,
//            List<Map<Integer, Integer>> combinationsList
//    ) {
//
//        // Parameter validations
//        assertIntNotNegative(observedPlayerKey, "observedPlayerKey");
//        assertObjectIsNotNull(playersAvailableSteps, "playersAvailableSteps");
//        assertObjectIsNotNull(playerKeys, "playerKeys");
//        assertObjectIsNotNull(stepsOfOneTurn, "stepsOfOneTurn");
//        assertObjectIsNotNull(combinationsList, "combinationsList");
//
//
//        Set<Integer> observedAvailableSteps = playersAvailableSteps.get(observedPlayerKey);
//        int nextPlayerKey = observedPlayerKey + 1;
//
//        for (Integer targetedCellKey : observedAvailableSteps) {
//            stepsOfOneTurn.put(observedPlayerKey, targetedCellKey);
//            if (playerKeys.contains(nextPlayerKey)) {
//                recursive(nextPlayerKey, playersAvailableSteps, playerKeys, stepsOfOneTurn, combinationsList);
//            } else { // att bottom deep
//                combinationsList.add(new HashMap<>(stepsOfOneTurn));
//            }
//        }
//    }
//
//
//    private static void recursive(int observedPlayerKey, Map<Integer, Set<Integer>> playersAvailableSteps, Set<Integer> playerKeys, Map<Integer, Integer> stepsOfOneTurn, List<Map<Integer, Integer>> combinationsList) {
/// / Parameter validations
//        assertIntNotNegative(observedPlayerKey, "observedPlayerKey");
//        assertObjectIsNotNull(playersAvailableSteps, "playersAvailableSteps");
//        assertObjectIsNotNull(playerKeys, "playerKeys");
//        assertObjectIsNotNull(stepsOfOneTurn, "stepsOfOneTurn");
//        assertObjectIsNotNull(combinationsList, "combinationsList");
//
//
//        Set<Integer> observedAvailableSteps = playersAvailableSteps.get(observedPlayerKey);
//        int nextPlayerKey = observedPlayerKey + 1;
//
//        for (Integer targetedCellKey : observedAvailableSteps) {
//            stepsOfOneTurn.put(observedPlayerKey, targetedCellKey);
//            if (playerKeys.contains(nextPlayerKey)) {
//                recursive(nextPlayerKey, playersAvailableSteps, playerKeys, stepsOfOneTurn, combinationsList);
//            } else { // att bottom deep
//                combinationsList.add(new HashMap<>(stepsOfOneTurn));
//            }
//        }
//    }

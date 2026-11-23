package hu.kalmancheysandor.applications.dominions.apis.ai.algorithm.simultan;

import java.util.*;

public class CompositeOperator {
    private int playerKey;
    private int targetIndex;
    private List<ElementaryOperator> combinations;

    public CompositeOperator(int playerKey, int targetIndex, List<ElementaryOperator> combinations) {
        this.playerKey = playerKey;
        this.targetIndex = targetIndex;
        this.combinations = combinations;
    }


    public int getPlayerKey() {
        return playerKey;
    }

    public int getTargetIndex() {
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
        int targetIndex =0;
        boolean isFound = false;

        for (ElementaryOperator operator : combination) {
            if (operator.getPlayerKey() == leadPlayerKey) {
                isFound = true;
                targetIndex = operator.getOperatorKey();
                break;
            }
        }

        if (!isFound) {
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
        return playerKey == that.playerKey && targetIndex == that.targetIndex;
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

        List<CompositeOperator> compositeOperators = createMultiple(1, operators);

        for (CompositeOperator compositeOperator : compositeOperators) {
            System.out.println("--------------leadPlayer:" + compositeOperator.getPlayerKey() + " targetIndex:" + compositeOperator.getTargetIndex() + "-----------------------");

            for (ElementaryOperator op : compositeOperator.getCombinations()) {
                System.out.println("Player:" + op.getPlayerKey() + " Target:" + op.getOperatorKey());
            }
        }
    }

}

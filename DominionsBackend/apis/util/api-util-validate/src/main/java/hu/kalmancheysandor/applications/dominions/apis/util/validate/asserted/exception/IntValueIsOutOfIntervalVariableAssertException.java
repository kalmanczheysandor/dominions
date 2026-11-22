package hu.kalmancheysandor.applications.dominions.apis.util.validate.asserted.exception;

public class IntValueIsOutOfIntervalVariableAssertException extends TVariableAssertException {

    private int max;
    private int min;
    private int value;

    public IntValueIsOutOfIntervalVariableAssertException(String variableName, int value, int min, int max) {
        super(variableName);
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public int getValue() {
        return value;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    @Override
    public String toString() {
        return "IntValueIsOutOfIntervalVariableAssertException{" +
                "max=" + max +
                ", min=" + min +
                ", value=" + value +
                '}';
    }
}

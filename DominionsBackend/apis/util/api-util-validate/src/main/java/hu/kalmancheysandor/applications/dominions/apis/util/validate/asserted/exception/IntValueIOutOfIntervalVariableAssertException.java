package hu.kalmancheysandor.applications.dominions.apis.util.validate.asserted.exception;

public class IntValueIOutOfIntervalVariableAssertException extends TVariableAssertException {

    private int max;
    private int min;

    public IntValueIOutOfIntervalVariableAssertException(String variableName, int min, int max) {
        super(variableName);
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}

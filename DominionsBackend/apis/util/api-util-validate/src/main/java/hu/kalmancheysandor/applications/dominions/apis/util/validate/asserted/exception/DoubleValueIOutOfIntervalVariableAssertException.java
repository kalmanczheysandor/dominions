package hu.kalmancheysandor.applications.dominions.apis.util.validate.asserted.exception;

public class DoubleValueIOutOfIntervalVariableAssertException extends TVariableAssertException {

    private double max;
    private double min;

    public DoubleValueIOutOfIntervalVariableAssertException(String variableName, double min, double max) {
        super(variableName);
        this.min = min;
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }
}

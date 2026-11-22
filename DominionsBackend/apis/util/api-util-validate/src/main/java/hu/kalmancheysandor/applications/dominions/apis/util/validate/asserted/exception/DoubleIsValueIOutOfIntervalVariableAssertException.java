package hu.kalmancheysandor.applications.dominions.apis.util.validate.asserted.exception;

public class DoubleIsValueIOutOfIntervalVariableAssertException extends TVariableAssertException {

    private double max;
    private double min;
    private double value;

    public DoubleIsValueIOutOfIntervalVariableAssertException(String variableName, double value, double min, double max) {
        super(variableName);
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "DoubleValueIOutOfIntervalVariableAssertException{" +
                "max=" + max +
                ", min=" + min +
                ", value=" + value +
                '}';
    }
}

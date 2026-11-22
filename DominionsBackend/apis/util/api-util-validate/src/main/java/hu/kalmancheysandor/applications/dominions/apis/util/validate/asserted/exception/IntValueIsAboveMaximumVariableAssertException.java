package hu.kalmancheysandor.applications.dominions.apis.util.validate.asserted.exception;

public class IntValueIsAboveMaximumVariableAssertException extends TVariableAssertException {

    private int maximum;

    public IntValueIsAboveMaximumVariableAssertException(String variableName, int maximum) {
        super(variableName);
        this.maximum = maximum;
    }

    public int getMaximum() {
        return maximum;
    }

    @Override
    public String toString() {
        return "IntValueIsAboveMaximumVariableAssertException{" +
                "maximum=" + maximum +
                '}';
    }
}

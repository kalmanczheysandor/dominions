package hu.kalmancheysandor.applications.dominions.apis.util.validate.asserted.exception;

public class IntValueIsUnderMinimumVariableAssertException extends TVariableAssertException {

    private int minimum;

    public IntValueIsUnderMinimumVariableAssertException(String variableName, int minimum) {
        super(variableName);
        this.minimum = minimum;
    }

    public int getMinimum() {
        return minimum;
    }

    @Override
    public String toString() {
        return "IntValueIsUnderMinimumVariableAssertException{" +
                "minimum=" + minimum +
                '}';
    }
}

package hu.kalmancheysandor.applications.dominions.apis.util.validate.asserted.exception;

public abstract class TVariableAssertException extends RuntimeException {

    private String variableName;
    public TVariableAssertException(String variableName) {
        this.variableName = variableName;
    }

    public String getVariableName() {
        return variableName;
    }
}


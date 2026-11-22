package hu.kalmancheysandor.applications.dominions.apis.util.validate.asserted;

import hu.kalmancheysandor.applications.dominions.apis.util.validate.asserted.exception.*;

import java.util.Collection;
import java.util.Map;

public class VariableAssert {
    public static void assertObjectIsNotNull(Object variable, String variableName) {
        if (variable == null) {
            throw new ObjectIsNullVariableAssertException(variableName);
        }
    }

    public static void assertIntNotNegative(int variable, String variableName) {
        if (variable < 0) {
            throw new IntValueIsNegativeVariableAssertException(variableName);
        }
    }

    public static void assertIntIsNotAboveMaximum(int variable, String variableName, int maximum) {
        if (variable > maximum) {
            throw new IntValueIsAboveMaximumVariableAssertException(variableName, maximum);
        }
    }
    public static void assertIntIsNotUnderMinimum(int variable, String variableName, int minimum) {
        if (variable < minimum) {
            throw new IntValueIsUnderMinimumVariableAssertException(variableName, minimum);
        }
    }


    public static void assertIntIsInInterval(int variable, String variableName, int minimum, int maximum) {
        if (variable < minimum || variable > maximum) {
            throw new IntValueIsOutOfIntervalVariableAssertException(variableName, variable, minimum, maximum);
        }
    }


    public static void assertDoubleIsInInterval(double variable, String variableName, double minimum, double maximum) {
        if (variable < minimum || variable > maximum) {
            throw new DoubleIsValueIOutOfIntervalVariableAssertException(variableName, variable, minimum, maximum);
        }
    }


    public static void assertCollectionNotEmpty(Collection<?> variable, String variableName) {
        if (variable == null || variable.isEmpty()) {
            throw new CollectionIsEmptyVariableAssertException(variableName);
        }
    }

    public static void assertMapNotEmpty(Map<?,?> variable, String variableName) {
        if (variable == null || variable.isEmpty()) {
            throw new MapIsEmptyVariableAssertException(variableName);
        }
    }
}

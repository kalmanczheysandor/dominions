package hu.kalmancheysandor.applications.dominions.apis.general.utils.validation.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private int minLength;
    private int maxLength;
    private boolean canBeEmpty;

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        minLength = constraintAnnotation.minLength();
        maxLength = constraintAnnotation.maxLength();
        canBeEmpty = constraintAnnotation.canBeEmpty();
    }

    @Override
    public boolean isValid(String field, ConstraintValidatorContext context) {

        // Allow null or empty passwords
        if (canBeEmpty == true && (field == null || field.isEmpty())) {
            return true;
        }

        if (field.length() < minLength) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Password must be at least " + minLength + " characters long")
                .addConstraintViolation();
            return false;
        } else if (maxLength > -1 && field.length() > maxLength) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Password must not be  longer than " + maxLength + " characters.")
                .addConstraintViolation();
            return false;
        }

        return true;
    }
}

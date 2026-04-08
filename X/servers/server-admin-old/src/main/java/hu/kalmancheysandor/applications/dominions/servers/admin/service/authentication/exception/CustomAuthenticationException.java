package hu.kalmancheysandor.applications.dominions.service.authentication.exception;

public class CustomAuthenticationException extends RuntimeException {
    public CustomAuthenticationException(String message) {
        super(message);
    }
}
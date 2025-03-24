package exception;
/**
 * Represents an exception when username entered is not valid
 */
public class InvalidCredentialsException extends Exception {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}

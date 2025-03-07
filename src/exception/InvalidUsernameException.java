package exception;
/**
 * Represents an exception when username entered is not valid
 */
public class InvalidUsernameException extends Exception {
    public InvalidUsernameException(String message) {
        super(message);
    }
}

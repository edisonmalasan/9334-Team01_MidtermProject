package exception;
/**
 * Represents an exception when a connection error is detected
 */
public class ConnectionException extends Exception{
    public ConnectionException(String message, Throwable cause){
        super(message, cause);
    }
}

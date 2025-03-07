package exception;
/**
 * Represents an exception when server is not detected when client is started
 */
public class ServerNotRunningException extends Exception {
    public ServerNotRunningException(String message) {
        super(message);
    }
}

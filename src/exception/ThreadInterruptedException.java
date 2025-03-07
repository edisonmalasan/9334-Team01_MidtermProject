package exception;
/**
 * Represents an exception when a thread is interrupted
 */
public class ThreadInterruptedException extends RuntimeException {
    public ThreadInterruptedException(String message, Throwable cause) {
        super(message, cause);
    }
}
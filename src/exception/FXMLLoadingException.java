package exception;
/**
 * Represents an exception when error is encountered when loading fxml file
 */
public class FXMLLoadingException extends Exception{
    public FXMLLoadingException(String fxmlFile, Throwable cause) {
        super("Failed to load " + fxmlFile, cause);
    }
}

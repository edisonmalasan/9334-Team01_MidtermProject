package common;

import javax.swing.JTextArea;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * a common logger to be used for response by appending logs to the GUI and a file.
 */
public class LogManager {
    private static LogManager instance;
    private JTextArea logArea;
    private static final String LOG_FILE = "server_client_logs.txt";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private LogManager() {}

    public static LogManager getInstance() {
        if (instance == null) {
            instance = new LogManager();
        }
        return instance;
    }

    public void setLogArea(JTextArea logArea) {
        this.logArea = logArea;
    }

    public void appendLog(String message) {
        String timestampedMessage = "[" + LocalDateTime.now().format(TIMESTAMP_FORMAT) + "] " + message;

        if (logArea != null) {
            logArea.append(timestampedMessage + "\n");
        }

        writeToFile(timestampedMessage);
    }

    private void writeToFile(String message) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(message + "\n");
        } catch (IOException e) {
            System.err.println("Failed to write log: " + e.getMessage());
        }
    }
}

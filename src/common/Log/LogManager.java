package common.Log;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A common logger to be used for response by appending logs to the GUI and a file.
 */
public class LogManager {
    private static LogManager instance;
    private JTextArea logArea;
    private static final String LOG_FILE = "server_client_logs.txt";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LogManager() {}

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
            SwingUtilities.invokeLater(() -> {
                logArea.append(timestampedMessage + "\n");
                logArea.setCaretPosition(logArea.getDocument().getLength());
            });
        } else {
            System.err.println("LogArea is not set in LogManager!");
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

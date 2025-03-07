package common;
/**
 * Logs for the server
 */
import java.io.File;
import java.io.IOException;
import java.util.logging.*;

public class LoggerSetup {
    public static Logger setupLogger(String loggerName, String filePath) {
        Logger logger = Logger.getLogger(loggerName);
        logger.setUseParentHandlers(false);

        try {
            File logFile = new File(filePath);
            File logDir = logFile.getParentFile();

            if (logDir != null && !logDir.exists()) {
                logDir.mkdirs();
            }

            FileHandler fileHandler = new FileHandler(filePath, true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setEncoding("UTF-8");
            fileHandler.setLevel(Level.ALL);

            logger.addHandler(fileHandler);

            for (Handler handler : logger.getHandlers()) {
                if (handler instanceof ConsoleHandler) {
                    logger.removeHandler(handler);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return logger;
    }
}

package common;
/**
 * Formats strings
 */
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class AnsiFormatter extends Formatter {
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String GREEN = "\u001B[32m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    @Override
    public String format(LogRecord record) {
        String color;

        if (record.getLevel() == Level.SEVERE) {
            color = RED;
        } else if (record.getLevel() == Level.WARNING) {
            color = YELLOW;
        } else if (record.getLevel() == Level.INFO) {
            color = GREEN;
        } else if (record.getLevel() == Level.FINE) {
            color = BLUE;
        } else {
            color = CYAN;
        }

        return String.format(
                "%s[%s] %s%s%n",
                color,
                record.getLevel().getName(),
                record.getMessage(),
                RESET
        );
    }

    public static void enableColorLogging(Logger logger) {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new AnsiFormatter());
        logger.setUseParentHandlers(false);
        logger.addHandler(handler);
    }
}

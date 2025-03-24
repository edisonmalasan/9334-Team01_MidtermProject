package Client.connection;
import common.AnsiFormatter;
import common.LoggerSetup;
import exception.ConnectionException;
import utility.BombGameServer;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;

import static common.Protocol.IP_ADDRESS;
import static common.Protocol.PORT_NUMBER;

public class ClientConnection {
    private static ClientConnection instance;
    public static BombGameServer bombGameServer;
    public static String fetchIPAddress;

    private static final Logger logger = LoggerSetup.setupLogger("ClientLogger", System.getProperty("user.dir") + "/src/Client/Log/client.log");

    static {
        AnsiFormatter.enableColorLogging(logger);
        try {
            fetchIPAddress = InetAddress.getLocalHost().getHostAddress();
            logger.info("Detected IP Address: " + fetchIPAddress);
        } catch (IOException e) {
            logger.warning("Failed to detect local IP.");
            fetchIPAddress = "127.0.0.1"; // make ip address default
        }
    }

    private ClientConnection() throws ConnectionException {
        try {
            logger.info("\nClientConnection: Attempting to connect to server...");

            while (true) {
                try {
                    Registry registry = LocateRegistry.getRegistry(fetchIPAddress, 1099);
                    bombGameServer = (BombGameServer) registry.lookup("server");
                    break;
                } catch (Exception e) {
                    logger.warning("⚠ Server is not running. The client will continue in offline mode.");
                    logger.warning("⚠ Connection failed. Retrying in 5 seconds...");

                    try {
                        Thread.sleep(5000);  // Retry after 5 seconds
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            }
            logger.info("\nClientConnection: Connected to server successfully!");
        } catch (Exception e) {
            logger.severe("\nClientConnection: Connection to server failed!");
            throw new ConnectionException("Error connecting to the server.", e);
        }

    }

    public static ClientConnection getInstance() throws ConnectionException {
        if (instance == null) {
            instance = new ClientConnection();
        }
        return instance;
    }

    public void connect() {
        if (bombGameServer == null) {
            try {
                Registry registry = LocateRegistry.getRegistry(fetchIPAddress, 1099);
                bombGameServer = (BombGameServer) registry.lookup("server");
                logger.info("✅ Client successfully connected to the server.");
            } catch (Exception e) {
                logger.warning("⚠ Server is not running. The client will continue in offline mode.");
            }
        }
    }

    public void close() {
        try {

            logger.info("\nClientConnection: Connection closed.");
        } catch (Exception e) {
            logger.severe("\nClientConnection: Error closing connection: " + e.getMessage());
        }
    }
}

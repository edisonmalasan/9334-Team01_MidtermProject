package Client.common.connection;
import common.Log.AnsiFormatter;
import common.Log.LoggerSetup;
import common.Protocol;
import exception.ConnectionException;
import utility.BombGameServer;

import java.io.*;
import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;

public class ClientConnection {
    private static ClientConnection instance;
    public static BombGameServer bombGameServer;
    public static String iPAddress = Protocol.IP_ADDRESS;

    private static final Logger logger = LoggerSetup.setupLogger("ClientLogger", System.getProperty("user.dir") + "/src/Client/Log/client.log");

    private ClientConnection() throws ConnectionException {
        try {
            logger.info("\nClientConnection: Attempting to connect to server...");

            while (true) {
                try {
                    Registry registry = LocateRegistry.getRegistry(iPAddress, 1099);
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
                Registry registry = LocateRegistry.getRegistry(iPAddress, 1099);
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

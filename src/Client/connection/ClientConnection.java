package Client.connection;
import common.AnsiFormatter;
import common.LoggerSetup;
import exception.ConnectionException;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

import static common.Protocol.IP_ADDRESS;
import static common.Protocol.PORT_NUMBER;

public class ClientConnection {
    private static ClientConnection instance;
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private static final Logger logger = LoggerSetup.setupLogger("ClientLogger", System.getProperty("user.dir") + "/src/Client/Log/client.log");

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    private ClientConnection() throws ConnectionException {
        try {
            logger.info("\nClientConnection: Attempting to connect to server...");

            socket = new Socket(IP_ADDRESS, PORT_NUMBER);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());

            logger.info("\nClientConnection: Connected to server successfully!");

        } catch (IOException e) {
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

    public void connect() throws ConnectionException {
        if (socket == null || socket.isClosed()) {
            try {
                socket = new Socket(IP_ADDRESS, PORT_NUMBER);
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectInputStream = new ObjectInputStream(socket.getInputStream());
                logger.info("\nClientConnection: Connected to the server.");
            } catch (IOException e) {
                logger.severe("\nClientConnection: Error connecting to the server: " + e.getMessage());
                throw new ConnectionException("Error connecting to the server.", e);
            }
        }
    }

    public void sendObject(Object obj) throws IOException {
        if (objectOutputStream == null) {
            throw new IOException("❌ ERROR: Object is NULL! Cannot send data.");
        }
        logger.info("\nClientConnection: Sending object: " + obj);
        objectOutputStream.writeObject(obj);
        objectOutputStream.flush();
    }

    public Object receiveObject() throws IOException, ClassNotFoundException {
        logger.info("\nClientConnection: Waiting to receive object from server...");

        Object obj = objectInputStream.readObject();

        logger.info("\nClientConnection: Received object: " + obj);
        return obj;
    }

    public void close() {
        try {
            if (socket != null) socket.close();
            if (objectInputStream != null) objectInputStream.close();
            if (objectOutputStream != null) objectOutputStream.close();
            logger.info("\nClientConnection: Connection closed.");
        } catch (IOException e) {
            logger.severe("\nClientConnection: Error closing connection: " + e.getMessage());
        }
    }
}

package Client.Admin.connection;
import common.AnsiFormatter;
import common.LoggerSetup;
import exception.ConnectionException;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

import static common.Protocol.IP_ADDRESS;
import static common.Protocol.PORT_NUMBER;

public class AdminClient {
    private static AdminClient instance;
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private static final Logger logger = LoggerSetup.setupLogger("AdminLogger", System.getProperty("user.dir") + "/src/Admin/Log/admin.log");

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    private AdminClient() throws ConnectionException {
        try {
            logger.info("\nAdminConnection: Attempting to connect to the server...");

            socket = new Socket(IP_ADDRESS, PORT_NUMBER);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());

            logger.info("\nAdminConnection: Connected to the server successfully!");

        } catch (IOException e) {
            logger.severe("\nAdminConnection: Connection to server failed!");
            throw new ConnectionException("Error connecting to the server.", e);
        }
    }

    public static AdminClient getInstance() throws ConnectionException {
        if (instance == null) {
            instance = new AdminClient();
        }
        return instance;
    }

    public void sendObject(Object obj) throws IOException {
        if (objectOutputStream == null) {
            throw new IOException("❌ ERROR: ObjectOutputStream is NULL! Cannot send data.");
        }
        logger.info("\nAdminConnection: Sending object: " + obj);
        objectOutputStream.writeObject(obj);
        objectOutputStream.flush();
    }

    public Object receiveObject() throws IOException, ClassNotFoundException {
        logger.info("\nAdminConnection: Waiting to receive object from server...");
        Object obj = objectInputStream.readObject();
        logger.info("\nAdminConnection: Received object: " + obj);
        return obj;
    }

    public void close() {
        try {
            if (socket != null) socket.close();
            if (objectInputStream != null) objectInputStream.close();
            if (objectOutputStream != null) objectOutputStream.close();
            logger.info("\nAdminConnection: Connection closed.");
        } catch (IOException e) {
            logger.severe("\nAdminConnection: Error closing connection: " + e.getMessage());
        }
    }

}


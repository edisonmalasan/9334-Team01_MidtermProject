package Client.Admin.connection;

import common.Log.AnsiFormatter;
import common.Log.LoggerSetup;
import exception.ConnectionException;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

import static common.Protocol.IP_ADDRESS;
import static common.Protocol.PORT_NUMBER;

public class AdminClient {
    private static AdminClient instance;
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;

    private static final Logger logger = LoggerSetup.setupLogger("AdminLogger", System.getProperty("user.dir") + "/src/Admin/Log/admin.log");

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    public AdminClient() throws ConnectionException {
        try {
            logger.info("AdminClient: Connecting to the server...");

            socket = new Socket(IP_ADDRESS, PORT_NUMBER);
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            logger.info("AdminClient: Connected successfully.");

        } catch (IOException e) {
            logger.severe("AdminClient: Connection failed: " + e.getMessage());
            throw new ConnectionException("Error connecting to the server.", e);
        }
    }

    public static AdminClient getInstance() throws ConnectionException {
        if (instance == null) {
            instance = new AdminClient();
        }
        return instance;
    }

    /**
     * Sends a request to the server and receives a response.
     * @param requestJson The request in JSON format.
     * @return The response from the server as a JSON string.
     */
    public String sendRequest(String requestJson) {
        try {
            if (output == null || input == null) {
                throw new IOException("I/O streams are not initialized.");
            }

            logger.info("AdminClient: Sending request: " + requestJson);
            output.println(requestJson);

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = input.readLine()) != null) {
                response.append(line);
            }

            logger.info("AdminClient: Received response: " + response.toString());
            return response.toString();
        } catch (IOException e) {
            logger.severe("AdminClient: Error sending request: " + e.getMessage());
            return null;
        }
    }

   // /**
    //     * Sends an authentication request to the server.
    //     * @param username Admin username.
    //     * @param password Admin password.
    //     * @return True if authenticated, false otherwise.
    //     */
    //    public boolean authenticate(String username, String password) {
    //        try {
    //            JSONObject authRequest = new JSONObject();
    //            authRequest.put("action", "ADMIN_LOGIN");
    //            authRequest.put("username", username);
    //            authRequest.put("password", password);
    //
    //            String response = sendRequest(authRequest.toString());
    //            if (response != null) {
    //                JSONObject jsonResponse = new JSONObject(response);
    //                return jsonResponse.getBoolean("authenticated");
    //            }
    //        } catch (Exception e) {
    //            logger.severe("AdminClient: Authentication failed: " + e.getMessage());
    //        }
    //        return false;
    //    }

    /**
     * Closes the connection to the server.
     */
    public void close() {
        try {
            if (socket != null) socket.close();
            if (input != null) input.close();
            if (output != null) output.close();
            logger.info("AdminClient: Connection closed.");
        } catch (IOException e) {
            logger.severe("AdminClient: Error closing connection: " + e.getMessage());
        }
    }
}

package Redundant;
/**
 * Handles the server
 */

import common.Log.AnsiFormatter;
import Server.controller.LeaderboardControllerServer;
import Server.handler.ClientHandler;
import common.Log.LoggerSetup;

import static common.Protocol.PORT_NUMBER;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class ServerHandler {
    private QuestionBankModel questionBank;
    private LeaderboardControllerServer leaderboard;
    private ServerSocket serverSocket;
    private static final Logger logger = LoggerSetup.setupLogger("ClientLogger", System.getProperty("user.dir") + "/src/Server/Log/server.log");

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    public ServerHandler(QuestionBankModel questionBank, LeaderboardControllerServer leaderboard) {
        this.questionBank = questionBank;
        this.leaderboard = leaderboard;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT_NUMBER);
            logger.info("Server Handler: ✅ Server started on port " + PORT_NUMBER);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                logger.info("Server Handler: 🔗 New client connected: " + clientSocket.getInetAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket, leaderboard);

                logger.info("Server Handler: 🚀 Starting client thread for: " + clientSocket.getInetAddress());

                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            logger.severe("Server error: " + e.getMessage());
        }
    }

    public void stop() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();  // Close the server socket
                logger.info("Server stopped.");
            }
        } catch (IOException e) {
            logger.severe("Error stopping the server: " + e.getMessage());
        }
    }
}

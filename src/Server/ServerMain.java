package Server;
/**
 * Runs the server
 */

import Server.controller.AdminViewController;
import Server.view.AdminView;
import common.AnsiFormatter;
import Server.model.QuestionBankModel;
import Server.controller.LeaderboardControllerServer;
import utility.BombGameServer;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.logging.Logger;

import static common.Protocol.PORT_NUMBER;

public class ServerMain {
    private static final Logger logger = Logger.getLogger(ServerMain.class.getName());
    private static ServerHandler serverHandler;
    private static Thread serverThread;
    private static boolean isServerRunning = false;

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    public static void main(String[] args) {
        QuestionBankModel questionBank = new QuestionBankModel();
        LeaderboardControllerServer leaderboardControllerServer = new LeaderboardControllerServer();

        logger.info("Server Main: 📖 QuestionBank loaded: " + questionBank.getQuestions().size() + " questions.");
        logger.info("Server Main: 🏆 Leaderboard controller initialized.");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    startServer(questionBank, leaderboardControllerServer);
                    break;
                case 2:
                    stopServer();
                    break;
                case 3:
                    stopServer();
                    logger.info("Server Main: Exiting...");
                    System.exit(0);
                    break;
                default:
                    logger.warning("Invalid choice. Please try again.");
            }
        }
    }

    private static void printMenu() {
        // Print the Server Menu options
        logger.info("\n============================");
        logger.info("\n=======Server Menu:=========");
        logger.info("\n============================");
        logger.info("1. Start Server               ");
        logger.info("2. Stop Server                ");
        logger.info("3. Exit                       ");
        logger.info("\n============================");
        logger.info("Please enter your choice:");

    }

    private static void startServer(QuestionBankModel questionBank, LeaderboardControllerServer leaderboardControllerServer) {
        try {
            BombGameServer server = new BombGameServerImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("server", server);
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("[INFO] Server started successfully.");
        logger.info("[INFO] Server Handler: ✅ Server started on port " + PORT_NUMBER);
    }

    private static void stopServer() {
        if (!isServerRunning) {
            logger.info("Server is not running.");
            return;
        }

        try {
            serverThread.interrupt();
            serverHandler.stop();
            isServerRunning = false;
            logger.info("Server stopped successfully.");
        } catch (Exception e) {
            logger.severe("Error stopping the server: " + e.getMessage());
        }
    }
}

package Server.controller;

import Server.BombGameServerImpl;
import Server.view.ServerView;
import utility.BombGameServer;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import static common.Protocol.PORT_NUMBER;

public class ServerController {
    private ServerView view;
    private Registry registry;
    private boolean isServerRunning = false;
    private static final Logger logger = Logger.getLogger(ServerController.class.getName());
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ServerController(ServerView view) {
        this.view = view;
        attachEventListeners();
    }

    private void attachEventListeners() {
        view.getStartButton().addActionListener(e -> startServer());
        view.getStopButton().addActionListener(e -> stopServer());
        view.getRestartButton().addActionListener(e -> restartServer());
        view.getClearLogButton().addActionListener(e -> clearLog());
    }

    private void startServer() {
        if (isServerRunning) {
            view.appendToLog("[ "+getTimestamp() + " ]"+ " --- Server is already running.\n");
            return;
        }

        try {
            BombGameServer server = new BombGameServerImpl();

            try {
                registry = LocateRegistry.createRegistry(1099);
                registry.rebind("server", server);
                isServerRunning = true;
                view.appendToLog("[ "+getTimestamp() + " ]"+ " --- New RMI registry created on port 1099.\n");
            } catch (Exception e) {
                view.appendToLog("[ "+getTimestamp() + " ]"+ " --- RMI registry already exists. Connecting...\n");
            }



            view.appendToLog("[ "+getTimestamp() + " ]"+ " --- Server started successfully on port " + 1099 + ".\n");
        } catch (Exception e) {
            view.appendToLog("[ "+getTimestamp() + " ]"+ " --- Failed to start server: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    private void stopServer() {
        if (!isServerRunning) {
            view.appendToLog("[ "+getTimestamp() + " ]"+ " --- Server is not running.\n");
            return;
        }

        try {
            registry.unbind("server");
            UnicastRemoteObject.unexportObject(registry, true);
            isServerRunning = false;

            view.appendToLog("[ "+getTimestamp() + " ]"+ " --- Server stopped successfully.\n");
        } catch (Exception e) {
            view.appendToLog("[ "+getTimestamp() + " ]"+ " --- Error stopping the server: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    private void restartServer() {
        view.appendToLog("[ "+getTimestamp() + " ]"+ " --- Restarting server...\n");
        stopServer();
        startServer();
    }

    private void clearLog() {
        view.clearLog();
    }

    private String getTimestamp() {
        return LocalDateTime.now().format(TIMESTAMP_FORMAT);
    }
}

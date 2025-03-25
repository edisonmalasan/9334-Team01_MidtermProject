package Server.controller;

import Server.BombGameServerImpl;
import Server.view.ServerView;
import common.Log.LogManager;
import utility.BombGameServer;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerController {
    private ServerView view;
    private Registry registry;
    private boolean isServerRunning = false;
    private LogManager logger = LogManager.getInstance();

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
            logger.appendLog("Server is already running.");
            return;
        }

        try {
            BombGameServer server = new BombGameServerImpl(view);

            try {
                registry = LocateRegistry.createRegistry(1099);
                registry.rebind("server", server);
                isServerRunning = true;
                logger.appendLog("New RMI registry created on port 1099.");
            } catch (Exception e) {
                logger.appendLog("RMI registry already exists. Connecting...");
                registry = LocateRegistry.getRegistry(1099);
                registry.rebind("server", server);
                isServerRunning = true;
            }

            logger.appendLog("Server started successfully on port 1099.");
        } catch (Exception e) {
            logger.appendLog("Failed to start server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void stopServer() {
        if (!isServerRunning) {
            logger.appendLog("Server is not running.");
            return;
        }

        try {
            registry.unbind("server");
            UnicastRemoteObject.unexportObject(registry, true);
            isServerRunning = false;
            logger.appendLog("Server stopped successfully.");
        } catch (Exception e) {
            logger.appendLog("Error stopping the server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void restartServer() {
        logger.appendLog("Restarting server...");
        stopServer();
        startServer();
    }

    private void clearLog() {
        view.clearLog();
    }
}
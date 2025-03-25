package Server;

import Server.controller.ServerController;
import Server.view.ServerView;
import Server.BombGameServerImpl;
import common.Log.LogManager;

import java.rmi.RemoteException;

public class ServerMain {
    public static void main(String[] args) {
        ServerView view = new ServerView();
        ServerController controller = new ServerController(view);

        try {
            BombGameServerImpl server = new BombGameServerImpl(view);
        } catch (RemoteException e) {
            LogManager.getInstance().appendLog("Failed to create server: " + e.getMessage());
            e.printStackTrace();
        }

        view.setVisible(true);
    }
}

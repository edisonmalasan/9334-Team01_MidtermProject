package Server;

import Server.controller.ServerController;
import Server.view.ServerView;

public class ServerMain {
    public static void main(String[] args) {
        ServerView view = new ServerView();
        ServerController controller = new ServerController(view);

        view.setVisible(true);
    }
}
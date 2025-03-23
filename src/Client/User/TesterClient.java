package Client.User;

import common.Response;
import utility.BombGameServer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

//Client class for testing

public class TesterClient {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            BombGameServer bombGameServer = (BombGameServer) registry.lookup("server");
            Response response = bombGameServer.getQuestionsList();
            Response response1 = bombGameServer.getQuestionsPerCategory("Algebra");
            System.out.println(response1.getData());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

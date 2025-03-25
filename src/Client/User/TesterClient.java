package Client.User;

import common.Response;
import common.model.PlayerModel;
import common.model.QuestionModel;
import utility.BombGameServer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//Client class for testing

public class TesterClient {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            BombGameServer bombGameServer = (BombGameServer) registry.lookup("server");
            Response response = bombGameServer.getQuestionsList();

            //testing if questions can be loaded from questions.json
            Response response1 = bombGameServer.getQuestionsPerCategory("LOGIC");
            //testing addQuestion
            List<String> choices = Arrays.asList("YES", "NO");
////            Response response2 = bombGameServer.addQuestion(new QuestionModel("LOGIC", "All roses are flowers. Are roses plants?", choices, "YES", 1));
//            PlayerModel playerModel = new PlayerModel("Hello","123","PLAYER",10,10);
//            bombGameServer.updatePlayerScore(playerModel);
//            System.out.println(response1.getData());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package Server;

import Server.controller.JSONStorageController;
import utility.Callback;
import utility.PlayerModel;
import Server.controller.QuestionController;
import Server.controller.XMLStorageController;
import Server.handler.ClientHandler;
import utility.LeaderboardEntryModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import common.AnsiFormatter;
import common.Response;
import common.model.QuestionModel;
import utility.BombGameServer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.logging.Logger;

public class BombGameServerImpl extends UnicastRemoteObject implements BombGameServer {

    private String fileName;
    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());
    private List<PlayerModel> playerList = new ArrayList<>();
    private Map<String, Callback> playerCallbacks = new Hashtable<>();

    static {
        AnsiFormatter.enableColorLogging(logger);
    }
    public BombGameServerImpl() throws RemoteException, FileNotFoundException {
        Gson gson = new Gson();
        Reader fileReader = new FileReader(System.getProperty("user.dir")+ "/data/players.json");
        Type playerListType = new TypeToken<List<PlayerModel>>(){}.getType();
        playerList = gson.fromJson(fileReader, playerListType);
    }

    @Override
    public Response getQuestionsPerCategory(String category) throws RemoteException {
        logger.info("Server received question request for category: " + category);

        QuestionController questionController = new QuestionController();
        List<QuestionModel> questions = questionController.getQuestionsByCategory(category);

        if (questions.isEmpty()) {
            logger.warning("No questions found for category: " + category);
            return new Response(false, "No questions found for category: " + category, null);
        }

        logger.info("Question retrieved successfully for category: " + category);
         return new Response(true, "Question retrieved successfully.", questions);
    }

    @Override
    public Response getLeaderboards(String leaderboardType) throws RemoteException {
        try {
            List<LeaderboardEntryModel> leaderboard = getLeaderboardEntries(leaderboardType);

            if (leaderboard == null) {
                logger.severe("Received null player data.");
               return new Response(false, "Received null player data.", null);
            }

            logger.info("Returning leaderboard data.");
            return new Response(true, "Leaderboard displayed successfully.", leaderboard);
        } catch (Exception e) {
            logger.severe("Error retrieving leaderboard: " + e.getMessage());
            return new Response(false, "Error retrieving leaderboard: " + e.getMessage(), null);
        }
    }
    // Method for populating and arranging list for leaderboards
    private List<LeaderboardEntryModel> getLeaderboardEntries(String leaderboardType) {
        List<LeaderboardEntryModel> leaderboard = new ArrayList<>();
        if (Objects.equals(leaderboardType, "classic")) {
            for (PlayerModel player : playerList) {
                LeaderboardEntryModel leaderboardEntry = new LeaderboardEntryModel(player.getUsername(), player.getClassicScore());
                leaderboard.add(leaderboardEntry);
            }
        } else {
            for (PlayerModel player : playerList) {
                LeaderboardEntryModel leaderboardEntry = new LeaderboardEntryModel(player.getUsername(), player.getEndlessScore());
                leaderboard.add(leaderboardEntry);
            }
        }
        leaderboard.sort((entry1, entry2) -> {
            int scoreComparison = Integer.compare(entry2.getScore(), entry1.getScore());
            if (scoreComparison == 0) {
                // If scores are equal, sort alphabetically by player name
                return entry1.getPlayerName().compareTo(entry2.getPlayerName());
            }
            return scoreComparison;
        });
        return leaderboard;
    }

    @Override
    public Response getQuestionsList() throws RemoteException {
        logger.info("Server received request for question list");

        QuestionController questionController = new QuestionController();
        List<QuestionModel> allQuestions = questionController.getQuestionsList();

        if (allQuestions.isEmpty()) {
            logger.warning("No question list found.");
            return new Response(false, "No question list found", null);
        }

        logger.info("Question list retrieved successfully");
        return new Response(true, "Question list retrieved successfully.", allQuestions);
    }

    @Override
    public Response updatePlayerScore(PlayerModel player) throws RemoteException {
        try {
            if (player == null) {
                logger.severe("Received null player data.");
                return new Response(false, "Received null player data.", null);
            }
            logger.info("Updating player score: " + player.getUsername());
            JSONStorageController.updatePlayerScore(player);

            logger.info("Player score updated successfully.");
            return new Response(true, "Player score updated successfully.", null);
        } catch (Exception e) {
            logger.severe("Error updating player score: " + e.getMessage());
            return new Response(false, "Error updating player score: " + e.getMessage(), null);
        }
    }

    public void addQuestion(QuestionModel question) throws RemoteException {
        try {
            logger.info("Server received request for removing question in database");
            if (question == null) {
                logger.severe("Received null question data.");
               // return new Response(false, "Received null question data.", null);
            }

            fileName = "data/questions.xml";
            List<QuestionModel> questionList = XMLStorageController.loadQuestionsFromXML(fileName);

            boolean found = false;
            for (QuestionModel questionModel : questionList) {
                if (question.equals(questionModel)) {
                    logger.info("Removed question from database: " + questionModel.getCategory() + " with text: " + questionModel.getQuestionText());
                    questionList.remove(questionModel);
                    found = true;
                    break;
                }
            }
            if (!found) {
                questionList.add(question);
                logger.info("Question added to database: " + question.getCategory() + " with text: " + question.getQuestionText());
            }

            XMLStorageController.saveQuestionsToXML(fileName, questionList);
            logger.info("Question database updated successfully.");
          //  return new Response(true, "Question database updated successfully.", null);
        } catch (Exception e) {
            logger.severe("Error updating: " + e.getMessage());
          //  return new Response(false, "Error updating question database: " + e.getMessage(), null);
        }
    }

    public Response removeFromLeaderboard(LeaderboardEntryModel leaderboardEntry, String leaderboardType) throws RemoteException {
        try {
            logger.info("Server received request for removal of entry in leaderboard");
            if (leaderboardEntry == null) {
                logger.severe("Received null leaderboard entry data.");
                return new Response(false, "Received null leaderboard entry data.", null);
            }


            fileName = "data/classic_leaderboard.xml";
            List<LeaderboardEntryModel> leaderboard = getLeaderboardEntries(leaderboardType);

            for (LeaderboardEntryModel entry : leaderboard) {
                if (leaderboardEntry.equals(entry)) {
                    logger.info("Removed entry from leaderboard: " + entry.getPlayerName() + " with score: " + entry.getScore());
                    leaderboard.remove(entry);
                    break;
                }
            }
            JSONStorageController.saveLeaderboardToJSON(fileName, leaderboard);
            logger.info("Leaderboard updated successfully.");
            return new Response(true, "Leaderboard updated successfully.", null);
        } catch (Exception e) {
            logger.severe("Error removing entry from leaderboard: " + e.getMessage());
            return new Response(false, "Error removing entry from leaderboard: " + e.getMessage(), null);
        }
    }
    @Override
    public void login(Callback callback) throws RemoteException {
        PlayerModel player = callback.getPlayer();

        if (playerCallbacks.containsValue(callback)) {
            System.out.println("Player already logged in.");
        } else {
            playerCallbacks.put(player.getUsername(), callback);
            System.out.println("Log In: " + player.getUsername());
            System.out.println("Online : [");
            Set<String> usernames = playerCallbacks.keySet();
            int counter = 1;
            for (String username : usernames) {
                System.out.println(username + (counter++ == usernames.size() ? "" : ", "));
                playerCallbacks.get(username).loginCall(player);
            }
            System.out.println("]");
        }
    }

    public void register() throws RemoteException {
    }
}

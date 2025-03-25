package Server;

import Server.controller.JSONStorageController;
import utility.Callback;
import common.model.PlayerModel;
import Server.controller.QuestionController;
import Server.handler.ClientHandler;
import utility.LeaderboardEntryModel;
import com.google.gson.Gson;
import common.Log.AnsiFormatter;
import common.Response;
import common.model.QuestionModel;
import utility.BombGameServer;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.logging.Logger;

public class BombGameServerImpl extends UnicastRemoteObject implements BombGameServer {

    private String fileName;
    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());
    private List<PlayerModel> playerList = new ArrayList<>();
    private Map<String, Callback> playerCallbacks = new Hashtable<>();
    private Gson gson = new Gson();

    static {
        AnsiFormatter.enableColorLogging(logger);
    }
    public BombGameServerImpl() throws RemoteException {
        playerList = JSONStorageController.loadPlayersFromJSON();
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
            JSONStorageController.updatePlayerScore(playerList, player);

            logger.info("Player score updated successfully.");
            return new Response(true, "Player score updated successfully.", null);
        } catch (Exception e) {
            logger.severe("Error updating player score: " + e.getMessage());
            return new Response(false, "Error updating player score: " + e.getMessage(), null);
        }
    }

    public Response updateQuestion(QuestionModel question) throws RemoteException {
        try {
            logger.info("Server received request for removing question in database");
            if (question == null) {
                logger.severe("Received null question data.");
                return new Response(false, "Received null question data.", null);
            }

            List<QuestionModel> questionList = JSONStorageController.loadQuestionsFromJSON();

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


            JSONStorageController.saveQuestionToJSON(questionList);
            logger.info("Question database updated successfully.");
            return new Response(true, "Question database updated successfully.", null);
        } catch (Exception e) {
            logger.severe("Error updating: " + e.getMessage());
            return new Response(false, "Error updating question database: " + e.getMessage(), null);
        }
    }

    public Response removeFromLeaderboard(LeaderboardEntryModel leaderboardEntry, String leaderboardType) throws RemoteException {
        try {
            logger.info("Server received request for removal of entry in leaderboard");
            if (leaderboardEntry == null) {
                logger.severe("Received null leaderboard entry data.");
                return new Response(false, "Received null leaderboard entry data.", null);
            }

            for (PlayerModel player : playerList) {
                if (leaderboardEntry.getPlayerName().equals(player.getUsername())) {
                    logger.info("Removed entry from player list: " + player.getUsername());
                    playerList.remove(player);
                    break;
                }
            }

            Writer writer = new FileWriter(System.getProperty("user.dir")+ "/data/players.json");
            gson.toJson(playerList, writer);

            logger.info("Player list updated successfully.");
            return new Response(true, "Player list updated successfully.", null);
        } catch (Exception e) {
            logger.severe("Error removing player from player list " + e.getMessage());
            return new Response(false, "Error removing entry from leaderboard: " + e.getMessage(), null);
        }
    }

    @Override
    public Response getPlayerList() {
        try {
            if (playerList == null) {
                logger.severe("Received null player data.");
                return new Response(false, "Received null player data.", null);
            }
            logger.info("Returning player list data.");
            return new Response(true, "Player list displayed successfully.", playerList);
        } catch (Exception e) {
            logger.severe("Error retrieving player list: " + e.getMessage());
            return new Response(false, "Error retrieving player list: " + e.getMessage(), null);
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
            for (String playerUsername : usernames) {
                System.out.println(playerUsername + (counter++ == usernames.size() ? "" : ", "));
                playerCallbacks.get(playerUsername).loginCall(player);
            }
            System.out.println("]");
        }
    }

    @Override
    public PlayerModel getPlayer(String username, String password) {
        for (PlayerModel player : playerList) {
            if (player.getUsername().equalsIgnoreCase(username) && player.getPassword().equalsIgnoreCase(password)) {
                return player;
            }
        }
        return null;
    }
    @Override
    public void register(String username, String password) throws RemoteException {
        try {
            PlayerModel player = new PlayerModel(username, password, "PLAYER", 0, 0);
            playerList.add(player);
            JSONStorageController.savePlayerListToJSON(playerList);
            logger.info("Registering player to database.");
        } catch (Exception e) {
            logger.severe("Error registering player: " + e.getMessage());
        }
    }
}

package Server;

import Server.controller.JSONStorageController;
import utility.Callback;
import common.model.PlayerModel;
import Server.controller.QuestionController;
import utility.LeaderboardEntryModel;
import com.google.gson.Gson;
import common.Log.AnsiFormatter;
import common.Response;
import common.model.QuestionModel;
import utility.BombGameServer;
import common.Log.LogManager;
import Server.view.ServerView;
import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class BombGameServerImpl extends UnicastRemoteObject implements BombGameServer {

    private ServerView serverView;
    private static final Logger logger = Logger.getLogger(BombGameServerImpl.class.getName());
    private List<PlayerModel> playerList = new ArrayList<>();
    private final Lock lock = new ReentrantLock();
    private Map<String, Callback> playerCallbacks = new Hashtable<>();
    private final Set<String> activePlayers = ConcurrentHashMap.newKeySet();

    static {
        AnsiFormatter.enableColorLogging(logger);
    }
    public BombGameServerImpl(ServerView view) throws RemoteException {
        this.serverView = view;
        playerList = JSONStorageController.loadPlayersFromJSON();

    }

    @Override
    public Response removePlayer(PlayerModel removingPlayer) {
        lock.lock();
        try {
            logger.info("Server received request for updating player list.");
            for (PlayerModel player : playerList) {
                if (player.equals(removingPlayer)){
                    playerList.remove(player);
                    break;
                }
            }
            JSONStorageController.savePlayerListToJSON(playerList);
            return new Response(true, "Player removed successfully.", null);
        } catch (Exception e) {
            logger.severe("Error removing player: " + e.getMessage());
            return new Response(false, "Error removing player: " + e.getMessage(), null);
        } finally {
            lock.unlock();
        }
    }
    @Override
    public Response getQuestionsPerCategory(String category) throws RemoteException {
        lock.lock();
        try {
            logger.info("Server received question request for category: " + category);

            QuestionController questionController = new QuestionController();
            List<QuestionModel> questions = questionController.getQuestionsByCategory(category);

            if (questions.isEmpty()) {
                logger.warning("No questions found for category: " + category);
                return new Response(false, "No questions found for category: " + category, null);
            }

            logger.info("Question retrieved successfully for category: " + category);
            return new Response(true, "Question retrieved successfully.", questions);
        } catch (Exception e) {
            logger.severe("Error retrieving questions: " + e.getMessage());
            return new Response(false, "Error retrieving questions: " + e.getMessage(), null);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Response getLeaderboards(String leaderboardType) throws RemoteException {
        lock.lock();
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
        } finally {
            lock.unlock();
        }
    }

    // Method for populating and arranging list for leaderboards
    private List<LeaderboardEntryModel> getLeaderboardEntries(String leaderboardType) {
        List<LeaderboardEntryModel> leaderboard = new ArrayList<>();
        if (Objects.equals(leaderboardType, "classic")) {
            for (PlayerModel player : playerList) {
                if (player.getHasPlayedClassic()) {
                    LeaderboardEntryModel leaderboardEntry = new LeaderboardEntryModel(player.getUsername(), player.getClassicScore());
                    leaderboard.add(leaderboardEntry);
                }
            }
        } else {
            for (PlayerModel player : playerList) {
                if (player.getHasPlayedEndless()) {
                    LeaderboardEntryModel leaderboardEntry = new LeaderboardEntryModel(player.getUsername(), player.getEndlessScore());
                    leaderboard.add(leaderboardEntry);
                }
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
        lock.lock();
        try {
            logger.info("Server received request for question list");

            QuestionController questionController = new QuestionController();
            List<QuestionModel> allQuestions = questionController.getQuestionsList();

            if (allQuestions.isEmpty()) {
                logger.warning("No question list found.");
                return new Response(false, "No question list found", null);
            }

            logger.info("Question list retrieved successfully");
            return new Response(true, "Question list retrieved successfully.", allQuestions);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Response updatePlayerScore(PlayerModel player) throws RemoteException {
        lock.lock();
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
        } finally {
            lock.unlock();
        }
    }

    public Response updateQuestion(QuestionModel question) throws RemoteException {
        lock.lock();
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
        } finally {
            lock.unlock();
        }
    }

    public Response removeFromLeaderboard(LeaderboardEntryModel leaderboardEntry, String leaderboardType) throws RemoteException {
        lock.lock();
        try {
            logger.info("Server received request for removal of entry in leaderboard");
            if (leaderboardEntry == null) {
                logger.severe("Received null leaderboard entry data.");
                return new Response(false, "Received null leaderboard entry data.", null);
            }

            for (PlayerModel player : playerList) {
                if (leaderboardEntry.getPlayerName().equals(player.getUsername())) {
                    logger.info("Removed entry from player list: " + player.getUsername());
                    if (Objects.equals(leaderboardType, "classic")) {
                        player.setHasPlayedClassic(false);
                        player.setClassicScore(0);
                    } else {
                        player.setHasPlayedEndless(false);
                        player.setEndlessScore(0);
                    }
                    break;
                }
            }

            JSONStorageController.savePlayerListToJSON(playerList);
            logger.info("Player list updated successfully.");
            return new Response(true, "Player list updated successfully.", null);
        } catch (Exception e) {
            logger.severe("Error removing player from player list " + e.getMessage());
            return new Response(false, "Error removing entry from leaderboard: " + e.getMessage(), null);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Response getPlayerList() {
        lock.lock();
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
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Response updatePlayerList(List<PlayerModel> updatedList) {
        lock.lock();
        try {
            if (updatedList == null) {
                logger.severe("Received null player list.");
                return new Response(false, "Received null player list.", null);
            }
            JSONStorageController.savePlayerListToJSON(updatedList);
            logger.info("Updating player list data.");
            return new Response(true, "Player list updated successfully.", playerList);
        } catch (Exception e) {
            logger.severe("Error retrieving player list: " + e.getMessage());
            return new Response(false, "Error retrieving player list: " + e.getMessage(), null);
        } finally {
            lock.unlock();
        }
    }
    @Override
    public void login(Callback callback) throws RemoteException {
        lock.lock();
        try {
            PlayerModel player = callback.getPlayer();

            if (playerCallbacks.containsValue(callback)) {
                logMessage("Player already logged in.");
            } else {
                playerCallbacks.put(player.getUsername(), callback);
                System.out.println("Log In: " + player.getUsername());
                logMessage("Online : [");
                Set<String> usernames = playerCallbacks.keySet();
                int counter = 1;
                for (String playerUsername : usernames) {
                    logMessage(playerUsername + (counter++ == usernames.size() ? "" : ", "));
                    playerCallbacks.get(playerUsername).loginCall(player);
                }
                logMessage("]");
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public PlayerModel getPlayer(String username, String password) {
        lock.lock();
        try {
        for (PlayerModel player : playerList) {
            if (player.getUsername().equalsIgnoreCase(username) && player.getPassword().equalsIgnoreCase(password)) {
                return player;
            }
        }
        return null;
        } finally {
            lock.unlock();
        }
    }
    @Override
    public void register(String username, String password) throws RemoteException {
        lock.lock();
        try {
            PlayerModel player = new PlayerModel(username, password, "PLAYER", 0, 0, false, false);
            playerList.add(player);
            JSONStorageController.savePlayerListToJSON(playerList);
            logger.info("Registering player to database.");
        } catch (Exception e) {
            logger.severe("Error registering player: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void logMessage(String message) throws RemoteException {
        lock.lock();
        try {
            LogManager.getInstance().appendLog(message);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void logoutPlayer(String username) throws RemoteException {
        if (activePlayers.contains(username)) {
            activePlayers.remove(username);
            logMessage("Player '" + username + "' has logged out.");
        } else {
            logMessage("Warning: Player '" + username + "' tried to log out but was not logged in.");
        }
    }
}

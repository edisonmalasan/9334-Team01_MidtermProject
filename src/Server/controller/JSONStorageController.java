package Server.controller;

import Client.common.connection.ClientConnection;
import common.Log.LogManager;
import common.model.QuestionModel;
import utility.LeaderboardEntryModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import common.model.PlayerModel;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Manipulates JSON files
 */
public class JSONStorageController {
    private static final LogManager logManager = LogManager.getInstance();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static String playerFileName = "data/players.json";
    private static String questionFileName = "data/questions.json";

    /**
     * Returns a list containing leaderboard entries
     */
    public static List<LeaderboardEntryModel> loadLeaderboardFromJSON(String filename) {
        List<LeaderboardEntryModel> leaderboard = new ArrayList<>();
        try (Reader reader = new FileReader(filename)) {
            Type leaderboardListType = new TypeToken<List<LeaderboardEntryModel>>() {}.getType();
            leaderboard = gson.fromJson(reader, leaderboardListType);
            ClientConnection.bombGameServer.logMessage("Leaderboard loaded successfully from " + filename);
        } catch (FileNotFoundException e) {
            logManager.appendLog("Leaderboard file not found, returning empty list.");
        } catch (IOException e) {
            logManager.appendLog("Error loading leaderboard from JSON: " + e.getMessage());
        }
        return leaderboard;
    }

    /**
     * Saves the list of leaderboard entries to the JSON file
     */
    public static void saveLeaderboardToJSON(List<LeaderboardEntryModel> leaderboardList) {
        try (Writer writer = new FileWriter(playerFileName)) {
            gson.toJson(leaderboardList, writer);
            ClientConnection.bombGameServer.logMessage("Leaderboard successfully saved to " + playerFileName);
        } catch (IOException e) {
            logManager.appendLog("Error saving leaderboard to JSON: " + e.getMessage());
        }
    }

    public static List<PlayerModel> loadPlayersFromJSON() {
        List<PlayerModel> playerList = new ArrayList<>();
        try (Reader reader = new FileReader(playerFileName)) {
            Type playerListType = new TypeToken<List<PlayerModel>>(){}.getType();
            playerList = gson.fromJson(reader, playerListType);
            ClientConnection.bombGameServer.logMessage("Players list loaded successfully from " + playerFileName);
        } catch (FileNotFoundException e) {
            logManager.appendLog("Players file not found, returning empty list.");
        } catch (IOException e) {
            logManager.appendLog("Error loading leaderboard from JSON: " + e.getMessage());
        }
        return playerList;
    }

    public static void savePlayerListToJSON(List<PlayerModel> playerList) {
        try (Writer writer = new FileWriter(playerFileName)) {
            gson.toJson(playerList, writer);
            ClientConnection.bombGameServer.logMessage("Player successfully saved to " + playerFileName);
        } catch (IOException e) {
            logManager.appendLog("Error saving player to JSON: " + e.getMessage());
        }
    }

    public static void updatePlayerDetails(String fileName, String username, String password) {
        List<PlayerModel> playerList = new ArrayList<>();
        String originalPassword = "";
        try (Reader reader = new FileReader(fileName);
             Writer writer = new FileWriter(fileName)) {
            Type playerListType = new TypeToken<List<PlayerModel>>(){}.getType();
            playerList = gson.fromJson(reader, playerListType);
            reader.close();

            for (PlayerModel player : playerList) {
                if (player.getUsername().equals(username)) {
                    originalPassword = player.getPassword();
                    player.setPassword(password);
                }
            }
            ClientConnection.bombGameServer.logMessage("Player " + "["+ username +"]" + "password successfully changed ");
        } catch (FileNotFoundException e) {
            logManager.appendLog("Players file not found.");
        } catch (IOException e) {
            logManager.appendLog("Error loading file from JSON: " + e.getMessage());
        }
    }

    public static void updatePlayerScore(List<PlayerModel> playerList, PlayerModel newPlayer) {
        try (Writer writer = new FileWriter(playerFileName)) {
            for (PlayerModel player : playerList) {
                if (player.getUsername().equals(newPlayer.getUsername())) {
                    player.setClassicScore(newPlayer.getClassicScore());
                    player.setEndlessScore(newPlayer.getEndlessScore());
                    player.setHasPlayed(true);
                }
            }

            gson.toJson(playerList, writer);
            ClientConnection.bombGameServer.logMessage("Player score successfully updated for " + newPlayer.getUsername());
        } catch (FileNotFoundException e) {
            logManager.appendLog("Players file not found.");
        } catch (IOException e) {
            logManager.appendLog("Error loading file from JSON: " + e.getMessage());
        }
    }

    public static List<QuestionModel> loadQuestionsFromJSON() {
        List<QuestionModel> questions = new ArrayList<>();
        try (Reader reader = new FileReader(questionFileName)) {
            Type questionModelType = new TypeToken<List<QuestionModel>>() {}.getType();
            questions = gson.fromJson(reader, questionModelType);
            ClientConnection.bombGameServer.logMessage("Questions loaded successfully from " + questionFileName);
        } catch (FileNotFoundException e) {
            logManager.appendLog("Questions file not found, returning empty list.");
        } catch (IOException e) {
            logManager.appendLog("Error loading questions from JSON: " + e.getMessage());
        }
        return questions;
    }

    public static void saveQuestionToJSON(List<QuestionModel> questionsList){
        try (Writer writer = new FileWriter(questionFileName)) {
            gson.toJson(questionsList, writer);
            ClientConnection.bombGameServer.logMessage("Questions successfully saved to " + questionFileName);
        } catch (IOException e) {
            logManager.appendLog("Error saving Questions to JSON: " + e.getMessage());
        }
    }
}

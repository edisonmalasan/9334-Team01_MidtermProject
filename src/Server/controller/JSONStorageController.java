package Server.controller;
/**
 * Manages JSON files
 */

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
import java.util.logging.Logger;

/**
 * Manipulates JSON files
 */
public class JSONStorageController {
    private static final Logger logger = Logger.getLogger(JSONStorageController.class.getName());
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
            logger.info("JSONStorageController: Leaderboard loaded successfully from " + filename);
        } catch (FileNotFoundException e) {
            logger.warning("JSONStorageController: Leaderboard file not found, returning empty list.");
        } catch (IOException e) {
            logger.severe("JSONStorageController: Error loading leaderboard from JSON: " + e.getMessage());
        }
        return leaderboard;
    }

    /**
     * Saves the list of leaderboard entries to the JSON file
     */
    public static void saveLeaderboardToJSON(List<LeaderboardEntryModel> leaderboardList) {
        try (Writer writer = new FileWriter(playerFileName)) {
            gson.toJson(leaderboardList, writer);
            logger.info("JSONStorageController: Leaderboard successfully saved to " + playerFileName);
        } catch (IOException e) {
            logger.severe("JSONStorageController: Error saving leaderboard to JSON: " + e.getMessage());
        }
    }

    public static List<PlayerModel> loadPlayersFromJSON() {
        List<PlayerModel> playerList = new ArrayList<>();
        try (Reader reader = new FileReader(playerFileName)) {
            Type playerListType = new TypeToken<List<PlayerModel>>(){}.getType();
            playerList = gson.fromJson(reader, playerListType);
            logger.info("JSONStorageController: Players list loaded successfully from " + playerFileName);
        } catch (FileNotFoundException e) {
            logger.warning("JSONStorageController: Players file not found, returning empty list.");
        } catch (IOException e) {
            logger.severe("JSONStorageController: Error loading leaderboard from JSON: " + e.getMessage());
        }
        return playerList;
    }
    public static void savePlayerToJSON(PlayerModel player) {
        List<PlayerModel> playerList = loadPlayersFromJSON();
        try (Writer writer = new FileWriter(playerFileName)) {
            playerList.add(player);
            gson.toJson(playerList, writer);
            logger.info("JSONStorageController: Player successfully saved to " + playerFileName);
        } catch (IOException e) {
            logger.severe("JSONStorageController: Error saving player to JSON: " + e.getMessage());
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
            logger.info("JSONStorageController: Player password successfully changed from " + originalPassword + " to " + password);
        } catch (FileNotFoundException e) {
            logger.warning("JSONStorageController: Players file not found.");
        } catch (IOException e) {
            logger.severe("JSONStorageController: Error loading file from JSON: " + e.getMessage());
        }
    }
    public static void updatePlayerScore(PlayerModel newPlayer) {
        List<PlayerModel> playerList = loadPlayersFromJSON();
        try (Writer writer = new FileWriter(playerFileName)) {

            for (PlayerModel player : playerList) {
                if (player.getUsername().equals(newPlayer.getUsername())) {
                    player.setClassicScore(newPlayer.getClassicScore());
                    player.setEndlessScore(newPlayer.getEndlessScore());
                }
            }

            gson.toJson(playerList, writer);
            logger.info("JSONStorageController: Player score successfully updated for " + newPlayer.getUsername());
        } catch (FileNotFoundException e) {
            logger.warning("JSONStorageController: Players file not found.");
        } catch (IOException e) {
            logger.severe("JSONStorageController: Error loading file from JSON: " + e.getMessage());
        }
    }

    public static List<QuestionModel> loadQuestionsFromJSON() {
        List<QuestionModel> questions = new ArrayList<>();
        try (Reader reader = new FileReader(questionFileName)) {
            Type questionModelType = new TypeToken<List<QuestionModel>>() {}.getType();
            questions = gson.fromJson(reader, questionModelType);

            logger.info("JSONStorageController: Questions loaded successfully from " + questionFileName);
        } catch (FileNotFoundException e) {
            logger.warning("JSONStorageController: Questions file not found, returning empty list.");
        } catch (IOException e) {
            logger.severe("JSONStorageController: Error loading questions from JSON: " + e.getMessage());
        }
        return questions;
    }

    public static void saveQuestionToJSON(List<QuestionModel> questionsList){
        try (Writer writer = new FileWriter(questionFileName)) {
            gson.toJson(questionsList, writer);
            logger.info("JSONStorageModel: Questions successfully saved to " + questionFileName);
        } catch (IOException e) {
            logger.severe("JSONStorageModel: Error saving Questions to JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

}


package Server.controller;
/**
 * Manages JSON files
 */

import Server.model.LeaderboardEntryModelServer;
import common.model.QuestionModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

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

    /**
     * Returns a list containing leaderboard entries
     */
    public static List<LeaderboardEntryModelServer> loadLeaderboardFromJSON(String filename) {
        List<LeaderboardEntryModelServer> leaderboard = new ArrayList<>();
        try (Reader reader = new FileReader(filename)) {
            Type leaderboardListType = new TypeToken<List<LeaderboardEntryModelServer>>() {}.getType();
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
    public static void saveLeaderboardToJSON(String filename, List<LeaderboardEntryModelServer> leaderboard) {
        try (Writer writer = new FileWriter(filename)) {
            gson.toJson(leaderboard, writer);
            logger.info("JSONStorageController: Leaderboard successfully saved to " + filename);
        } catch (IOException e) {
            logger.severe("JSONStorageController: Error saving leaderboard to JSON: " + e.getMessage());
        }
    }
}


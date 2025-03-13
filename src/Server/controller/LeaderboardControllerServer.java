package Server.controller;
/**
 * Sends leaderboard files
 */

import Server.model.LeaderboardEntryModelServer;

import java.util.List;

public class LeaderboardControllerServer {
    private static final String CLASSIC_LEADERBOARD_FILE = "data/classic_leaderboard.xml";
    private static final String ENDLESS_LEADERBOARD_FILE = "data/endless_leaderboard.xml";

    /**
     * Returns a list containing the contents of the classic leaderboard
     */
    public static List<LeaderboardEntryModelServer> getClassicLeaderboard() {
        return XMLStorageController.loadLeaderboardFromXML(CLASSIC_LEADERBOARD_FILE);
    }
    /**
     * Returns a list containing the contents of the classic leaderboard
     */
    public static List<LeaderboardEntryModelServer> getEndlessLeaderboard() {
        return XMLStorageController.loadLeaderboardFromXML(ENDLESS_LEADERBOARD_FILE);
    }
}
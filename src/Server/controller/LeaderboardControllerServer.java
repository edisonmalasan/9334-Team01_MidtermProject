package Server.controller;
/**
 * Sends leaderboard files (JSON)
 */

import Server.model.LeaderboardEntryModelServer;

import java.util.List;

public class LeaderboardControllerServer {
    private static final String CLASSIC_LEADERBOARD_FILE = "data/classic_leaderboard.json";
    private static final String ENDLESS_LEADERBOARD_FILE = "data/endless_leaderboard.json";

    /**
     * Returns a list containing the contents of the classic leaderboard
     */
    public static List<LeaderboardEntryModelServer> getClassicLeaderboard() {
        return JSONStorageController.loadLeaderboardFromJSON(CLASSIC_LEADERBOARD_FILE);
    }
    /**
     * Returns a list containing the contents of the endless leaderboard
     */
    public static List<LeaderboardEntryModelServer> getEndlessLeaderboard() {
        return JSONStorageController.loadLeaderboardFromJSON(ENDLESS_LEADERBOARD_FILE);
    }
}

/**
 * The code commented out below are previous code for .xml processing
 */

//package Server.controller;
//
//import Server.model.LeaderboardEntryModelServer;
//
//import java.util.List;
//
//public class LeaderboardControllerServer {
//    private static final String CLASSIC_LEADERBOARD_FILE = "data/classic_leaderboard.xml";
//    private static final String ENDLESS_LEADERBOARD_FILE = "data/endless_leaderboard.xml";
//
//    /**
//     * Returns a list containing the contents of the classic leaderboard
//     */
//    public static List<LeaderboardEntryModelServer> getClassicLeaderboard() {
//        return XMLStorageController.loadLeaderboardFromXML(CLASSIC_LEADERBOARD_FILE);
//    }
//    /**
//     * Returns a list containing the contents of the classic leaderboard
//     */
//    public static List<LeaderboardEntryModelServer> getEndlessLeaderboard() {
//        return XMLStorageController.loadLeaderboardFromXML(ENDLESS_LEADERBOARD_FILE);
//    }
//}
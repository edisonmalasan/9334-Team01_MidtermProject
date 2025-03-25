package Client.Player.utils;

public class ClientSession {
    private static String playerUsername;

    // Set the player session when logged in
    public static void setPlayerUsername(String username) {
        playerUsername = username;
    }

    // Get the current logged-in player's username
    public static String getPlayerUsername() {
        return playerUsername;
    }

    // Clear session when logging out
    public static void clear() {
        playerUsername = null;
    }
}
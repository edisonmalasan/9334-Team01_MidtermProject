package utility;

import common.Response;
import common.model.PlayerModel;
import common.model.QuestionModel;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface BombGameServer extends Remote {

    // Remove player from data
    public Response removePlayer(PlayerModel player) throws RemoteException;

    // Load questions for given category
    public Response getQuestionsPerCategory(String category) throws RemoteException;

    // Get data for leaderboards (classic and endless)
    public Response getLeaderboards(String leaderboardType) throws RemoteException;

    // Get the list of all questions
    public Response getQuestionsList() throws RemoteException;

    // Update score of existing player
    public Response updatePlayerScore(PlayerModel player) throws RemoteException;

    // Removes question from database if found, else adds question to database if not found
    public Response updateQuestion(QuestionModel question) throws RemoteException;

    // Update leaderboards data
    public Response removeFromLeaderboard(LeaderboardEntryModel leaderboardEntry, String leaderboardType) throws RemoteException;

    // Update player list
    public Response updatePlayerList(List<PlayerModel> playerList) throws RemoteException;
    // Get player list
    public Response getPlayerList() throws RemoteException;

    // Login account (player and admin)
    public void login(Callback callback) throws RemoteException;

    // Returns a specific player
    public PlayerModel getPlayer(String username, String password) throws RemoteException;

    //Register an account
    public void register(String username, String password) throws RemoteException;

    // logMessage
    void logMessage(String message) throws RemoteException;

    void logoutPlayer(String username) throws RemoteException;
}

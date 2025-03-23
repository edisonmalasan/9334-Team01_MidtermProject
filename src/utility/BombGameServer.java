package utility;

import common.Response;
import common.model.QuestionModel;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BombGameServer extends Remote {

    // Load questions for given category
    public Response getQuestionsPerCategory(String category) throws RemoteException;

    // Get data for leaderboards (classic and endless)
    public Response getLeaderboards(String leaderboardType) throws RemoteException;

    // Get the list of all questions
    public Response getQuestionsList() throws RemoteException;

    // Update score of existing player
    public Response updatePlayerScore(PlayerModel player) throws RemoteException;

    // Add question to database
    public void addQuestion(QuestionModel question) throws RemoteException;

    // Update leaderboards data
    public Response removeFromLeaderboard(LeaderboardEntryModel leaderboardEntry, String leaderboardType) throws RemoteException;

    // Login account (player and admin)
    public void login(Callback callback) throws RemoteException;

    //Register an account
    public void register() throws RemoteException;
}

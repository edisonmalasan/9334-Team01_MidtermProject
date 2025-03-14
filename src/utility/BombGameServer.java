package utility;

import Client.model.PlayerModel;
import Server.model.LeaderboardEntryModelServer;
import common.model.QuestionModel;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface BombGameServer extends Remote {

    // Load questions for given category
    public void getQuestionsPerCategory(String category) throws RemoteException;

    // Get data for leaderboards (classic and endless)
    public void getLeaderboards(List<?> list, String fileName) throws RemoteException;

    // Get the list of all questions
    public void getQuestionsList() throws RemoteException;

    // Update score of existing player
    public void updatePlayerScore(PlayerModel player) throws RemoteException;

    // Add question to database
    public void addQuestion(QuestionModel question) throws RemoteException;

    // Update leaderboards data
    public void updateLeaderboard(LeaderboardEntryModelServer leaderboardEntry) throws RemoteException;

    // Login account (player and admin)
    public void login() throws RemoteException;
}

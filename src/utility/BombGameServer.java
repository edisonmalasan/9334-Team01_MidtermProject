package utility;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BombGameServer extends Remote {

    // Load questions for given category
    public void getQuestionsPerCategory() throws RemoteException;

    // Get data for leaderboards (classic and endless)
    public void getLeaderboards() throws RemoteException;

    // Get the list of all questions
    public void getQuestionsList() throws RemoteException;

    // Update score of existing player
    public void updatePlayerScore() throws RemoteException;

    // Login account (player and admin)
    public void login() throws RemoteException;
}

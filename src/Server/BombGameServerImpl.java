package Server;

import utility.BombGameServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class BombGameServerImpl extends UnicastRemoteObject implements BombGameServer {
    public BombGameServerImpl() throws RemoteException {
    }

    @Override
    public void getQuestionsPerCategory() throws RemoteException {

    }

    @Override
    public void getLeaderboards() throws RemoteException {

    }

    @Override
    public void getQuestionsList() throws RemoteException {

    }

    @Override
    public void updatePlayerScore() throws RemoteException {

    }

    @Override
    public void login() throws RemoteException {

    }
}

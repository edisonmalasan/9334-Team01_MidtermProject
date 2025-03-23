package Client;

import Client.User.utils.SoundUtility;
import utility.Callback;
import utility.PlayerModel;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CallbackImpl extends UnicastRemoteObject implements Callback, Serializable {
    private PlayerModel player;
    public CallbackImpl(PlayerModel player) throws RemoteException {
        this.player = player;
    }
    @Override
    public PlayerModel getPlayer() throws RemoteException {
        return this.player;
    }

    @Override
    public void loginCall(PlayerModel player) throws RemoteException {
        System.out.println(player.getUsername() + " logged in...");
    }

    @Override
    public void logoutCall(PlayerModel player) throws RemoteException {
        System.out.println(player.getUsername() + " logged out...");
    }
}

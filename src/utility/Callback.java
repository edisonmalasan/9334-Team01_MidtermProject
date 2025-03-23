package utility;
import java.rmi.Remote;
import java.rmi.RemoteException;
public interface Callback extends Remote {
    public PlayerModel getPlayer() throws RemoteException;
    public void loginCall(PlayerModel player) throws RemoteException;
    public void logoutCall(PlayerModel player) throws RemoteException;
}
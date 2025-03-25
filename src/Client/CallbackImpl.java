package Client;

import Client.Player.controller.LoginController;
import common.Log.AnsiFormatter;
import common.Log.LogManager;
import utility.Callback;
import common.model.PlayerModel;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

public class CallbackImpl extends UnicastRemoteObject implements Callback, Serializable {
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

    private final LogManager logManager = LogManager.getInstance();
    static {
        AnsiFormatter.enableColorLogging(logger);
    }
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
       logger.info(player.getUsername() + " logged in...");
    }

    @Override
    public void logoutCall(PlayerModel player) throws RemoteException {
        logger.info(player.getUsername() + " logged out...");
    }
}

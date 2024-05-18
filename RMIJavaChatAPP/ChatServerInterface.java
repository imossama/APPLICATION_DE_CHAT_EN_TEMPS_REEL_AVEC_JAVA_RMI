import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChatServerInterface extends Remote {
    String registerClient(ChatClientInterface client, String username) throws RemoteException;
    void broadcastMessage(String message, String sender) throws RemoteException;
    List<String> getConnectedUsers() throws RemoteException; // Add this method
    void changeUsername(String oldUsername, String newUsername) throws RemoteException;
}

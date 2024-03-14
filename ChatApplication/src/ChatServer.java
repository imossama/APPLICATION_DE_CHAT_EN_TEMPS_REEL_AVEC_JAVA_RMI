import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.*;
import java.util.HashMap;
import java.util.Map;

// Define the interface for the chat server
interface ChatServerInterface extends Remote {
    void registerClient(ChatClientInterface client, String username) throws RemoteException;
    void broadcastMessage(String message, String sender) throws RemoteException;
}

// Implement the chat server
public class ChatServer extends UnicastRemoteObject implements ChatServerInterface {
    private Map<String, ChatClientInterface> clients;

    public ChatServer() throws RemoteException {
        clients = new HashMap<>();
    }

    public void registerClient(ChatClientInterface client, String username) throws RemoteException {
        clients.put(username, client);
        broadcastMessage(username + " has joined the chat.", "Server");
    }

    public void broadcastMessage(String message, String sender) throws RemoteException {
        for (ChatClientInterface client : clients.values()) {
            client.receiveMessage(message, sender);
        }
    }

    public static void main(String[] args) {
        try {
            // Create and export the RMI registry
            LocateRegistry.createRegistry(1099);

            // Create the server object
            ChatServer server = new ChatServer();

            // Bind the server object to the registry
            Naming.rebind("ChatServer", server);

            System.out.println("Chat Server is running...");
        } catch (Exception e) {
            System.err.println("Chat Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

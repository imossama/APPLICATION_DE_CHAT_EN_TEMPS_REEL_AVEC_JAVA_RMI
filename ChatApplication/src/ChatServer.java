// ChatServer.java
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.*;
import java.util.HashMap;
import java.util.Map;

// Define the interface for the chat server
interface ChatServerInterface extends Remote {
    String registerClient(ChatClientInterface client, String username) throws RemoteException; // Updated method signature
    void broadcastMessage(String message, String sender) throws RemoteException;
}

// Implement the chat server
public class ChatServer extends UnicastRemoteObject implements ChatServerInterface {
    private Map<String, ChatClientInterface> clients;
    private Map<String, Integer> usernameCounts; // Keep track of username counts

    public ChatServer() throws RemoteException {
        clients = new HashMap<>();
        usernameCounts = new HashMap<>();
    }

    public String registerClient(ChatClientInterface client, String username) throws RemoteException {
        // Ensure the username is unique
        String uniqueUsername = makeUniqueUsername(username);

        clients.put(uniqueUsername, client);
        usernameCounts.put(username, usernameCounts.getOrDefault(username, 0) + 1);

        broadcastMessage(uniqueUsername + " has joined the chat.", "Server");

        return uniqueUsername; // Return the unique username to the client
    }

    private String makeUniqueUsername(String username) {
        int count = usernameCounts.getOrDefault(username, 0);
        String uniqueUsername = username;
        if (count > 0) {
            uniqueUsername = username + "(" + count + ")";
        }
        return uniqueUsername;
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

            System.out.println("Chat server is running...");
        } catch (Exception e) {
            System.err.println("Chat server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.rmi.Naming; // Import the Naming class
import java.rmi.registry.LocateRegistry; // Import the LocateRegistry class

public class ChatServer extends UnicastRemoteObject implements ChatServerInterface {
    private Map<String, ChatClientInterface> clients;
    private Map<String, Integer> usernameCounts;

    public ChatServer() throws RemoteException {
        clients = new HashMap<>();
        usernameCounts = new HashMap<>();
    }

    public String registerClient(ChatClientInterface client, String username) throws RemoteException {
        String uniqueUsername = makeUniqueUsername(username);

        clients.put(uniqueUsername, client);
        usernameCounts.put(username, usernameCounts.getOrDefault(username, 0) + 1);

        broadcastMessage(uniqueUsername + " has joined the chat.", "Server");

        return uniqueUsername;
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

    public List<String> getConnectedUsers() throws RemoteException {
        return new ArrayList<>(clients.keySet());
    }

    public void changeUsername(String oldUsername, String newUsername) throws RemoteException {
        if (!clients.containsKey(oldUsername)) {
            throw new RemoteException("User not found: " + oldUsername);
        }
        
        ChatClientInterface client = clients.remove(oldUsername);
        clients.put(newUsername, client);
        
        int count = usernameCounts.getOrDefault(newUsername, 0);
        usernameCounts.put(newUsername, count + 1);
        
        broadcastMessage(oldUsername + " has changed their username to " + newUsername, "Server");
    }

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);

            ChatServer server = new ChatServer();

            // Use Naming.rebind() to register the server object
            Naming.rebind("ChatServer", server);

            System.out.println("Chat server is running...");
        } catch (Exception e) {
            System.err.println("Chat server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

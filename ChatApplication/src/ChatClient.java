import java.rmi.*;
import java.util.Scanner;

// Define the interface for the chat client
interface ChatClientInterface extends Remote {
    void receiveMessage(String message, String sender) throws RemoteException;
}

// Implement the chat client
public class ChatClient extends java.rmi.server.UnicastRemoteObject implements ChatClientInterface {
    private String username;
    private ChatServerInterface server;

    public ChatClient(String username, ChatServerInterface server) throws RemoteException {
        this.username = username;
        this.server = server;
        server.registerClient(this, username);
    }

    public void sendMessage(String message) throws RemoteException {
        server.broadcastMessage(message, username);
    }

    public void receiveMessage(String message, String sender) throws RemoteException {
        System.out.println(sender + ": " + message);
    }

    public static void main(String[] args) {
        try {
            String username;
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.print("Enter your username: ");
                username = scanner.nextLine();

                ChatServerInterface server = (ChatServerInterface) Naming.lookup("rmi://localhost/ChatServer");
                ChatClient client = new ChatClient(username, server);

                System.out.println("Connected to chat. Type your message or type 'quit' to exit.");
                while (true) {
                    String message = scanner.nextLine();
                    if (message.equalsIgnoreCase("quit")) {
                        break;
                    }
                    client.sendMessage(message);
                }
            }
        } catch (Exception e) {
            System.err.println("Chat Client exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

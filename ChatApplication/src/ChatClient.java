import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.*;

// Define the interface for the chat client
interface ChatClientInterface extends Remote {
    void receiveMessage(String message, String sender) throws RemoteException;
}

// Implement the chat client with Swing UI
public class ChatClient extends java.rmi.server.UnicastRemoteObject implements ChatClientInterface {
    private String username;
    private ChatServerInterface server;
    private JTextArea chatArea; // Initialize chatArea here

    public ChatClient(String username, ChatServerInterface server) throws RemoteException {
        this.username = username;
        this.server = server;
        server.registerClient(this, username);
        chatArea = new JTextArea(); // Initialize chatArea here
    }

    public void sendMessage(String message) throws RemoteException {
        server.broadcastMessage(message, username);
    }

    public void receiveMessage(String message, String sender) throws RemoteException {
        if (chatArea != null) {
            chatArea.append(sender + ": " + message + "\n");
        }
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Chat Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JScrollPane chatScrollPane = new JScrollPane(chatArea); // Use initialized chatArea
        chatArea.setEditable(false);
        frame.add(chatScrollPane, BorderLayout.CENTER);

        JTextField messageField = new JTextField();
        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String message = messageField.getText();
                    if (!message.isEmpty()) {
                        sendMessage(message);
                        messageField.setText("");
                    }
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });
        frame.add(messageField, BorderLayout.SOUTH);

        frame.setSize(400, 300);
        frame.setVisible(true);

        messageField.requestFocusInWindow();
    }

    public static void main(String[] args) {
        try {
            String username = JOptionPane.showInputDialog("Enter your username:");
            if (username == null || username.isEmpty()) {
                System.exit(0);
            }

            ChatServerInterface server = (ChatServerInterface) Naming.lookup("rmi://localhost/ChatServer");
            ChatClient client = new ChatClient(username, server);

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    client.createAndShowGUI();
                }
            });

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error connecting to the chat server: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }
    }
}

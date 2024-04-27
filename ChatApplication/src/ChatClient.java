import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

// Define the interface for the chat client
interface ChatClientInterface extends Remote {
    void receiveMessage(String message, String sender) throws RemoteException;
}

// Implement the chat client with Swing UI
public class ChatClient extends java.rmi.server.UnicastRemoteObject implements ChatClientInterface {
    private String username;
    private ChatServerInterface server;
    private JTextArea chatArea;

    public ChatClient(String username, ChatServerInterface server) throws RemoteException {
        this.username = username;
        this.server = server;
        server.registerClient(this, username);
        chatArea = new JTextArea();
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
        JFrame frame = new JFrame("Chat Application - " + username);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Panel for username and time
        JPanel userInfoPanel = new JPanel(new BorderLayout());

        // Display username
        JLabel usernameLabel = new JLabel("Username: " + username);
        userInfoPanel.add(usernameLabel, BorderLayout.WEST);

        // Display current time
        JLabel timeLabel = new JLabel(getCurrentTime());
        userInfoPanel.add(timeLabel, BorderLayout.EAST);

        frame.add(userInfoPanel, BorderLayout.NORTH);

        JScrollPane chatScrollPane = new JScrollPane(chatArea);
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

        // Rest of the code for buttons...

        frame.setSize(400, 300);
        frame.setVisible(true);

        messageField.requestFocusInWindow();
    }

    // Method to get current time in a formatted string
    private String getCurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return "Time: " + dateFormat.format(date);
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

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Timer;

interface ChatClientInterface extends Remote {
    void receiveMessage(String message, String sender) throws RemoteException;
    String getUsername() throws RemoteException;
}

public class ChatClient extends java.rmi.server.UnicastRemoteObject implements ChatClientInterface {
    private String username;
    private ChatServerInterface server;
    private JTextArea chatArea;
    private JLabel usernameLabel;
    private JLabel timeLabel;

    public ChatClient(String username, ChatServerInterface server) throws RemoteException {
        this.username = username;
        this.server = server;
        chatArea = new JTextArea();

        this.username = server.registerClient(this, username);
    }

    public void sendMessage(String message) throws RemoteException {
        server.broadcastMessage(message, username);
    }

    public void receiveMessage(String message, String sender) throws RemoteException {
        if (chatArea != null) {
            chatArea.append(sender + ": " + message + "\n");
        }
    }

    public String getUsername() throws RemoteException {
        return username;
    }
    
    public void createAndShowGUI() {
        JFrame frame = new JFrame("Chat Application - " + username);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.X_AXIS));

        // Change username button with pen emoji
        JButton changeUsernameButton = new JButton("\uD83D\uDD8B"); // Pen emoji
        changeUsernameButton.setContentAreaFilled(false);
        changeUsernameButton.setBorderPainted(false);
        changeUsernameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newUsername = JOptionPane.showInputDialog("Enter your new username:");
                if (newUsername != null && !newUsername.isEmpty()) {
                    username = newUsername;
                    usernameLabel.setText("Username: " + username);
                    frame.setTitle("Chat Application - " + username);
                }
            }
        });

        // Username label
        usernameLabel = new JLabel("Username: " + username);
        userInfoPanel.add(usernameLabel);
        userInfoPanel.add(changeUsernameButton);

        // Time label
        timeLabel = new JLabel();
        userInfoPanel.add(Box.createHorizontalGlue()); // Add a flexible space
        userInfoPanel.add(timeLabel);

        frame.add(userInfoPanel, BorderLayout.NORTH);

        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatArea.setEditable(false);
        frame.add(chatScrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel messagePanel = new JPanel(new BorderLayout());
        JTextField messageField = new JTextField();
        messagePanel.add(messageField, BorderLayout.CENTER);

        JButton sendButton = new JButton("Send");
        messagePanel.add(sendButton, BorderLayout.EAST);

        bottomPanel.add(messagePanel, BorderLayout.CENTER);

        JButton emojiButton = new JButton("Emoji");
        bottomPanel.add(emojiButton, BorderLayout.WEST);

        JButton clearButton = new JButton("Clear");
        bottomPanel.add(clearButton, BorderLayout.EAST);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setSize(400, 300);
        frame.setVisible(true);

        messageField.requestFocusInWindow();

        // Start a timer to update the time label every second
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLabel.setText(getCurrentTime());
            }
        });
        timer.start();

        // Action listener for sending messages
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageField.getText().trim();
                if (!message.isEmpty()) {
                    try {
                        sendMessage(message);
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                    messageField.setText("");
                }
            }
        });

        // Action listener for clearing chat
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chatArea.setText("");
            }
        });

        // Action listener for emoji button
        emojiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EmojiDialog emojiDialog = new EmojiDialog(frame, messageField);
                emojiDialog.setVisible(true);
            }
        });
    }

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

class EmojiDialog extends JDialog {
    private JTextField messageField;

    public EmojiDialog(Frame parent, JTextField messageField) {
        super(parent, "Select Emoji", true);
        this.messageField = messageField;
        setupUI();
    }

    private void setupUI() {
        setLayout(new GridLayout(3, 4, 5, 5));

        String[] emojis = { "😀", "😂", "😍", "😎", "😜", "😇", "🤔", "😱", "🙄", "😴", "🤣", "😊" };

        for (String emoji : emojis) {
            JButton emojiButton = new JButton(emoji);
            emojiButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    messageField.setText(messageField.getText() + emoji);
                }
            });
            add(emojiButton);
        }

        pack();
        setLocationRelativeTo(null);
    }
}

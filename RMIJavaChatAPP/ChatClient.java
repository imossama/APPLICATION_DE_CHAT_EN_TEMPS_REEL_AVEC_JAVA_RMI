import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.Timer;

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

        JButton changeUsernameButton = new JButton("🔧");
        changeUsernameButton.setToolTipText("Change Username");
        changeUsernameButton.setMargin(new Insets(5, 10, 5, 10));
        changeUsernameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newUsername = JOptionPane.showInputDialog("Enter your new username:");
                if (newUsername != null && !newUsername.isEmpty()) {
                    try {
                        server.changeUsername(username, newUsername); // Broadcast username change to server
                        username = newUsername;
                        usernameLabel.setText("Username: " + username);
                        frame.setTitle("Chat Application - " + username);
                        JOptionPane.showMessageDialog(frame, "Username changed successfully to: " + username, "Username Changed", JOptionPane.INFORMATION_MESSAGE);
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        usernameLabel = new JLabel("Username: " + username);
        userInfoPanel.add(usernameLabel);
        userInfoPanel.add(Box.createHorizontalStrut(10));
        userInfoPanel.add(changeUsernameButton);

        timeLabel = new JLabel();
        userInfoPanel.add(Box.createHorizontalGlue());
        userInfoPanel.add(timeLabel);

        frame.add(userInfoPanel, BorderLayout.NORTH);

        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatArea.setEditable(false);
        frame.add(chatScrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel messagePanel = new JPanel(new BorderLayout());
        JTextField messageField = new JTextField();
        messagePanel.add(messageField, BorderLayout.CENTER);

        JButton sendButton = new JButton("✉");
        sendButton.setMargin(new Insets(5, 10, 5, 10));
        // messagePanel.add(sendButton, BorderLayout.EAST);

        bottomPanel.add(messagePanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        buttonPanel.add(sendButton);

        JButton clearButton = new JButton("❌");
        clearButton.setMargin(new Insets(5, 10, 5, 10));
        buttonPanel.add(clearButton);

        JButton emojiButton = new JButton("😀");
        emojiButton.setToolTipText("Emoji");
        emojiButton.setMargin(new Insets(5, 10, 5, 10));
        emojiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EmojiDialog emojiDialog = new EmojiDialog(frame, messageField);
                emojiDialog.setVisible(true);
            }
        });
        buttonPanel.add(emojiButton);

        JButton usersButton = new JButton("👥");
        usersButton.setToolTipText("Users");
        usersButton.setMargin(new Insets(5, 10, 5, 10));
        usersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<String> users = server.getConnectedUsers();
                    if (users.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "There's no user connected to the chat room.", "Users", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame, String.join("\n", users), "Connected Users", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(frame, "Error fetching users: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
        buttonPanel.add(usersButton);

        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setSize(600, 400);
        frame.setVisible(true);

        messageField.requestFocusInWindow();

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLabel.setText(getCurrentTime());
            }
        });
        timer.start();

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

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chatArea.setText("");
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

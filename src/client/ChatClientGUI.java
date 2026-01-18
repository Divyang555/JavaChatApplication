package client;

// Swing UI components
import javax.swing.*;

// AWT classes for layout, color, font, events
import java.awt.*;
import java.awt.event.*;

// IO for client-server communication
import java.io.IOException;

// Date & time formatting
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ChatClientGUI
 * This class creates a modern Swing-based chat client interface
 * and connects it with the ChatClient (socket communication).
 */
public class ChatClientGUI extends JFrame {

    // Area where chat messages are displayed
    private JTextArea messageArea;

    // Text field where user types messages
    private JTextField textField;

    // Button to exit the chat
    private JButton exitButton;

    // Client object to communicate with the server
    private ChatClient client;

    /**
     * Constructor: Initializes the GUI and connects to the chat server
     */
    public ChatClientGUI() {

        // Set window title
        super("Chat Application");

        // Set window size
        setSize(400, 500);

        // Close application when window is closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        /* =========================
           UI THEME & STYLING
           ========================= */

        // WhatsApp-style light green background
        Color backgroundColor = new Color(220, 248, 198);

        // Message background color
        Color messageBgColor = Color.WHITE;

        // Green color for buttons
        Color buttonColor = new Color(37, 211, 102);

        // Dark text color
        Color textColor = new Color(33, 33, 33);

        // Fonts for chat text and buttons
        Font textFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 13);

        /* =========================
           MESSAGE DISPLAY AREA
           ========================= */

        // Create chat message area
        messageArea = new JTextArea();

        // Prevent user from editing messages
        messageArea.setEditable(false);

        // Set background and font styles
        messageArea.setBackground(new Color(245, 255, 245));
        messageArea.setForeground(textColor);
        messageArea.setFont(textFont);

        // Add padding inside message area
        messageArea.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        // Add scrolling functionality
        JScrollPane scrollPane = new JScrollPane(messageArea);

        // Place message area at the center of the window
        add(scrollPane, BorderLayout.CENTER);

        /* =========================
           USER NAME INPUT
           ========================= */

        // Prompt user to enter their name
        String name = JOptionPane.showInputDialog(
                this,
                "Enter your name:",
                "Name Entry",
                JOptionPane.PLAIN_MESSAGE
        );

        // Update window title with user name
        this.setTitle("Chat Application - " + name);

        /* =========================
           MESSAGE INPUT FIELD
           ========================= */

        // Text field for typing messages
        textField = new JTextField();

        // Apply modern font
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Add border and padding
        textField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(8, 10, 8, 10)
                )
        );

        // Set text and background colors
        textField.setForeground(textColor);
        textField.setBackground(backgroundColor);

        // Send message when Enter key is pressed
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // Create timestamped message
                String message =
                        "[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] "
                                + name + ": "
                                + textField.getText();

                // Send message to server
                client.sendMessage(message);

                // Clear input field
                textField.setText("");
            }
        });

        /* =========================
           EXIT BUTTON
           ========================= */

        // Create exit button
        exitButton = new JButton("Exit");

        // Remove focus outline
        exitButton.setFocusPainted(false);

        // Style button
        exitButton.setFont(buttonFont);
        exitButton.setBackground(new Color(220, 53, 69)); // Red color
        exitButton.setForeground(Color.WHITE);

        // Add padding
        exitButton.setBorder(
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        );

        // Action when exit button is clicked
        exitButton.addActionListener(e -> {

            // Notify server that user left
            client.sendMessage(name + " has left the chat.");

            // Small delay to ensure message is sent
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }

            // Close application
            System.exit(0);
        });

        /* =========================
           BOTTOM PANEL
           ========================= */

        // Panel to hold input field and exit button
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Apply background color
        bottomPanel.setBackground(backgroundColor);

        // Add spacing around panel
        bottomPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        // Add components to panel
        bottomPanel.add(textField, BorderLayout.CENTER);
        bottomPanel.add(exitButton, BorderLayout.EAST);

        // Add panel to bottom of the window
        add(bottomPanel, BorderLayout.SOUTH);

        /* =========================
           CLIENT INITIALIZATION
           ========================= */

        try {
            // Create client and connect to server
            this.client = new ChatClient(
                    "127.0.0.1",
                    5000,
                    this::onMessageReceived
            );

            // Start listening for incoming messages
            client.startClient();

        } catch (IOException e) {
            // Show error if connection fails
            JOptionPane.showMessageDialog(
                    this,
                    "Error connecting to the server",
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE
            );
            System.exit(1);
        }
    }

    /**
     * Handles messages received from server
     * Ensures UI updates happen on the Event Dispatch Thread
     */
    private void onMessageReceived(String message) {
        SwingUtilities.invokeLater(() ->
                messageArea.append("➤ " + message + "\n\n")
        );
    }

    /**
     * Main method – program entry point
     */
    public static void main(String[] args) {

        // Ensure GUI runs on Swing Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new ChatClientGUI().setVisible(true);
        });
    }
}

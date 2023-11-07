import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

public class LoginWindow extends JFrame{
    public LoginWindow(BufferedReader ReadFromServer, PrintWriter SendToServer, Message message, Socket clientSocket) {
        // Create the login/register window
        JFrame loginRegisterWindow = new JFrame("Gym Management System");
        loginRegisterWindow.setSize(400, 300);
        loginRegisterWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginRegisterWindow.setLayout(new GridLayout(2, 1));

        // Create the login and register buttons
        JPanel loginRegisterPanel = new JPanel(new GridLayout(1, 1));
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        loginRegisterPanel.add(loginButton);
        loginRegisterPanel.add(registerButton);

        // Create exit buttons
        JButton exitButton = new JButton("Exit");

        // Add the components to the window
        loginRegisterWindow.add(loginRegisterPanel);
        loginRegisterWindow.add(exitButton);

        // Add action listeners to the buttons
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Create the login window
                JFrame loginWindow = new JFrame("Login");
                loginWindow.setSize(300, 150);
                loginWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                loginWindow.setLayout(new GridLayout(3, 1));

                // Create the username label and text field
                JPanel usernamePanel = new JPanel(new GridLayout(1, 2));
                JLabel usernameLabel = new JLabel("Username:");
                JTextField usernameTextField = new JTextField();
                usernamePanel.add(usernameLabel);
                usernamePanel.add(usernameTextField);

                // Create the password label and password field
                JPanel passwordPanel = new JPanel(new GridLayout(1, 2));
                JLabel passwordLabel = new JLabel("Password:");
                JPasswordField passwordField = new JPasswordField();
                passwordPanel.add(passwordLabel);
                passwordPanel.add(passwordField);

                // Create the login button
                JButton loginButton = new JButton("Login");

                // Add the components to the window
                loginWindow.add(usernamePanel);
                loginWindow.add(passwordPanel);
                loginWindow.add(loginButton);

                // Add action listener to the login button
                loginButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            // Get the username and password
                            String username = usernameTextField.getText();
                            String password = new String(passwordField.getPassword());

                            // Send the username and password to the server
                            message.sendPrintMessage(SendToServer, "LOGIN:" + username + "," + password);

                        } catch (Exception ex) {
                            message.sendPrintMessage(SendToServer, ex.getMessage());
                        }
                    }
                });

                // Show the login window
                loginWindow.setVisible(true);
            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            // TODO register
            }
        });


        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Close streams and socket
                    ReadFromServer.close();
                    SendToServer.close();
                    clientSocket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                // Close the window
                loginRegisterWindow.dispose();
            }
        });

        // Show the window
        loginRegisterWindow.setVisible(true);
    }

    
}

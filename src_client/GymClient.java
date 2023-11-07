import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class GymClient {
    public static void main(String[] args) throws IOException {
        // Create client socket
        Socket clientSocket = new Socket("localhost", 5000);
        System.out.println(Color.ColorString("Connected to server", Color.ANSI_GREEN));

        // Create input and output streams
        BufferedReader ReadFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter SendToServer = new PrintWriter(clientSocket.getOutputStream(), true);

        // Create message object
        Message message = new Message();

        // Create the main window
        JFrame mainWindow = new JFrame("Gym Management System");
        mainWindow.setSize(400, 300);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setLayout(new GridLayout(2, 1));
        mainWindow.setVisible(false);

        // Create the add gym member and add gym buttons
        JPanel addGymMemberGymPanel = new JPanel(new GridLayout(1, 2));
        JButton addGymMemberButton = new JButton("Add Gym Member");
        JButton addGymButton = new JButton("Add Gym");
        addGymMemberGymPanel.add(addGymMemberButton);
        addGymMemberGymPanel.add(addGymButton);

        // Create the exit button
        JButton exitButton = new JButton("Exit");

        // Add the components to the window
        mainWindow.add(addGymMemberGymPanel);
        mainWindow.add(exitButton);

        // Add action listeners to the buttons
        addGymMemberButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Create the add gym member window
                JFrame addGymMemberWindow = new JFrame("Add Gym Member");
                addGymMemberWindow.setSize(300, 150);
                addGymMemberWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                addGymMemberWindow.setLayout(new GridLayout(3, 1));

                // Create the name label and text field
                JPanel namePanel = new JPanel(new GridLayout(1, 2));
                JLabel nameLabel = new JLabel("Name:");
                JTextField nameTextField = new JTextField();
                namePanel.add(nameLabel);
                namePanel.add(nameTextField);

                // Create the age label and text field
                JPanel agePanel = new JPanel(new GridLayout(1, 2));
                JLabel ageLabel = new JLabel("Age:");
                JTextField ageTextField = new JTextField();
                agePanel.add(ageLabel);
                agePanel.add(ageTextField);

                // Create the add button
                JButton addButton = new JButton("Add");

                // Add the components to the window
                addGymMemberWindow.add(namePanel);
                addGymMemberWindow.add(agePanel);
                addGymMemberWindow.add(addButton);

                // Add action listener to the add button
                addButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            // Get the name and age
                            String name = nameTextField.getText();
                            int age = Integer.parseInt(ageTextField.getText());

                            // TODO: Send the name and age to the server to add a new gym member

                            // Close the add gym member window
                            addGymMemberWindow.dispose();

                        } catch (Exception ex) {
                            message.sendPrintMessage(SendToServer, ex.getMessage());
                        }
                    }
                });

                // Show the add gym member window
                addGymMemberWindow.setVisible(true);
            }
        });

        addGymButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Create the add gym window
                JFrame addGymWindow = new JFrame("Add Gym");
                addGymWindow.setSize(300, 150);
                addGymWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                addGymWindow.setLayout(new GridLayout(3, 1));

                // Create the name label and text field
                JPanel namePanel = new JPanel(new GridLayout(1, 2));
                JLabel nameLabel = new JLabel("Name:");
                JTextField nameTextField = new JTextField();
                namePanel.add(nameLabel);
                namePanel.add(nameTextField);

                // Create the address label and text field
                JPanel addressPanel = new JPanel(new GridLayout(1, 2));
                JLabel addressLabel = new JLabel("Address:");
                JTextField addressTextField = new JTextField();
                addressPanel.add(addressLabel);
                addressPanel.add(addressTextField);

                // Create the add button
                JButton addButton = new JButton("Add");

                // Add the components to the window
                addGymWindow.add(namePanel);
                addGymWindow.add(addressPanel);
                addGymWindow.add(addButton);

                // Add action listener to the add button
                addButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            // Get the name and address
                            String name = nameTextField.getText();
                            String address = addressTextField.getText();

                            // TODO: Send the name and address to the server to add a new gym

                            // Close the add gym window
                            addGymWindow.dispose();

                        } catch (Exception ex) {
                            message.sendPrintMessage(SendToServer, ex.getMessage());
                        }
                    }
                });

                // Show the add gym window
                addGymWindow.setVisible(true);
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: Close the client socket and exit the program
            }
        });

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
                        mainWindow.setVisible(true);
                        loginRegisterWindow.dispose();
                        loginWindow.dispose();

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

        // TODO sockets should be closed when the user exits the program (e.g. by clicking the exit button) ONLY
        // Close streams and socket
        ReadFromServer.close();
        SendToServer.close();
        clientSocket.close();
    }
}

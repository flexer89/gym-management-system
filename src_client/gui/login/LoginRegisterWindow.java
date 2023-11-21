package gui.login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import utils.Message;

public class LoginRegisterWindow extends JFrame{
    public LoginRegisterWindow(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer, Socket clientSocket){
        // Create the window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400,300);
        this.setLayout(new GridLayout(2, 1));
        this.setVisible(true);
        this.setTitle("Gym Management System");

        // Create the login and register buttons
        JPanel loginRegisterPanel = new JPanel(new GridLayout(1, 1));
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton exitButton = new JButton("Exit");
        loginRegisterPanel.add(loginButton);
        loginRegisterPanel.add(registerButton);

        this.add(loginRegisterPanel);
        this.add(exitButton);

        SwingUtilities.updateComponentTreeUI(getContentPane());

        // Add event listener for exit button
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
                dispose();
            }
        });

        // Add event listener for login button
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new LoginWindow(message, ReadFromServer, SendToServer, LoginRegisterWindow.this);
            }
        });

        // Add event listener for register button
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new RegisterWindow(message, ReadFromServer, SendToServer);
            }
        });
    };

}


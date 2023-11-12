package gui.exit_entrance;

import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import utils.Message;

public class ExitEntranceWindow extends JFrame{
    private int gymID;
    public ExitEntranceWindow(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer, Socket clientSocket, int gymID) {
        this.gymID = gymID;
        // Create the window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400,300);
        this.setVisible(true);
        this.setTitle("Gym Management System | Entrance Manager | Gym no " + gymID);
        this.setLayout(new GridLayout(1, 2));

        // Create Exit Button
        JButton exitButton = new JButton("Exit the gym");

        // Create Enter Button
        JButton entranceButton = new JButton("Enter the gym");

        // Create Entrance panel
        JPanel entrancePanel = new JPanel(new GridLayout(5, 1));
        entrancePanel.setBorder(new EmptyBorder(10, 40, 10, 40));

        // Create Entrance Label
        JLabel entranceLabel = new JLabel("Enter your card number");
        entranceLabel.setFont(entranceLabel.getFont().deriveFont(20.0f));
        entranceLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create Entrance Text Field
        JTextField entranceTextField = new JTextField();
        entranceTextField.setFont(entranceLabel.getFont().deriveFont(30.0f));

        // Create Entrance Button
        JButton enterButton = new JButton("Enter");

        // Create Back Button
        JButton backButton = new JButton("Back");

        // Create buy ticket button
        JButton buyTicketButton = new JButton("Buy a ticket");
        
        // Add the label and the text field to the panel
        entrancePanel.add(entranceLabel);
        entrancePanel.add(entranceTextField);
        entrancePanel.add(enterButton);
        entrancePanel.add(buyTicketButton);
        entrancePanel.add(backButton);

        // Create Exit Panel
        JPanel exitPanel = new JPanel(new GridLayout(4, 1));
        exitPanel.setBorder(new EmptyBorder(10, 40, 10, 40));

        // Create Exit Label
        JLabel exitLabel = new JLabel("Enter your card number:");
        exitLabel.setFont(exitLabel.getFont().deriveFont(20.0f));
        exitLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create Exit Text Field
        JTextField exitTextField = new JTextField();
        exitTextField.setFont(exitLabel.getFont().deriveFont(30.0f));

        // Create Exit Button
        JButton exitGymButton = new JButton("Exit");

        // Create Back Button
        JButton exitBackButton = new JButton("Back");

        // Add the label and the button to the panel
        exitPanel.add(exitLabel);
        exitPanel.add(exitTextField);
        exitPanel.add(exitGymButton);
        exitPanel.add(exitBackButton);

        add(entranceButton);
        add(exitButton);

        SwingUtilities.updateComponentTreeUI(getContentPane());

        // Add event listener for entrance button
        entranceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Clear the window
                getContentPane().removeAll();
                add(entrancePanel);
                revalidate();
                SwingUtilities.updateComponentTreeUI(getContentPane());
            }
        });

        // Add event listener for buy ticket button
        buyTicketButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO Create the buy ticket window
                
            }
        });

        // Add event listener for buy ticket button
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Clear the window
                getContentPane().removeAll();
                add(exitPanel);
                revalidate();
                SwingUtilities.updateComponentTreeUI(getContentPane());
            }
        });

        // Add event listener for back button
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Clear the window
                getContentPane().removeAll();
                add(entranceButton);
                add(exitButton);
                revalidate();
                SwingUtilities.updateComponentTreeUI(getContentPane());
            }
        });

        // Add event listener for exit button
        exitBackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Clear the window
                getContentPane().removeAll();
                add(entranceButton);
                add(exitButton);
                revalidate();
                SwingUtilities.updateComponentTreeUI(getContentPane());
            }
        });

        // Add event listener for exitBackButton button
        exitGymButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the ID
                String card_number = exitTextField.getText();
                // Send the ID to the server
                message.sendCanExitGymMessage(SendToServer, card_number + "," + gymID);
                try {
                    // Get the response from the server
                    String response = ReadFromServer.readLine();
                    if (response.equals("true")) {
                        // If the response is true, the user can exit the gym
                        JOptionPane.showMessageDialog(null, "Have a nice day!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        // If the response is false, the user cannot exit the gym
                        JOptionPane.showMessageDialog(null, "You haven't even entered the gym yet!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException e1) {
                    System.out.println("Error reading from server: " + e1.getMessage());
                
                }
            }
        });

        // Add event listener for enter button
        enterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the ID
                String clientID = entranceTextField.getText();
                // Send the ID to the server
                message.sendCanEnterGymMessage(SendToServer, clientID + "," + gymID);
                try {
                    // Get the response from the server
                    String response = ReadFromServer.readLine();
                    if (response.equals("true")) {
                        // If the response is true, the user can enter the gym
                        JOptionPane.showMessageDialog(null, "Enjoy your training!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        // If the response is false, the user cannot enter the gym
                        JOptionPane.showMessageDialog(null, "You cannot enter the gym, sorry!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException e1) {
                    System.out.println("Error reading from server: " + e1.getMessage());
                }
            }
        });

    }

}
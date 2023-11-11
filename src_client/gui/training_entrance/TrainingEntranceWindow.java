package gui.training_entrance;

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

import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import utils.Message;

public class TrainingEntranceWindow extends JFrame{
    private int roomID;
    public TrainingEntranceWindow(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer, Socket clientSocket, int roomID) {
        this.roomID = roomID;
        // Create the window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400,300);
        this.setVisible(true);
        this.setTitle("Gym Management System | Training Entrance room " + roomID);

        // Create Entrance panel
        JPanel entrancePanel = new JPanel(new GridLayout(3, 1));
        entrancePanel.setBorder(new EmptyBorder(10, 40, 10, 40));
        JLabel entranceLabel = new JLabel("Enter your ID:"); 
        entranceLabel.setFont(entranceLabel.getFont().deriveFont(30.0f));
        entranceLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create the text field
        JTextField idTextField = new JTextField();
        idTextField.setFont(entranceLabel.getFont().deriveFont(30.0f));

        // Create enter button
        JButton enterButton = new JButton("Enter");
        enterButton.setMargin(new Insets(10, 10, 10, 10));

        // Add the label and the text field to the panel
        entrancePanel.add(entranceLabel);
        entrancePanel.add(idTextField);
        entrancePanel.add(enterButton);

        // Add the panels to the window
        this.add(entrancePanel);

        SwingUtilities.updateComponentTreeUI(getContentPane());

        enterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String clientID = idTextField.getText();
                message.sendCanEnterTrainingMessage(SendToServer, clientID + "," + roomID);

                try {
                    String message = ReadFromServer.readLine();
                    if (message.equals("true")) {
                        JOptionPane.showMessageDialog(null, "Enjoy your training!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "You can't enter the training room yet or you don't have a reservation for this room", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    System.out.println(message);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });

    }

}
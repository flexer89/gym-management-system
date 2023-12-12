package gui.dashboard.trainer_dashboard.training_management;

import utils.CustomLogger;
import utils.Message;
import utils.ValidateData;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddTrainingWindow extends JFrame {
    private JTextField nameField;
    private JTextField dateField;
    private JTextField startHourField;
    private JTextField endHourField;
    private JTextField capacityField;
    private JTextField roomField;
    private JTextField gymIDField;
    private JButton addButton;
    private JButton cancelButton;

    public AddTrainingWindow(Message message, BufferedReader readFromServer, PrintWriter sendToServer, int trainerID) {
        String trainerIDString = Integer.toString(trainerID);
        // Create the main window
        this.setSize(400, 300);
        this.setVisible(true);
        this.setTitle("Add Training");

        // Create the panel for the form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        // Add the name field
        c.gridx = 0;
        c.gridy = 0;
        formPanel.add(new JLabel("Name: "), c);

        c.gridx = 1;
        c.gridy = 0;
        nameField = new JTextField(20);
        formPanel.add(nameField, c);

        // Add the date field
        c.gridx = 0;
        c.gridy = 1;
        formPanel.add(new JLabel("Date: "), c);

        c.gridx = 1;
        c.gridy = 1;
        dateField = new JTextField(20);
        formPanel.add(dateField, c);

        // Add the start field
        c.gridx = 0;
        c.gridy = 2;
        formPanel.add(new JLabel("Start Hour:"), c);

        c.gridx = 1;
        c.gridy = 2;
        startHourField = new JTextField(20);
        formPanel.add(startHourField, c);

        // Add the end hour field
        c.gridx = 0;
        c.gridy = 3;
        formPanel.add(new JLabel("End Hour:"), c);

        c.gridx = 1;
        c.gridy = 3;
        endHourField = new JTextField(20);
        formPanel.add(endHourField, c);

        // Add the capacity field
        c.gridx = 0;
        c.gridy = 4;
        formPanel.add(new JLabel("Capacity: "), c);

        c.gridx = 1;
        c.gridy = 4;
        capacityField = new JTextField(20);
        formPanel.add(capacityField, c);

        // Add the room field
        c.gridx = 0;
        c.gridy = 5;
        formPanel.add(new JLabel("Room: "), c);

        c.gridx = 1;
        c.gridy = 5;
        roomField = new JTextField(20);
        formPanel.add(roomField, c);

        // Add the phone field
        c.gridx = 0;
        c.gridy = 6;
        formPanel.add(new JLabel("Gym ID: "), c);

        c.gridx = 1;
        c.gridy = 6;
        gymIDField = new JTextField(20);
        formPanel.add(gymIDField, c);

        // Create the panel for the buttons
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        // Add the panels to the main window
        this.add(formPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners to the buttons
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the data from the fields
                String name = nameField.getText();
                String date = dateField.getText();
                String startHour = startHourField.getText();
                String endHour = endHourField.getText();
                String capacity = capacityField.getText();
                String room = roomField.getText();
                String gymID = gymIDField.getText();
                
                // Validate the input
                if (name.isEmpty() || date.isEmpty() || startHour.isEmpty() || endHour.isEmpty() || capacity.isEmpty() || room.isEmpty() || gymID.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!ValidateData.ValidateCapacity(capacity) || !ValidateData.ValidateHourRange(startHour, endHour) || !ValidateData.ValidateDate(date) || !ValidateData.ValidateRoom(room) || !ValidateData.ValidateGymID(gymID)) {
                    return;
                }

                // Send the data to the server
                message.sendAddTrainingMessage(sendToServer, name + "," + date + "," + startHour + ","  + endHour + "," + capacity + "," + room + "," + trainerIDString + "," + gymID);

                // Read the response from the server
                try {
                    String response = readFromServer.readLine();
                    if (response.equals("True")) {
                        JOptionPane.showMessageDialog(null, "Training added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error adding Training!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException e1) {
                    CustomLogger.logError("Error reading from server: " + e1.getMessage());
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

}
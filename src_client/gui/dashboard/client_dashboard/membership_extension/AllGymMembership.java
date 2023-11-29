package gui.dashboard.client_dashboard.membership_extension;

import utils.Message;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AllGymMembership extends JFrame {
    private JTextField startDateField;
    private JTextField monthsField;
    private JButton addButton;
    private JButton cancelButton;

    public AllGymMembership(Message message, BufferedReader readFromServer, PrintWriter sendToServer, int userId) {
        // Create the main window
        this.setSize(400, 300);
        this.setVisible(true);
        this.setTitle("Add Gym");

        // Create the panel for the form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        // Add the Start field
        c.gridx = 0;
        c.gridy = 0;
        formPanel.add(new JLabel("Start Date:"), c);

        c.gridx = 1;
        c.gridy = 0;
        startDateField = new JTextField(20);
        formPanel.add(startDateField, c);

        // Add the Months field
        c.gridx = 0;
        c.gridy = 1;
        formPanel.add(new JLabel("Months:"), c);

        c.gridx = 1;
        c.gridy = 1;
        monthsField = new JTextField(20);
        formPanel.add(monthsField, c);

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

            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

}
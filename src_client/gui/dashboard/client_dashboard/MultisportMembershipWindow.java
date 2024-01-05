package gui.dashboard.client_dashboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

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

import utils.CustomLogger;
import utils.Message;

public class MultisportMembershipWindow extends JFrame{
    public MultisportMembershipWindow(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer, int userID) {
        // Create the window
        this.setSize(400,300);
        this.setVisible(true);
        this.setTitle("Gym Management System | Multisport Membership");

        // Create membership panel
        JPanel multisportPanel = new JPanel(new GridLayout(3, 1));
        multisportPanel.setBorder(new EmptyBorder(10, 40, 10, 40));
        JLabel multisportLabel = new JLabel("Enter Card Number:"); 
        multisportLabel.setFont(multisportLabel.getFont().deriveFont(30.0f));
        multisportLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create the text field
        JTextField idTextField = new JTextField();
        idTextField.setFont(multisportLabel.getFont().deriveFont(30.0f));

        // Create enter button
        JButton enterButton = new JButton("Enter");
        enterButton.setMargin(new Insets(10, 10, 10, 10));

        // Add the label and the text field to the panel
        multisportPanel.add(multisportLabel);
        multisportPanel.add(idTextField);
        multisportPanel.add(enterButton);

        // Add the panels to the window
        this.add(multisportPanel);

        SwingUtilities.updateComponentTreeUI(getContentPane());

        enterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cardNumber = idTextField.getText();

                if (!utils.ValidateData.ValidateMultisportNumber(cardNumber)) {
                    return;
                }

                message.sendRegisterMultisportCardMessage(SendToServer, cardNumber + "," + userID);

                try {
                    String message = ReadFromServer.readLine();
                    if (message.equals("true")) {
                        JOptionPane.showMessageDialog(null, "Multisport membership added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid card number", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    dispose();
                } catch (IOException e1) {
                    CustomLogger.logError("Error reading from server: " + e1.getMessage());
                }
            }
        });

    }

}
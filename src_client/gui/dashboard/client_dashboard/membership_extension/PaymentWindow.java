package gui.dashboard.client_dashboard.membership_extension;

import javax.swing.*;

import utils.Message;
import utils.ValidateData;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class PaymentWindow extends JFrame {

    public PaymentWindow(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer, int userID, int amount, int gymId, int allGymAccess) {
        setTitle("Payment");
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // payment form panel
        JPanel paymentFormPanel = new JPanel(new GridLayout(8, 1, 10, 10));
        paymentFormPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // amount label
        JLabel amountLabel = new JLabel("Amount: " + amount + " PLN");
        amountLabel.setFont(new Font("Arial", Font.BOLD, 16)); 
        paymentFormPanel.add(amountLabel);

        // paymment method label
        JLabel paymentLabel = new JLabel("Choose payment method:");
        paymentFormPanel.add(paymentLabel);

        // card radio button
        JRadioButton cardRadioButton = new JRadioButton("Card");
        cardRadioButton.setSelected(true);
        paymentFormPanel.add(cardRadioButton);

        // blik radio button
        JRadioButton blikRadioButton = new JRadioButton("BLIK");
        paymentFormPanel.add(blikRadioButton);

        // cash radio button
        JRadioButton cashRadioButton = new JRadioButton("Cash");
        paymentFormPanel.add(cashRadioButton);

        // group radio buttons
        ButtonGroup group = new ButtonGroup();
        group.add(cardRadioButton);
        group.add(blikRadioButton);
        group.add(cashRadioButton);

        // blki code label
        JLabel blikCodeLabel = new JLabel("Enter BLIK code:");
        paymentFormPanel.add(blikCodeLabel);

        JTextField blikCodeField = new JTextField();
        paymentFormPanel.add(blikCodeField);

        // payment button
        JButton payButton = new JButton("Pay");
        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String paymentMethod = "";

                // Get payment method
                if (cardRadioButton.isSelected()) {
                    paymentMethod = "Card";
                } else if (blikRadioButton.isSelected()) {
                    paymentMethod = "BLIK";
                } else if (cashRadioButton.isSelected()) {
                    paymentMethod = "Cash";
                }

                String blikCode = blikCodeField.getText();

                if (paymentMethod.equals("BLIK") && !ValidateData.ValidateBlik(blikCode)) {
                    return;
                }
                if (paymentMethod.equals("Cash")) {
                    JOptionPane.showMessageDialog(PaymentWindow.this, "Insert cash into the machine", "Payment", JOptionPane.INFORMATION_MESSAGE);
                }

                JOptionPane.showMessageDialog(PaymentWindow.this, "Payment processing...", "Payment", JOptionPane.INFORMATION_MESSAGE);

                // Log payment
                message.sendPaymentMessage(SendToServer, amount + "," + paymentMethod.toLowerCase() + "," + userID + "," + gymId + "," + allGymAccess);

                // Get response from server
                String serverResponse;
                try {
                    serverResponse = ReadFromServer.readLine();
                    // split 
                    String[] parts = serverResponse.split(":", 2);
                    serverResponse = parts[0];
                    String membershipCard = parts[1];

                    if (serverResponse.equals("true")) {
                        JOptionPane.showMessageDialog(PaymentWindow.this, "<html><h2>Payment successful!<h2><h3>Your card number: " + membershipCard + "</html>", "Payment", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(PaymentWindow.this, "Payment failed!", "Payment", JOptionPane.ERROR_MESSAGE);
                        dispose();
                    }
                } catch (IOException e1) {
                    System.out.println(utils.Color.ANSI_RED + "Error: " + e1.getMessage() + utils.Color.ANSI_RESET);
                }

            }
        });
        paymentFormPanel.add(payButton);

        // Dodanie panelu formularza płatności do głównego panelu
        mainPanel.add(paymentFormPanel, BorderLayout.CENTER);

        // Ustawienia okna
        add(mainPanel);
        setVisible(true);
    }
}

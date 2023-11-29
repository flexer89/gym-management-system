package gui.dashboard.client_dashboard.membership_extension;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaymentWindow extends JFrame {

    public PaymentWindow(int amount) {
        setTitle("Payment");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // payment form panel
        JPanel paymentFormPanel = new JPanel(new GridLayout(7, 1, 10, 10));
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
                // Obsługa płatności - tutaj możesz dodać logikę obsługi płatności
                String paymentMethod = cardRadioButton.isSelected() ? "Karta" : "BLIK";
                String blikCode = blikCodeField.getText();
                JOptionPane.showMessageDialog(PaymentWindow.this,
                        "Płatność za karnet przyjęta. Metoda płatności: " + paymentMethod +
                                (blikRadioButton.isSelected() ? ", Kod BLIK: " + blikCode : ""));
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

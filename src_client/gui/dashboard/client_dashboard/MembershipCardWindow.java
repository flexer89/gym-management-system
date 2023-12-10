package gui.dashboard.client_dashboard;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import gui.dashboard.client_dashboard.membership_extension.AllGymMembership;
import gui.dashboard.client_dashboard.membership_extension.OneGymMembership;
import gui.dashboard.client_dashboard.membership_extension.PaymentWindow;
import utils.*;

public class MembershipCardWindow extends JFrame{
    public MembershipCardWindow(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer, int userID) throws IOException {
        // Create the main window
        this.setSize(1280, 768);
        this.setLayout(new BorderLayout());
        this.setVisible(true);
        this.setTitle("Gym Management System | Membership Card Management");

        // Create a main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIFormat.LIGHT_CREAMY_BACKGROUND);

        // Create a header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(UIFormat.WHITE_BACKGROUND);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create a title label
        JLabel titleLabel = new JLabel("Gym Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(UIFormat.DARK_GREY_FOREGROUND);

        // Add the title label to the header panel
        headerPanel.add(titleLabel);

        // Add the header panel to the main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Create a content panel
        JPanel contentPanel = new JPanel(new GridLayout(2, 1));
        contentPanel.setBackground(UIFormat.WHITE_BACKGROUND);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create a current data panel
        JPanel currentData = new JPanel(new GridLayout(5, 1));
        currentData.setBorder(BorderFactory.createTitledBorder("Current Informations"));
        currentData.setBackground(UIFormat.WHITE_BACKGROUND);

        // Send the message to the server
        message.sendGetMembershipCardMessage(SendToServer, Integer.toString(userID));

        // Get the response from the server
        String response = ReadFromServer.readLine();

        // Debug print
        System.out.println("Response: " + response);
        
        // Split the response
        String[] responseSplit = response.split(",");
        if (responseSplit.length != 5) {
            JLabel noMembershipLabel = new JLabel("<html><b>You don't have a membership card!</b></html>");
            noMembershipLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
            noMembershipLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            currentData.add(noMembershipLabel);
        } else {
            String membershipNumber = responseSplit[0];
            String expirationDate = responseSplit[1];
            String membershipType = responseSplit[2];
            String allGymAccess = responseSplit[3];
            String originalGym = responseSplit[4];
            
            // Debug prints
            System.out.println("Membership Number: " + membershipNumber);
            System.out.println("Expiration Date: " + expirationDate);
            System.out.println("Membership Type: " + membershipType);
            System.out.println("Original Gym: " + originalGym);
            System.out.println("All Gym Access: " + allGymAccess);



            // Create labels for the current data panel
            JLabel membershipNumberLabel = new JLabel("<html><b>Membership Number: </b> " + membershipNumber + "</html>");
            membershipNumberLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
            membershipNumberLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            JLabel expirationDateLabel = new JLabel("<html><b>Expiration Date: </b> " +  expirationDate + "</html>");
            expirationDateLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
            expirationDateLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            JLabel membershipTypeLabel = new JLabel("<html><b>Membership Type </b> " + membershipType + "</html>");
            membershipTypeLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
            membershipTypeLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            JLabel originalGymLabel = new JLabel("<html><b>Main Gym: </b> " + originalGym + "</html>");
            originalGymLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
            originalGymLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            JLabel allGymAccessLabel = new JLabel("<html><b>All gym Access: </b> " + allGymAccess + "</html>");
            allGymAccessLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
            allGymAccessLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            // Add the labels to the current data panel
            currentData.add(membershipNumberLabel);
            currentData.add(expirationDateLabel);
            currentData.add(membershipTypeLabel);
            currentData.add(originalGymLabel);
            currentData.add(allGymAccessLabel);

            // Add the multisport button if the membership type is not multisport
            if (!membershipType.equals("multisport")) {
                // Create multisport button
                JButton multisportButton = new JButton("I have multisport button");
                multisportButton.setBackground(UIFormat.LIGHT_CREAMY_BACKGROUND);
                multisportButton.setForeground(UIFormat.DARK_GREY_FOREGROUND);
                multisportButton.setFont(new Font("Arial", Font.PLAIN, 14));
                mainPanel.add(multisportButton, BorderLayout.SOUTH);
            }
        }

        // Create cancel subscription button
        JButton cancelSubscriptionButton = new JButton("Cancel Subscription");
        cancelSubscriptionButton.setBackground(UIFormat.LIGHT_CREAMY_BACKGROUND);
        cancelSubscriptionButton.setForeground(UIFormat.DARK_GREY_FOREGROUND);
        cancelSubscriptionButton.setFont(new Font("Arial", Font.PLAIN, 14));

        // Create an extend membership panel
        JPanel extendMembershipPanel = new JPanel(new GridLayout(1, 3));
        extendMembershipPanel.setBorder(BorderFactory.createTitledBorder("Extend Membership"));
        extendMembershipPanel.setBackground(UIFormat.WHITE_BACKGROUND);

        // Create Panels for the current data panel
        JPanel oneDayMembership = new JPanel(new GridLayout(2, 1));
        JPanel oneGymMembership = new JPanel(new GridLayout(2, 1));
        JPanel unlimitedMembership = new JPanel(new GridLayout(2, 1));

        // Create Labels for the current data panel
        String oneDayString = "<html>\n" +
            "<p style=\"font-weight: bold;\">One Day Membership - Enjoy a single, 24-hour access pass to any gym location in our network.</p>\n" +
            "<ul>\n" +
            "    <li>Ideal for those seeking a flexible and occasional workout option.</li>\n" +
            "    <li>Provides access to all facilities and services during the specified 24-hour period.</li>\n" +
            "</ul>\n" +
            "<p style=\"text-align: center;\"><b>Price: &nbsp;" + utils.Prices.ONE_DAY_MEMBERSHIP_PRICE + " PLN/day</b></p></html>";
        JLabel oneDayMembershipLabel = new JLabel(oneDayString);

        String oneGymString = "<html>\n" +
            "<p style=\"font-weight: bold;\">One Gym Membership - Access to a specific gym location within our network.</p>\n" +
            "<ul>\n" +
            "    <li>Perfect for individuals who prefer to work out consistently at a particular gym.</li>\n" +
            "    <li>Enjoy the amenities and services offered at your chosen gym with this membership.</li>\n" +
            "</ul>\n" +
            "<p style=\"text-align: center;\"><b>Price: &nbsp;" + utils.Prices.ONE_GYM_MEMBERSHIP_PRICE + " PLN/month</b></p></html>";
        JLabel oneGymMembershipLabel = new JLabel(oneGymString);

        String unlimitedString = "<html>\n" +
            "<p style=\"font-weight: bold;\">Unlimited Membership - Ultimate flexibility with unrestricted access to all gym locations in our network.</p>\n" +
            "<ul>\n" +
            "    <li>Explore various facilities and choose the gym that suits your preferences.</li>\n" +
            "    <li>Ideal for those who want the freedom to work out at any gym, anytime, as often as desired.</li>\n" +
            "</ul>\n" +
            "<p style=\"text-align: center;\"><b>Price: &nbsp;" + utils.Prices.FULL_MEMBERSHIP_PRICE + " PLN/month</b></p></html>";
        JLabel unlimitedMembershipLabel = new JLabel(unlimitedString);


        // Create buttons for the employee management panel
        JButton oneDayMembershipButton = new JButton("Choose");
        JButton oneGymMembershipButton = new JButton("Choose");
        JButton unlimitedMembershipButton = new JButton("Choose");

        // Set the style of the buttons
        oneDayMembershipButton.setBackground(UIFormat.LIGHT_CREAMY_BACKGROUND);
        oneDayMembershipButton.setForeground(UIFormat.DARK_GREY_FOREGROUND);
        oneDayMembershipButton.setFont(new Font("Arial", Font.PLAIN, 14));
        unlimitedMembershipButton.setBackground(UIFormat.LIGHT_CREAMY_BACKGROUND);
        unlimitedMembershipButton.setForeground(UIFormat.DARK_GREY_FOREGROUND);
        unlimitedMembershipButton.setFont(new Font("Arial", Font.PLAIN, 14));
        oneGymMembershipButton.setBackground(UIFormat.LIGHT_CREAMY_BACKGROUND);
        oneGymMembershipButton.setForeground(UIFormat.DARK_GREY_FOREGROUND);
        oneGymMembershipButton.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add the buttons to the membership management panel
        oneDayMembership.add(oneDayMembershipLabel);
        oneDayMembership.add(oneDayMembershipButton);
        unlimitedMembership.add(unlimitedMembershipLabel);
        unlimitedMembership.add(unlimitedMembershipButton);
        oneGymMembership.add(oneGymMembershipLabel);
        oneGymMembership.add(oneGymMembershipButton);

        // Add the panels to the current data panel
        extendMembershipPanel.add(oneDayMembership);
        extendMembershipPanel.add(oneGymMembership);
        extendMembershipPanel.add(unlimitedMembership);

        // Add panels to the content panel
        contentPanel.add(currentData);
        contentPanel.add(extendMembershipPanel);

        // Add the content panel to the main panel
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(cancelSubscriptionButton, BorderLayout.NORTH);

        // Add the main panel to the frame
        this.add(mainPanel);
        
        cancelSubscriptionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel your subscription?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    message.sendCancelSubscriptionMessage(SendToServer, Integer.toString(userID));
                    dispose();

                    try {
                        boolean subscriptionCancelled = Boolean.parseBoolean(ReadFromServer.readLine());
                        if (subscriptionCancelled) {
                            JOptionPane.showMessageDialog(null, "Subscription cancelled successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Error cancelling subscription!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        // ====================================================================================================
        // Add event listener for training management button
        oneDayMembershipButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LocalDate endDate = LocalDate.now().plusDays(1);
                new PaymentWindow(message, ReadFromServer, SendToServer, userID, utils.Prices.ONE_DAY_MEMBERSHIP_PRICE, 1, 1, endDate);
            }
        });

        // Add event listener for membership management button
        oneGymMembershipButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new OneGymMembership(message, ReadFromServer, SendToServer, userID);
            }
        });
        
        // Add event listener for profile management button
        unlimitedMembershipButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new AllGymMembership(message, ReadFromServer, SendToServer, userID);
            }
        });

    }
}

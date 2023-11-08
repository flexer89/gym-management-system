package gui.dashboard;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class EmployeeDashboard extends JFrame{
    public EmployeeDashboard() {
        // Create the main window
        this.setSize(400, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(1, 3));
        this.setVisible(true);
        this.setTitle("Gym Management System | Employee Dashboard");

        // Create Profile button
        JPanel profilePanel = new JPanel(new GridLayout(1, 1));
        JButton profileButton = new JButton("Profile");
        profilePanel.add(profileButton);

        // Create Membership button
        JPanel membershipPanel = new JPanel(new GridLayout(1, 1));
        JButton membershipButton = new JButton("Membership");
        membershipPanel.add(membershipButton);

        // Create Training button
        JPanel trainingPanel = new JPanel(new GridLayout(1, 1));
        JButton trainingButton = new JButton("Training");
        trainingPanel.add(trainingButton);

        // Add the components to the window
        this.add(profilePanel);
        this.add(membershipPanel);
        this.add(trainingPanel);
    }
}

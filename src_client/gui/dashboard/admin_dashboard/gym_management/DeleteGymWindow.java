package gui.dashboard.admin_dashboard.gym_management;

import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.swing.JFrame;

import utils.Message;

public class DeleteGymWindow extends JFrame {
    public DeleteGymWindow(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer) {
        // Create the main window
        this.setSize(800, 600);
        this.setVisible(true);
    }
}
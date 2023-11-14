package gui.dashboard.client_dashboard;

import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.swing.JFrame;

import utils.Message;

public class ProfileWindow extends JFrame {
    public ProfileWindow(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer) {
        // Create the main window
        this.setSize(800, 600);
        this.setVisible(true);
    }
}
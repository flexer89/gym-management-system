package gui.dashboard.trainer_dashboard.training_management;

import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.swing.JFrame;

import utils.Message;

public class AddTrainingWindow extends JFrame {
    public AddTrainingWindow(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer) {
        // Create the main window
        this.setSize(800, 600);
        this.setVisible(true);
    }
}
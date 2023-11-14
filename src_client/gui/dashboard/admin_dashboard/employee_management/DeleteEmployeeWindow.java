package gui.dashboard.admin_dashboard.employee_management;

import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.swing.JFrame;

import utils.Message;

public class DeleteEmployeeWindow extends JFrame {
    public DeleteEmployeeWindow(Message message, BufferedReader ReadFromServer, PrintWriter SendToServer) {
        // Create the main window
        this.setSize(800, 600);
        this.setVisible(true);
    }
}
import java.io.*;
import java.net.*;

import gui.login.LoginRegisterWindow;
import utils.Message;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GymClient {
    private static Socket clientSocket;
    private static BufferedReader ReadFromServer;
    private static PrintWriter SendToServer;
    private static Message message;
    private static LoginRegisterWindow loginRegisterWindow;
    private static JFrame frame;

    public static void main(String[] args) throws IOException {
        frame = new JFrame("Gym Client");
        JButton button = new JButton("Reconnect");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect();
            }
        });

        frame.getContentPane().add(button);
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        connect();
    }

    private static void connect() {
        try {
            // Create client socket
            clientSocket = new Socket("localhost", 5000);
            System.out.println("Connected to server");

            // Close the reconnect window
            frame.dispose();

            // Create input and output streams
            ReadFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            SendToServer = new PrintWriter(clientSocket.getOutputStream(), true);

            // Create message object
            message = new Message();

            // Create the login/register window
            loginRegisterWindow = new LoginRegisterWindow(message, ReadFromServer, SendToServer, clientSocket);
        } catch (ConnectException e) {
            JOptionPane.showMessageDialog(null, "Server is not running", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
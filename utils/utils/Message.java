package utils;
import java.io.PrintWriter;

public class Message {
    public void sendInputMessage(PrintWriter writer, String message) {
        writer.println("SEND:" + message);
    }

    public void sendExitMessage(PrintWriter writer, String message) {
        writer.println("EXIT:" + message);
    }

    public void sendPrintMessage(PrintWriter writer, String message) {
        writer.println("PRINT:" + message);
    }

    public void sendLoginMessage(PrintWriter writer, String message) {
        writer.println("LOGIN:" + message);
    }
    
    public void sendRegisterMessage(PrintWriter writer, String message) {
        writer.println("REGISTER:" + message);
    }
    public void sendCanEnterTrainingMessage(PrintWriter writer, String message) {
        writer.println("CAN_ENTER_TRAINING:" + message);
    }
    public void sendCanEnterGymMessage(PrintWriter writer, String message) {
        writer.println("CAN_ENTER_GYM:" + message);
    }

    public void sendCanExitGymMessage(PrintWriter writer, String message) {
        writer.println("CAN_EXIT_GYM:" + message);
    }

    public void sendAddGymMessage(PrintWriter writer, String message) {
        writer.println("ADD_GYM:" + message);
    }

    public void sendAddEmployeeMessage(PrintWriter writer, String message) {
        writer.println("ADD_EMPLOYEE:" + message);
    }
}

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

    public void sendPaymentReportMessage(PrintWriter writer, String message) {
        writer.println("PAYMENT_REPORT:" + message);
    }

    public void sendGymReportMessage(PrintWriter writer, String message) {
        writer.println("GYM_REPORT:" + message);
    }

    public void sendClientReportMessage(PrintWriter writer, String message) {
        writer.println("CLIENT_REPORT:" + message);
    }

    public void sendEmployeeReportMessage(PrintWriter writer, String message) {
        writer.println("EMPLOYEE_REPORT:" + message);
    }

    public void sendTrainingReportMessage(PrintWriter writer, String message) {
        writer.println("TRAINING_REPORT:" + message);
    }

    public void sendLoadGymMessage(PrintWriter writer, String message) {
        writer.println("LOAD_GYM:" + message);
    }

    public void sendDeleteGymMessage(PrintWriter writer, String message) {
        writer.println("DELETE_GYM:" + message);
    }

    public void sendDeleteEmployeeMessage(PrintWriter writer, String message) {
        writer.println("DELETE_EMPLOYEE:" + message);
    }

    public void sendloadEmployeesMessage(PrintWriter writer, String message) {
        writer.println("LOAD_EMPLOYEE:" + message);
    }

    public void sendGetClientMessage(PrintWriter writer, String message) {
        writer.println("GET_CLIENT:" + message);
    }

    public void sendGetTrainerMessage(PrintWriter writer, String message) {
        writer.println("GET_TRAINER:" + message);
    }

    public void sendAddTrainingMessage(PrintWriter writer, String message) {
        writer.println("ADD_TRAINING:" + message);
    }

    public void sendLoadTrainingsMessage(PrintWriter writer, String message) {
        writer.println("LOAD_TRAININGS:" + message);
    }

    public void sendReserveTrainingMessage(PrintWriter writer, String message) {
        writer.println("RESERVE_TRAINING:" + message);
    }

    public void sendTimeSpentReportMessage(PrintWriter writer, String message) {
        writer.println("TIME_SPENT_REPORT:" + message);
    }

    public void sendChangePasswordMessage(PrintWriter writer, String message) {
        writer.println("CHANGE_PASSWORD:" + message);
    }

    public void sendUpdateGymMessage(PrintWriter writer, String message) {
        writer.println("UPDATE_GYM:" + message);
    }

    public void sendUpdateEmployeeMessage(PrintWriter writer, String message) {
        writer.println("UPDATE_EMPLOYEE:" + message);
    }

    public void sendTimeSpentEmployeeReportMessage(PrintWriter writer, String message) {
        writer.println("TIME_SPENT_EMPLOYEE_REPORT:" + message);
    }

    public void sendGetMembershipCardMessage(PrintWriter writer, String message) {
        writer.println("GET_MEMBERSHIP_CARD:" + message);
    }

    public void sendPaymentMessage(PrintWriter writer, String message) {
        writer.println("PAYMENT:" + message);
    }

    public void sendCancelSubscriptionMessage(PrintWriter writer, String message) {
        writer.println("CANCEL_SUBSCRIPTION:" + message);
    }

    public void sendUpdateTrainingMessage(PrintWriter writer, String message) {
        writer.println("UPDATE_TRAINING:" + message);
    }

    public void sendLoadEmployeeTrainingsMessage(PrintWriter writer, String message) {
        writer.println("LOAD_EMPLOYEE_TRAININGS:" + message);
    }

    public void sendDeleteTrainingMessage(PrintWriter writer, String message) {
        writer.println("DELETE_TRAINING:" + message);
    }

    public void sendLoadClientTrainingsMessage(PrintWriter writer, String message) {
        writer.println("LOAD_CLIENT_TRAININGS:" + message);
    }

    public void sendCancelReservationMessage(PrintWriter writer, String message) {
        writer.println("CANCEL_RESERVATION:" + message);
    }
}

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import utils.Message;

import java.io.PrintWriter;
import java.io.StringWriter;

@RunWith(org.junit.runners.JUnit4.class)
public class MessageTest {
    @Test
    public void testSendInputMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput = "SEND:Hello World!";
        
        message.sendInputMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testSendExitMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput = "EXIT:Goodbye!";
        
        message.sendExitMessage(writer, "Goodbye!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testSendPrintMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput = "PRINT:Hello World!";
        
        message.sendPrintMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSendLoginMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput = "LOGIN:Hello World!";
        
        message.sendLoginMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSendRegisterMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput = "REGISTER:Hello World!";
        
        message.sendRegisterMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSendCanEnterTrainingMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput = "CAN_ENTER_TRAINING:Hello World!";
        
        message.sendCanEnterTrainingMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSendCanEnterGymMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput = "CAN_ENTER_GYM:Hello World!";
        
        message.sendCanEnterGymMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSendCanExitGymMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput = "CAN_EXIT_GYM:Hello World!";
        
        message.sendCanExitGymMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSendAddGymMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput = "ADD_GYM:Hello World!";
        
        message.sendAddGymMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSendAddEmployeeMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput = "ADD_EMPLOYEE:Hello World!";
        
        message.sendAddEmployeeMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSendPaymentReportMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput ="PAYMENT_REPORT:Hello World!";
        
        message.sendPaymentReportMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSendGymReportMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput ="GYM_REPORT:Hello World!";
        
        message.sendGymReportMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSendClientReportMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput ="CLIENT_REPORT:Hello World!";
        
        message.sendClientReportMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSendEmployeeReportMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput ="EMPLOYEE_REPORT:Hello World!";
        
        message.sendEmployeeReportMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSendTrainingReportMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput ="TRAINING_REPORT:Hello World!";
        
        message.sendTrainingReportMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSendLoadGymMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput ="LOAD_GYM:Hello World!";
        
        message.sendLoadGymMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSendDeleteGymMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput ="DELETE_GYM:Hello World!";
        
        message.sendDeleteGymMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSendDeleteEmployeeMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput ="DELETE_EMPLOYEE:Hello World!";
        
        message.sendDeleteEmployeeMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSendloadEmployeesMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput ="LOAD_EMPLOYEE:Hello World!";
        
        message.sendloadEmployeesMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGetClientMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput ="GET_CLIENT:Hello World!";
        
        message.sendGetClientMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGetTrainerMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput ="GET_TRAINER:Hello World!";
        
        message.sendGetTrainerMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAddTrainingMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput ="ADD_TRAINING:Hello World!";
        
        message.sendAddTrainingMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testLoadTrainingsMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput ="LOAD_TRAININGS:Hello World!";
        
        message.sendLoadTrainingsMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSendReserveTrainingMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput ="RESERVE_TRAINING:Hello World!";
        
        message.sendReserveTrainingMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSendTimeSpentReportMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput ="TIME_SPENT_REPORT:Hello World!";
        
        message.sendTimeSpentReportMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSendChangePasswordMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput ="CHANGE_PASSWORD:Hello World!";
        
        message.sendChangePasswordMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSendUpdateGymMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput ="UPDATE_GYM:Hello World!";
        
        message.sendUpdateGymMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSendUpdateEmployeeMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput ="UPDATE_EMPLOYEE:Hello World!";
        
        message.sendUpdateEmployeeMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSendTimeSpentEmployeeReportMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput ="TIME_SPENT_EMPLOYEE_REPORT:Hello World!";
        
        message.sendTimeSpentEmployeeReportMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSendGetMembershipCardMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput ="GET_MEMBERSHIP_CARD:Hello World!";
        
        message.sendGetMembershipCardMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSendPaymentMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput ="PAYMENT:Hello World!";
        
        message.sendPaymentMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSendCancelSubscriptionMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput ="CANCEL_SUBSCRIPTION:Hello World!";
        
        message.sendCancelSubscriptionMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSendUpdateTrainingMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput ="UPDATE_TRAINING:Hello World!";
        
        message.sendUpdateTrainingMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSendLoadEmployeeTrainingsMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput ="LOAD_EMPLOYEE_TRAININGS:1";
        
        message.sendLoadEmployeeTrainingsMessage(writer, 1);
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSendDeleteTrainingMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Message message = new Message();
        String expectedOutput ="DELETE_TRAINING:Hello World!";
        
        message.sendDeleteTrainingMessage(writer, "Hello World!");
        String actualOutput = stringWriter.toString().trim();
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }
}
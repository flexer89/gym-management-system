import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import utils.ValidateData;

@RunWith(org.junit.runners.JUnit4.class)
public class ValidateDataTest {
    @Test
    public void testValidateMail() {
        Assert.assertTrue(ValidateData.validateMail("test@example.com"));
        Assert.assertFalse(ValidateData.validateMail("invalid_email"));
        Assert.assertFalse(ValidateData.validateMail("invalid_email@"));
        Assert.assertFalse(ValidateData.validateMail("@domain"));
        Assert.assertFalse(ValidateData.validateMail("@"));
        Assert.assertFalse(ValidateData.validateMail(""));
        Assert.assertFalse(ValidateData.validateMail(" "));
    }

    @Test
    public void testValidateAddress() {
        Assert.assertTrue(ValidateData.validateAddress("123 Main St"));
        Assert.assertFalse(ValidateData.validateAddress("Invalid Address!"));
        Assert.assertFalse(ValidateData.validateAddress(" "));
    }

    @Test
    public void testValidateName() {
        Assert.assertTrue(ValidateData.validateName("John"));
        Assert.assertFalse(ValidateData.validateName("Invalid Name!"));
        Assert.assertFalse(ValidateData.validateName(" "));
    }

    @Test
    public void testValidateSurname() {
        Assert.assertTrue(ValidateData.validateSurname("Doe"));
        Assert.assertFalse(ValidateData.validateSurname("Invalid Surname!"));
        Assert.assertFalse(ValidateData.validateSurname(" "));
        Assert.assertFalse(ValidateData.validateSurname("Doe123"));
    }

    @Test
    public void testValidatePhoneNumber() {
        Assert.assertTrue(ValidateData.validatePhoneNumber("123456789"));
        Assert.assertFalse(ValidateData.validatePhoneNumber("Invalid Phone Number!"));
        Assert.assertFalse(ValidateData.validatePhoneNumber(" "));
        Assert.assertFalse(ValidateData.validatePhoneNumber("1234567890"));
        Assert.assertFalse(ValidateData.validatePhoneNumber("12345678"));
        Assert.assertFalse(ValidateData.validatePhoneNumber("123456789a"));
        Assert.assertFalse(ValidateData.validatePhoneNumber("123456789 "));
        Assert.assertFalse(ValidateData.validatePhoneNumber(" 123b56789"));
    }

    @Test
    public void testValidateDataRange() {
        Assert.assertTrue(ValidateData.ValidateDataRange("2022-01-01", "2022-12-31"));
        Assert.assertFalse(ValidateData.ValidateDataRange("2022-12-31", "2022-01-01"));
        Assert.assertFalse(ValidateData.ValidateDataRange("Invalid Date Range!", "2022-01-01"));
        Assert.assertFalse(ValidateData.ValidateDataRange("2022-01-01", "Invalid Date Range!"));
        Assert.assertFalse(ValidateData.ValidateDataRange("20000-01-01", "2000-01-01"));
        Assert.assertFalse(ValidateData.ValidateDataRange("2000-01-01", "20000-01-01"));
        Assert.assertTrue(ValidateData.ValidateDataRange("2000-01-01", "2000-01-01"));
        Assert.assertFalse(ValidateData.ValidateDataRange("2000-91-01", "2000-01-41"));
    }

    @Test
    public void testValidatePaymentOption() {
        Assert.assertTrue(ValidateData.ValidatePaymentOption("cash"));
        Assert.assertFalse(ValidateData.ValidatePaymentOption("Invalid Payment Method!"));
        Assert.assertFalse(ValidateData.ValidatePaymentOption(" "));
        Assert.assertFalse(ValidateData.ValidatePaymentOption("cash "));
    }

    @Test
    public void testValidatePaymentAmount() {
        Assert.assertTrue(ValidateData.ValidatePaymentAmount("100", "200"));
        Assert.assertFalse(ValidateData.ValidatePaymentAmount("200", "100"));
        Assert.assertFalse(ValidateData.ValidatePaymentAmount("Invalid Payment Amount!", "200"));
        Assert.assertFalse(ValidateData.ValidatePaymentAmount("100", "Invalid Payment Amount!"));
    }

    @Test
    public void testValidatePostalCode() {
        Assert.assertTrue(ValidateData.ValidatePostalCode("12-345"));
        Assert.assertFalse(ValidateData.ValidatePostalCode("Invalid Postal Code!"));
        Assert.assertFalse(ValidateData.ValidatePostalCode(" "));
        Assert.assertFalse(ValidateData.ValidatePostalCode("12-3456"));
        Assert.assertFalse(ValidateData.ValidatePostalCode("12-34"));
        Assert.assertFalse(ValidateData.ValidatePostalCode("12-34a"));
    }

    @Test
    public void testValidateCity() {
        Assert.assertTrue(ValidateData.ValidateCity("New York"));
        Assert.assertFalse(ValidateData.ValidateCity("Invalid City!"));
        Assert.assertFalse(ValidateData.ValidateCity(" "));
        Assert.assertTrue(ValidateData.ValidateCity("New York"));
        Assert.assertFalse(ValidateData.ValidateCity("New York "));
        Assert.assertFalse(ValidateData.ValidateCity("New York1"));
    }

    @Test
    public void testValidateHourRange() {
        Assert.assertTrue(ValidateData.ValidateHourRange("08:00", "18:00"));
        Assert.assertFalse(ValidateData.ValidateHourRange("18:00", "08:00"));
        Assert.assertFalse(ValidateData.ValidateHourRange("Invalid Hour Range!", "18:00"));
        Assert.assertFalse(ValidateData.ValidateHourRange("08:00", "Invalid Hour Range!"));
        Assert.assertTrue(ValidateData.ValidateHourRange("08:00", "08:00"));
    }

    @Test
    public void testValidateCapacity() {
        Assert.assertTrue(ValidateData.ValidateCapacity("50"));
        Assert.assertFalse(ValidateData.ValidateCapacity("Invalid Capacity!"));
        Assert.assertFalse(ValidateData.ValidateCapacity(" "));
        Assert.assertFalse(ValidateData.ValidateCapacity("50a"));
        Assert.assertFalse(ValidateData.ValidateCapacity("50 "));
    }

    @Test
    public void testValidateHour() {
        Assert.assertTrue(ValidateData.ValidateHour("12:30"));
        Assert.assertFalse(ValidateData.ValidateHour("Invalid Hour!"));
        Assert.assertFalse(ValidateData.ValidateHour(" "));
        Assert.assertFalse(ValidateData.ValidateHour("12:30a"));
        Assert.assertFalse(ValidateData.ValidateHour("12:30 "));
    }

    @Test
    public void testValidateDate() {
        Assert.assertTrue(ValidateData.ValidateDate("2022-01-01"));
        Assert.assertFalse(ValidateData.ValidateDate("Invalid Date!"));
        Assert.assertFalse(ValidateData.ValidateDate(" "));
        Assert.assertFalse(ValidateData.ValidateDate("2022-01-01a"));
        Assert.assertFalse(ValidateData.ValidateDate("2022-01-01 "));
    }

    @Test
    public void testValidateRoom() {
        Assert.assertTrue(ValidateData.ValidateRoom("101"));
        Assert.assertFalse(ValidateData.ValidateRoom("Invalid Room!"));
        Assert.assertFalse(ValidateData.ValidateRoom(" "));
        Assert.assertFalse(ValidateData.ValidateRoom("101a"));
        Assert.assertFalse(ValidateData.ValidateRoom("101 "));
    }

    @Test
    public void testValidateGymID() {
        Assert.assertTrue(ValidateData.ValidateGymID("123"));
        Assert.assertFalse(ValidateData.ValidateGymID("Invalid Gym ID!"));
        Assert.assertFalse(ValidateData.ValidateGymID(" "));
        Assert.assertFalse(ValidateData.ValidateGymID("123a"));
        Assert.assertFalse(ValidateData.ValidateGymID("123 "));
    }

    @Test
    public void testValidatePassword() {
        Assert.assertFalse(ValidateData.ValidatePassword("Password123"));
        Assert.assertFalse(ValidateData.ValidatePassword("invalid_password"));
        Assert.assertFalse(ValidateData.ValidatePassword(" "));
        Assert.assertFalse(ValidateData.ValidatePassword("Password"));
        Assert.assertFalse(ValidateData.ValidatePassword("Password123 "));
        Assert.assertFalse(ValidateData.ValidatePassword("Password123a"));
        Assert.assertTrue(ValidateData.ValidatePassword("Password123!"));
    }

    @Test
    public void testValidateUsername() {
        Assert.assertTrue(ValidateData.ValidateUsername("john_doe"));
        Assert.assertFalse(ValidateData.ValidateUsername("Invalid Username!"));
        Assert.assertFalse(ValidateData.ValidateUsername(" "));
        Assert.assertFalse(ValidateData.ValidateUsername("john_doe "));
        Assert.assertTrue(ValidateData.ValidateUsername("john_doe1"));
    }

    @Test
    public void testValidatePosition() {
        Assert.assertTrue(ValidateData.ValidatePosition("admin"));
        Assert.assertFalse(ValidateData.ValidatePosition("Invalid Position!"));
        Assert.assertFalse(ValidateData.ValidatePosition(" "));
        Assert.assertFalse(ValidateData.ValidatePosition("admin "));
        Assert.assertFalse(ValidateData.ValidatePosition("admin1"));
    }

    @Test
    public void testValidateBlik() {
        Assert.assertTrue(ValidateData.ValidateBlik("123456"));
        Assert.assertFalse(ValidateData.ValidateBlik("Invalid BLIK Code!"));
        Assert.assertFalse(ValidateData.ValidateBlik(" "));
        Assert.assertFalse(ValidateData.ValidateBlik("1234567"));
        Assert.assertFalse(ValidateData.ValidateBlik("12345"));
        Assert.assertFalse(ValidateData.ValidateBlik("12345a"));
    }

    @Test
    public void testValidateIsBefore() {
        Assert.assertTrue(ValidateData.ValidateIsBefore("2025-01-01"));
        Assert.assertFalse(ValidateData.ValidateIsBefore("2000-01-01"));
        Assert.assertFalse(ValidateData.ValidateIsBefore("Invalid Date!"));
        Assert.assertFalse(ValidateData.ValidateIsBefore(" "));
        Assert.assertFalse(ValidateData.ValidateIsBefore("2022-01-01a"));
        Assert.assertFalse(ValidateData.ValidateIsBefore("2022-01-01 "));
    }
}


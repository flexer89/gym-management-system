package utils;

import javax.swing.JOptionPane;

public class ValidateData {
    public static boolean validateMail(String email) {
        if (!email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$") && !email.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid email address!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean validateName(String name) {
        if (!name.matches("^[a-zA-Z]+$") && !name.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid name!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean validateSurname(String surname)
    {
        if (!surname.matches("^[a-zA-Z]+$") && !surname.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid surname!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        if (!phoneNumber.matches("^[0-9]{9}$") && !phoneNumber.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid phone number!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean ValidateDataRange(String fromDate, String toDate) {
        if ((!fromDate.matches("\\d{4}-\\d{2}-\\d{2}") && !fromDate.isEmpty()) || (!toDate.matches("\\d{4}-\\d{2}-\\d{2}") && !toDate.isEmpty())) {
            JOptionPane.showMessageDialog(null, "Date must be in the format yyyy-mm-dd!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Validate if the date is in the correct range
        if (!fromDate.isEmpty() && !toDate.isEmpty() && fromDate.compareTo(toDate) > 0) {
            JOptionPane.showMessageDialog(null, "From date must be before to date!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate correct month nmber
        if ((!fromDate.isEmpty() && Integer.parseInt(fromDate.substring(5, 7)) > 12) || (!toDate.isEmpty() && Integer.parseInt(toDate.substring(5, 7)) > 12)) {
            JOptionPane.showMessageDialog(null, "Invalid month number!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate correct day number
        if ((!fromDate.isEmpty() && Integer.parseInt(fromDate.substring(8, 10)) > 31) || (!toDate.isEmpty() && Integer.parseInt(toDate.substring(8, 10)) > 31)) {
            JOptionPane.showMessageDialog(null, "Invalid day number!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    public static boolean ValidatePaymentOption(String paymentMethod) {
        if (!paymentMethod.equals("cash") && !paymentMethod.equals("card") && !paymentMethod.equals("blik") && !paymentMethod.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Payment method must be cash, card or blik", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean ValidatePaymentAmount(String minimumPayment, String maximumPayment) 
    {
        if ((!minimumPayment.matches("\\d+") && !minimumPayment.isEmpty()) || (!maximumPayment.matches("\\d+") && !maximumPayment.isEmpty()) || (!minimumPayment.isEmpty() && !maximumPayment.isEmpty() && Integer.parseInt(minimumPayment) > Integer.parseInt(maximumPayment))) {
            JOptionPane.showMessageDialog(null, "Invalid payment amount!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean ValidatePostalCode(String postalCode) {
        if (!postalCode.matches("^[0-9]{2}-[0-9]{3}$") && !postalCode.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid postal code!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean ValidateCity(String city) {
        if (!city.matches("^[a-zA-Z]+$") && !city.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid city!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}

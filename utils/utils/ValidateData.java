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

    public static boolean ValidateHourRange(String fromHour, String toHour) {
        if ((!fromHour.matches("\\d{2}:\\d{2}") && !fromHour.isEmpty()) || (!toHour.matches("\\d{2}:\\d{2}") && !toHour.isEmpty())) {
            JOptionPane.showMessageDialog(null, "Hour must be in the format hh:mm!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Validate if the hour is in the correct range
        if (!fromHour.isEmpty() && !toHour.isEmpty() && fromHour.compareTo(toHour) > 0) {
            JOptionPane.showMessageDialog(null, "From hour must be before to hour!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate correct hour number
        if ((!fromHour.isEmpty() && Integer.parseInt(fromHour.substring(0, 2)) > 23) || (!toHour.isEmpty() && Integer.parseInt(toHour.substring(0, 2)) > 23)) {
            JOptionPane.showMessageDialog(null, "Invalid hour number!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate correct minute number
        if ((!fromHour.isEmpty() && Integer.parseInt(fromHour.substring(3, 5)) > 59) || (!toHour.isEmpty() && Integer.parseInt(toHour.substring(3, 5)) > 59)) {
            JOptionPane.showMessageDialog(null, "Invalid minute number!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }

    public static boolean ValidateCapacity(String capacity) {
        if (!capacity.matches("\\d+") && !capacity.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid capacity!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}

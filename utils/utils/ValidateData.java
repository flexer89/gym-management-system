package utils;

import java.time.LocalDate;

import javax.swing.JOptionPane;

public class ValidateData {
    public static boolean validateMail(String email) {
        if ((!email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$") || email.length() > 255) && !email.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid email address!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean validateAddress(String address) {
        if ((!address.matches("^[a-zA-Z0-9 ]+$") || address.length() > 255 || address.isBlank()) && !address.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid address!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean validateName(String name) {
        if ((name.length() > 255 || (!name.matches("^[a-zA-Z]+$") && !name.isEmpty()))) {
            JOptionPane.showMessageDialog(null, "Invalid name!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean validateSurname(String surname)
    {
        if (!surname.matches("^[a-zA-Z]+$") && (!surname.isEmpty() || surname.length() > 255)) {
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
        if ((!minimumPayment.matches("\\d+") && !minimumPayment.isEmpty()) || (!maximumPayment.matches("\\d+") && !maximumPayment.isEmpty()) || (!minimumPayment.isEmpty() && !maximumPayment.isEmpty() && Integer.parseInt(minimumPayment) >= Integer.parseInt(maximumPayment))) {
            JOptionPane.showMessageDialog(null, "Invalid payment amount!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean ValidatePostalCode(String postalCode) {
        if (!postalCode.matches("^[0-9]{2}-[0-9]{3}$") && !postalCode.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid postal code! (Should be xx-xxx)", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean ValidateCity(String city) {
        if (city.length() > 255 ||
            (!city.matches("^[a-zA-Z]+ [a-zA-Z]+$") && 
            (!city.matches("^[a-zA-Z]+$")))) {
            JOptionPane.showMessageDialog(null, "Invalid city!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean ValidateHourRange(String fromHour, String toHour) {
        if (((!fromHour.matches("\\d{2}:\\d{2}") && !fromHour.matches("\\d{2}:\\d{2}:00")) && !fromHour.isEmpty()) || ((!toHour.matches("\\d{2}:\\d{2}") && !toHour.matches("\\d{2}:\\d{2}:00")) && !toHour.isEmpty())) {
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
        if (!capacity.matches("\\d+") && (!capacity.isEmpty() || capacity.length() > 100)) {
            JOptionPane.showMessageDialog(null, "Invalid capacity!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean ValidateHour(String hour) {
        if (!hour.matches("\\d{2}:\\d{2}") && !hour.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid hour!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!hour.isEmpty() && Integer.parseInt(hour.substring(0, 2)) > 23) {
            JOptionPane.showMessageDialog(null, "Invalid hour number!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!hour.isEmpty() && Integer.parseInt(hour.substring(3, 5)) > 59) {
            JOptionPane.showMessageDialog(null, "Invalid minute number!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean ValidateDate(String date) {
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}") && !date.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid date!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!date.isEmpty() && Integer.parseInt(date.substring(5, 7)) > 12) {
            JOptionPane.showMessageDialog(null, "Invalid month number!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!date.isEmpty() && Integer.parseInt(date.substring(8, 10)) > 31) {
            JOptionPane.showMessageDialog(null, "Invalid day number!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean ValidateRoom(String room) {
        if (!room.matches("\\d+") && (!room.isEmpty() || room.length() > 100)) {
            JOptionPane.showMessageDialog(null, "Invalid room!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean ValidateGymID(String gymID) {
        if (!gymID.matches("\\d+") && (!gymID.isEmpty() || gymID.length() > 100)) {
            JOptionPane.showMessageDialog(null, "Invalid gym ID!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean ValidatePassword(String password) {
        if (password.length() > 255) {
            JOptionPane.showMessageDialog(null, "Password is too long", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // at least one digit, one lowercase, one uppercase and be at least 8 characters long
        if (password.contains(" ")) {
            JOptionPane.showMessageDialog(null, "Password cannot contain spaces", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            JOptionPane.showMessageDialog(null, "Password must contain at least one digit", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!password.matches(".*[a-z].*")) {
            JOptionPane.showMessageDialog(null, "Password must contain at least one lowercase letter", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!password.matches(".*[A-Z].*")) {
            JOptionPane.showMessageDialog(null, "Password must contain at least one uppercase letter", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            JOptionPane.showMessageDialog(null, "Password must contain at least one special character", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (password.length() < 8) {
            JOptionPane.showMessageDialog(null, "Password must be at least 8 characters long", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean ValidateUsername(String username) {
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            JOptionPane.showMessageDialog(null, "Username can only contain letters, numbers or _ sign", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean ValidatePosition(String position) {
        if (!position.equals("admin") && !position.equals("trainer") && !position.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Position must be admin or trainer", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean ValidateBlik(String blikCode) {
        if (!blikCode.matches("\\d{6}") || blikCode.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid BLIK code!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    public static boolean ValidateIsBefore(String date) {
        // Validate if the date is in the correct range
        if (!ValidateDate(date) || (ValidateDate(date) && date.compareTo(LocalDate.now().toString()) < 0)) {
            JOptionPane.showMessageDialog(null, "Date must be after today!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean ValidateCardNumber(String cardNumber) {
        if (!cardNumber.matches("\\d{8}") || (!cardNumber.startsWith("00") && 
        !cardNumber.startsWith("99"))) {
            JOptionPane.showMessageDialog(null, "Invalid card number!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean ValidateMultisportNumber(String cardNumber) {
        if (!cardNumber.matches("\\d{8}") || !cardNumber.startsWith("60")) {
            JOptionPane.showMessageDialog(null, "Invalid card number!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}

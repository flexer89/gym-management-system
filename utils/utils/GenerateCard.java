package utils;

import java.util.Random;

public class GenerateCard {
    private static final int CARD_NUMBER_LENGTH = 8;
    private static final String CLIENT_NUMBER_PREFIX = "00";
    private static final String EMPLOYEE_NUMBER_PREFIX = "99";

    public static String generateClientCardNumber() {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();

        cardNumber.append(CLIENT_NUMBER_PREFIX);
        for (int i = 0; i < CARD_NUMBER_LENGTH - CLIENT_NUMBER_PREFIX.length(); i++) {
            cardNumber.append(random.nextInt(10));
        }

        return cardNumber.toString();
    }

    public static String generateEmployeeCardNumber() {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();

        cardNumber.append(EMPLOYEE_NUMBER_PREFIX);
        for (int i = 0; i < CARD_NUMBER_LENGTH - EMPLOYEE_NUMBER_PREFIX.length(); i++) {
            cardNumber.append(random.nextInt(10));
        }

        return cardNumber.toString();
    }

    // Getter for CARD_NUMBER_LENGTH
    public static int getCardNumberLength() {
        return CARD_NUMBER_LENGTH;
    }

    // Getter for CLIENT_NUMBER_PREFIX
    public static String getClientNumberPrefix() {
        return CLIENT_NUMBER_PREFIX;
    }

    // Getter for EMPLOYEE_NUMBER_PREFIX
    public static String getEmployeeNumberPrefix() {
        return EMPLOYEE_NUMBER_PREFIX;
    }
}

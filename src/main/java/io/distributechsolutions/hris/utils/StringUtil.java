package io.distributechsolutions.hris.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A String utility class that provides the following functions:
 * 1.  Random Password Generator
 * 2.  Password Encryption
 * 3.  Input Stream Reader Content
 * 4.  String Numeric Checker
 */
public class StringUtil {
    private static StringUtil INSTANCE;

    private StringUtil() {
    }

    public synchronized static StringUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StringUtil();
        }

        return INSTANCE;
    }

    /**
     * This static method generates a random password that used in creating new user account.
     * @return The generated random password.
     */
    public static String generateRandomPassword() {
        return UUID.randomUUID().toString();
    }

    /**
     * Encrypts the input password using the bcrypt encyption.
     * @param rawPassword - The input password.
     * @return The encrypted password.
     */
    public static String encryptPassword(String rawPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
        return bCryptPasswordEncoder.encode(rawPassword);
    }

    /**
     * Reads each line of String from an input stream and returns as a whole document String.
     * @param inputStream - A stream of String coming from a resources file.
     * @return The content from the input stream.
     * @throws IOException
     */
    public static String readContentFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        return stringBuilder.toString();
    }

    /**
     * Checks if the input String is a number (either whole or decimal number).
     * Returns true if it is a numeric input, false if not.
     *
     * @param input - The expected numeric String input.
     * @return True if it is a numeric input, false if it is not.
     */
    public static boolean isNumeric(String input) {
        return input.matches("-?\\d+(\\.\\d+)?");
    }

    /**
     * Generates a string of leave code that will be used as a part of the data to save in the database.
     *
     * @param inputs - A sequence of input strings.
     * @return A concatenated input strings which is the leave code.
     */
    public static String generateDataCode(String... inputs) {
        String dataCode = "";

        for (String input : inputs) {
            Pattern pattern = Pattern.compile("\\(([^)]+)\\)");
            Matcher matcher = pattern.matcher(input);
            String output;

            if (matcher.find()) {
                output = matcher.group(1);
            } else {
                output = input;
            }

            dataCode = dataCode.concat(output);
        }

        return dataCode;
    }

    /**
     * This will check if the input String is a date or not.
     *
     * @param dateString - An input String date.
     * @return True, if the input is not a date. False, if it is a date.
     */
    public static boolean isNotDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        try {
            LocalDate.parse(dateString, formatter);
            return false; // The string is a date
        } catch (DateTimeParseException e) {
            return true; // The string is not a date
        }
    }
}

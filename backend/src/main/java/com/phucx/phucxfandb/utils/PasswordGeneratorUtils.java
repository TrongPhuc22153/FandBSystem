package com.phucx.phucxfandb.utils;

import java.security.SecureRandom;

public class PasswordGeneratorUtils {
    public static final int DEFAULT_PASSWORD_LENGTH = 10;
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String ALL_ALLOWED = LETTERS + DIGITS;
    private static final SecureRandom random = new SecureRandom();

    public static String generatePassword(int length) {
        if (length < 2) {
            throw new IllegalArgumentException("Password length must be at least 2 to include both a letter and a digit.");
        }

        StringBuilder password = new StringBuilder(length);

        password.append(LETTERS.charAt(random.nextInt(LETTERS.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));

        for (int i = 2; i < length; i++) {
            password.append(ALL_ALLOWED.charAt(random.nextInt(ALL_ALLOWED.length())));
        }

        return shuffleString(password.toString());
    }

    private static String shuffleString(String input) {
        char[] a = input.toCharArray();
        for (int i = a.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char tmp = a[i];
            a[i] = a[j];
            a[j] = tmp;
        }
        return new String(a);
    }
}

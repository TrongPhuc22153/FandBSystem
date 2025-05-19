package com.phucx.phucxfandb.constant;

public class EmailConstants {
    private EmailConstants() {}

    public static final String RESET_PASSWORD_SUBJECT = "Reset your password";
    public static final String RESET_PASSWORD_TEXT_TEMPLATE = """
        Hi %s,

        We received a request to reset the password for your account.

        If you made this request, please click the link below to reset your password:

        %s

        This link will expire in 30 minutes. If you didn’t request a password reset, you can safely ignore this email.

        Thanks,
        """;

    public static String PASSWORD_SUBJECT = "";
}

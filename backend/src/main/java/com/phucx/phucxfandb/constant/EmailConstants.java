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

    public static String PASSWORD_SUBJECT = "Your Account Password Assignment";
    public static String PASSWORD_TEXT_TEMPLATE = """
            Dear %s,
            
            Your account has been successfully created. Please find your login credentials below:
            
            Username: %s
            Password: %s
            
            For security reasons, we recommend that you log in and change your password immediately.
            
            If you did not request this account or believe this email was sent to you in error, please contact our support team.
            
            Best regards,
            """;
}

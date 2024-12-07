package com.laterna.xaxathonprime.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.application.base-url}")
    private String baseUrl;

    @Async
    public void sendEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email");
        }
    }

    public String buildVerificationEmailContent(String token) {
        return String.format("""
            <html><body>
                <h2>Email Verification</h2>
                <p>Please click the link below to verify your email:</p>
                <a href="%s/api/verify?token=%s">Verify Email</a>
            </body></html>
            """, baseUrl, token);
    }

    public String buildPasswordResetEmailContent(String token) {
        return String.format("""
            <html><body>
                <h2>Password Reset</h2>
                <p>Click the link below to reset your password:</p>
                <a href="%s/api/reset-password?token=%s">Reset Password</a>
            </body></html>
            """, baseUrl, token);
    }
}
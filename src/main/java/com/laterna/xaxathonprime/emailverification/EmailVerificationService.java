package com.laterna.xaxathonprime.emailverification;

import com.laterna.xaxathonprime.emailverification.dto.VerifyNewPasswordDto;
import com.laterna.xaxathonprime.user.User;
import com.laterna.xaxathonprime.user.UserService;
import com.laterna.xaxathonprime.user.dto.UserDto;
import com.laterna.xaxathonprime.verificationtoken.VerificationToken;
import com.laterna.xaxathonprime.verificationtoken.VerificationTokenService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    @Value("${spring.application.base-url}")
    private String baseUrl;

    private final UserService userService;
    private final VerificationTokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    private final JavaMailSender mailSender;

    @Async
    public void sendVerificationEmail(String to, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Email Verification");
            helper.setText(buildVerificationEmailText(token), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email");
        }
    }

    @Async
    public void sendPasswordResetEmail(String to) {
        try {
            UserDto user = userService.findByEmail(to);
            VerificationToken resetToken = tokenService.createToken(user);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Password Reset Request");
            helper.setText(buildPasswordResetEmailText(resetToken.getToken()), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email");
        }
    }

    private String buildVerificationEmailText(String token) {
        return String.format("""
        <html>
            <body>
                <h2>Email Verification</h2>
                <p>Please click the link below to verify your email:</p>
                <a href="%s/api/verify?token=%s">Verify Email</a>
            </body>
        </html>
        """, baseUrl, token);
    }

    private String buildPasswordResetEmailText(String token) {
        return String.format("""
            <html>
                <body>
                    <h2>Password Reset</h2>
                    <p>Click the link below to reset your password:</p>
                    <a href="%s/api/reset-password?token=%s">Reset Password</a>
                </body>
            </html>
            """, baseUrl, token);
    }

    public void verifyEmail(String token) {
        VerificationToken verificationToken = tokenService.findByToken(token);

        if (verificationToken == null) {
            throw new AccessDeniedException("Invalid verification token");
        }

        if (verificationToken.isExpired()) {
            throw new AccessDeniedException("Token expired");
        }

        User user = verificationToken.getUser();
        user.setEmailVerified(true);
        userService.save(user);
        tokenService.deleteToken(verificationToken);
    }

    public void resendVerificationEmail(String email) {
        UserDto user = userService.findByEmail(email);

        if (user.emailVerified()) {
            throw new AccessDeniedException("Email already verified");
        }

        tokenService.deleteByUser(user);
        VerificationToken newToken = tokenService.createToken(user);
        sendVerificationEmail(user.email(), newToken.getToken());
    }

    @Transactional
    public void resetPassword(VerifyNewPasswordDto dto) {
        VerificationToken resetToken = tokenService.findByToken(dto.token());

        if (resetToken == null || resetToken.isExpired()) {
            throw new AccessDeniedException("Invalid or expired token");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        userService.save(user);
        tokenService.deleteToken(resetToken);
    }
}
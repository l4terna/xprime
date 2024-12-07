package com.laterna.xaxathonprime.emailverification;

import com.laterna.xaxathonprime.email.EmailService;
import com.laterna.xaxathonprime.user.User;
import com.laterna.xaxathonprime.user.UserMapper;
import com.laterna.xaxathonprime.user.dto.UserDto;
import com.laterna.xaxathonprime.verificationtoken.VerificationToken;
import com.laterna.xaxathonprime.verificationtoken.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VerificationManagementService {
    private final EmailService emailService;
    private final VerificationTokenService tokenService;

    @Transactional
    public void handleUserCreation(UserDto user) {
        System.out.println(user);
        VerificationToken token = tokenService.createToken(user);
        emailService.sendEmail(
            user.email(),
            "Email Verification",
            emailService.buildVerificationEmailContent(token.getToken())
        );
    }

    @Transactional
    public void handleUserVerification(User user) {
        user.setEmailVerified(true);
    }
}
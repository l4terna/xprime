package com.laterna.xaxathonprime.emailverification;

import com.laterna.xaxathonprime.emailverification.dto.ResetPasswordDto;
import com.laterna.xaxathonprime.emailverification.dto.VerifyNewPasswordDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailVerificationController {
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        emailVerificationService.verifyEmail(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerification(@RequestParam String email) {
        emailVerificationService.resendVerificationEmail(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> requestPasswordReset(@RequestBody ResetPasswordDto resetPasswordDto) {
        emailVerificationService.sendPasswordResetEmail(resetPasswordDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password/verify")
    public ResponseEntity<?> verifyPasswordReset(
            @RequestBody VerifyNewPasswordDto newPassword
    ) {
        emailVerificationService.resetPassword(newPassword);
        return ResponseEntity.ok().build();
    }

}
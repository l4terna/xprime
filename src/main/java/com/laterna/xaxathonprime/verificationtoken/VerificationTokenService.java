package com.laterna.xaxathonprime.verificationtoken;

import com.laterna.xaxathonprime.user.User;
import com.laterna.xaxathonprime.user.UserMapper;
import com.laterna.xaxathonprime.user.UserService;
import com.laterna.xaxathonprime.user.dto.UserDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {
   private final VerificationTokenRepository tokenRepository;
   private final UserMapper userMapper;
   
   @Value("${verification.token.expiration.minutes:15}")
   private int expirationMinutes;

   public VerificationToken createToken(UserDto user) {
       VerificationToken token = new VerificationToken();
       token.setUser(userMapper.toEntity(user));
       token.setToken(generateVerificationCode());
       token.setExpiryDate(LocalDateTime.now().plusMinutes(expirationMinutes));
       return tokenRepository.save(token);
   }

    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

   public VerificationToken findByToken(String token) {
       return tokenRepository.findByToken(token)
           .orElseThrow(() -> new EntityNotFoundException("Token not found"));
   }

   public void deleteToken(VerificationToken token) {
       tokenRepository.delete(token);
   }

   public void deleteExpiredTokens() {
       tokenRepository.deleteAllByExpiryDateBefore(LocalDateTime.now());
   }

    public void deleteByUser(UserDto user) {
        tokenRepository.deleteByUserId(user.id());
    }
}
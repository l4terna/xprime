package com.laterna.xaxathonprime.verificationtoken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
   Optional<VerificationToken> findByToken(String token);
   void deleteAllByExpiryDateBefore(LocalDateTime date);
   void deleteByUserId(Long userId);
}
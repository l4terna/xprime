package com.laterna.xaxathonprime.auth;

import com.laterna.xaxathonprime.auth.dto.AuthDto;
import com.laterna.xaxathonprime.auth.dto.LoginDto;
import com.laterna.xaxathonprime.user.dto.CreateUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthDto> register(@RequestBody CreateUserDto createUserDto) {
        return ResponseEntity.ok(authService.register(createUserDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDto> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }
}

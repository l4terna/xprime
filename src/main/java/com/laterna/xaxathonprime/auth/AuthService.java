package com.laterna.xaxathonprime.auth;

import com.laterna.xaxathonprime.auth.dto.AuthDto;
import com.laterna.xaxathonprime.auth.dto.LoginDto;
import com.laterna.xaxathonprime.auth.exception.AuthenticationFailedException;
import com.laterna.xaxathonprime.user.UserService;
import com.laterna.xaxathonprime.user.dto.CreateUserDto;
import com.laterna.xaxathonprime.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthDto register(CreateUserDto createUserDto) {
        UserDto user = userService.create(createUserDto);
        String token = generateTokenBy(createUserDto.email(), createUserDto.password());

        return new AuthDto(token, user);
    }


    public AuthDto login(LoginDto loginDto) {
        if(userService.validatePassword(loginDto.email(), loginDto.password())) {
            String token = generateTokenBy(loginDto.email(), loginDto.password());
            UserDto user = userService.findByLogin(loginDto.email());

            return new AuthDto(token, user);
        }

        throw new AuthenticationFailedException("Invalid login or password");
    }

    private String generateTokenBy(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        );

        return jwtTokenProvider.generateToken(authentication);
    }
}

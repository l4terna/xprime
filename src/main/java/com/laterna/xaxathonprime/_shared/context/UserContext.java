package com.laterna.xaxathonprime._shared.context;

import com.laterna.xaxathonprime.user.CustomUserDetails;
import com.laterna.xaxathonprime.user.UserMapper;
import com.laterna.xaxathonprime.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserContext {
    private final UserMapper userMapper;

    public UserDto getCurrentUser() {
        CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(userDetails == null || userDetails.getUser() == null) {
            throw new AccessDeniedException("Access denied");
        }

        return userMapper.toDto(userDetails.getUser());
    }

    public Long getCurrentUserId() {
        return getCurrentUser().id();
    }
}

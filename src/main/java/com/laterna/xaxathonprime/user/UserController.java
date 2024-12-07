package com.laterna.xaxathonprime.user;

import com.laterna.xaxathonprime.user.dto.UpdateUserDto;
import com.laterna.xaxathonprime.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UpdateUserDto updateUserDto, @PathVariable Long id) {
        return ResponseEntity.ok(userService.updateUser(id, updateUserDto));
    }

}

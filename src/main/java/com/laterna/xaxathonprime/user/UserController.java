package com.laterna.xaxathonprime.user;

import com.laterna.xaxathonprime.user.dto.CreateUserDto;
import com.laterna.xaxathonprime.user.dto.UpdateUserDto;
import com.laterna.xaxathonprime.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserDto>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String search
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), "id");
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(userService.findUsers(pageRequest, search));
    }


    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @PostMapping("/region-admin")
    public ResponseEntity<UserDto> createRegionAdmin(@RequestBody CreateUserDto createUserDto) {
        return ResponseEntity.ok(userService.createRegionAdmin(createUserDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UpdateUserDto updateUserDto, @PathVariable Long id) {
        return ResponseEntity.ok(userService.updateUser(id, updateUserDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}

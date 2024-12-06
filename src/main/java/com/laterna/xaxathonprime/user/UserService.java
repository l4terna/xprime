package com.laterna.xaxathonprime.user;

import com.laterna.xaxathonprime._shared.context.UserContext;
import com.laterna.xaxathonprime.role.Role;
import com.laterna.xaxathonprime.role.RoleMapper;
import com.laterna.xaxathonprime.role.RoleService;
import com.laterna.xaxathonprime.role.dto.RoleDto;
import com.laterna.xaxathonprime.role.enumeration.RoleType;
import com.laterna.xaxathonprime.user.dto.CreateUserDto;
import com.laterna.xaxathonprime.user.dto.UpdateUserDto;
import com.laterna.xaxathonprime.user.dto.UserDto;
import com.laterna.xaxathonprime.user.exception.UserAlreadyExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleService roleService;
    private final UserContext userContext;
    private final RoleMapper roleMapper;

    @Transactional
    public UserDto create(CreateUserDto createUserDto) {
        if (userRepository.existsByEmail(createUserDto.email())) {
            throw new UserAlreadyExistsException("User with email " + createUserDto.email() + " already exists");
        }

        RoleDto roleDto = roleService.findByName(RoleType.USER.toString());

        User user = User.builder()
                .firstname(createUserDto.firstname())
                .patronymic(createUserDto.patronymic())
                .role(Role.builder().id(roleDto.id()).name(roleDto.name()).build())
                .lastname(createUserDto.lastname())
                .email(createUserDto.email())
                .password(passwordEncoder.encode(createUserDto.password()))
                .build();

        return userMapper.toDto(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserDto findByLogin(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
    }

    @Transactional(readOnly = true)
    public boolean validatePassword(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));

        return passwordEncoder.matches(password, user.getPassword());
    }

    @Transactional
    public UserDto updateUser(Long id, UpdateUserDto updateUserDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        User currentUser = userMapper.toEntity(userContext.getCurrentUser());

        if (currentUser.getId().equals(id) && updateUserDto.roleId() != null) {
            throw new AccessDeniedException("You cannot change your own role");
        }

        if (updateUserDto.roleId() != null) {
            RoleDto newRole = roleService.findById(updateUserDto.roleId());
            validateRoleAssignment(currentUser, newRole);
        }

        if (!currentUser.getId().equals(id)) {
            validateUserEditPermissions(currentUser, existingUser);
        }

        if (updateUserDto.firstname() != null) {
            existingUser.setFirstname(updateUserDto.firstname());
        }

        if (updateUserDto.firstname() != null) {
            existingUser.setLastname(updateUserDto.lastname());
        }

        if (updateUserDto.firstname() != null) {
            existingUser.setPatronymic(updateUserDto.patronymic());
        }

        if (updateUserDto.roleId() != null) {
            existingUser.setRole(roleMapper.toEntity(roleService.findById(updateUserDto.roleId())));
        }

        return userMapper.toDto(userRepository.save(existingUser));
    }

    private void validateRoleAssignment(User currentUser, RoleDto newRole) {
        try {
            RoleType currentRoleType = RoleType.valueOf(currentUser.getRole().getName());
            RoleType newRoleType = RoleType.valueOf(newRole.name());

            if (getRoleLevel(currentRoleType) <= getRoleLevel(newRoleType)) {
                throw new AccessDeniedException("You cannot assign role of equal or higher level than your own");
            }
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Invalid role type");
        }
    }

    private void validateUserEditPermissions(User currentUser, User targetUser) {
        try {
            RoleType currentRole = RoleType.valueOf(currentUser.getRole().getName());
            RoleType targetRole = RoleType.valueOf(targetUser.getRole().getName());

            if (getRoleLevel(currentRole) <= getRoleLevel(targetRole)) {
                throw new AccessDeniedException("You cannot edit users with equal or higher role level");
            }

            boolean isFspAdmin = currentRole == RoleType.FSP_ADMIN;
            boolean isRegionAdmin = currentRole == RoleType.REGION_ADMIN;
            boolean targetIsRegionAdmin = targetRole == RoleType.REGION_ADMIN;
            boolean targetIsUser = targetRole == RoleType.USER;

            if (!isFspAdmin && !isRegionAdmin) {
                throw new AccessDeniedException("You don't have permission to edit other users");
            }

            if (isRegionAdmin && !targetIsUser) {
                throw new AccessDeniedException("Region admin can only edit users with USER role");
            }

            if (isFspAdmin && !targetIsRegionAdmin && !targetIsUser) {
                throw new AccessDeniedException("FSP admin can only edit REGION_ADMIN and USER roles");
            }
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Role " + currentUser.getRole().getName() + " not found");
        }

    }

    private int getRoleLevel(RoleType roleType) {
        return switch (roleType) {
            case FSP_ADMIN -> 3;
            case REGION_ADMIN -> 2;
            case USER -> 1;
        };
    }

    @Transactional(readOnly = true)
    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }
}

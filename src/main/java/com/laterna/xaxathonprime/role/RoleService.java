package com.laterna.xaxathonprime.role;

import com.laterna.xaxathonprime.role.dto.RoleDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleDto findByName(String name) {
        return roleRepository.findByName(name)
                .map(roleMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Role not found: " + name));
    }

    public RoleDto findById(Long id) {
        return roleRepository.findById(id).map(roleMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Role not found: " + id));
    }

    public List<RoleDto> getRoles() {
        return roleRepository.findAll().stream().map(roleMapper::toDto).toList();
    }
}

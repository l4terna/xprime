package com.laterna.xaxathonprime.role;

import com.laterna.xaxathonprime.role.dto.RoleDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDto toDto(Role role);
    Role toEntity(RoleDto roleDto);
}

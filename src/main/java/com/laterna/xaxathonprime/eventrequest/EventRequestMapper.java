package com.laterna.xaxathonprime.eventrequest;

import com.laterna.xaxathonprime.eventbase.dto.CreateEventBaseDto;
import com.laterna.xaxathonprime.eventrequest.dto.CreateEventRequestDto;
import com.laterna.xaxathonprime.eventrequest.dto.EventRequestDto;
import com.laterna.xaxathonprime.region.Region;
import com.laterna.xaxathonprime.region.dto.RegionDto;
import com.laterna.xaxathonprime.role.Role;
import com.laterna.xaxathonprime.role.dto.RoleDto;
import com.laterna.xaxathonprime.user.User;
import com.laterna.xaxathonprime.user.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventRequestMapper {

    @Mapping(target = "name", source = "base.name")
    @Mapping(target = "gender", source = "base.gender")
    @Mapping(target = "minAge", source = "base.minAge")
    @Mapping(target = "maxAge", source = "base.maxAge")
    @Mapping(target = "location", source = "base.location")
    @Mapping(target = "disciplines", source = "base.disciplines")
    @Mapping(target = "startDate", source = "base.startDate")
    @Mapping(target = "endDate", source = "base.endDate")
    @Mapping(target = "maxParticipants", source = "base.maxParticipants")
    @Mapping(target = "region", expression = "java(mapRegionWithoutUserReference(eventRequest.getBase().getRegion()))")
    @Mapping(target = "baseId", source = "base.id")
    EventRequestDto toDto(EventRequest eventRequest);

    CreateEventBaseDto toCreateEventBaseDto(CreateEventRequestDto createEventRequestDto);

    // Helper method to break circular reference
    default RegionDto mapRegionWithoutUserReference(Region region) {
        if (region == null) {
            return null;
        }
        return new RegionDto(
                region.getId(),
                region.getName(),
                region.getContactEmail(),
                region.getDescription(),
                region.getCreatedAt(),
                region.getImageUrl(),
                region.getFederalDistrict(),
                mapUserWithoutRegionReference(region.getUser())
        );
    }

    // Helper method to break circular reference
    default UserDto mapUserWithoutRegionReference(User user) {
        if (user == null) {
            return null;
        }
        return new UserDto(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getPatronymic(),
                user.getEmail(),
                user.getEmailVerified(),
                mapRole(user.getRole()),
                null, // Breaking the circular reference by not including region
                user.getCreatedAt()
        );
    }

    RoleDto mapRole(Role role);
}

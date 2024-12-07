package com.laterna.xaxathonprime.team;

import com.laterna.xaxathonprime.region.RegionMapper;
import com.laterna.xaxathonprime.team.dto.AddTeamMemberDto;
import com.laterna.xaxathonprime.team.dto.CreateTeamDto;
import com.laterna.xaxathonprime.team.dto.TeamDto;
import com.laterna.xaxathonprime.team.dto.UpdateTeamDto;
import com.laterna.xaxathonprime.user.UserMapper;
import com.laterna.xaxathonprime.user.UserService;
import com.laterna.xaxathonprime.user.dto.UserDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserService userService;
    private final RegionMapper regionMapper;
    private final UserMapper userMapper;
    private final TeamMapper teamMapper;

    @Transactional
    public TeamDto createTeam(CreateTeamDto request) {
        UserDto currentUser = userService.getCurrentUser();
        if (currentUser.region() == null) {
            throw new AccessDeniedException("User must be assigned to a region to create a team");
        }

        Team team = Team.builder()
                .name(request.name())
                .description(request.description())
                .region(regionMapper.toEntity(currentUser.region()))
                .build();

        team.getUsers().add(userMapper.toEntity(currentUser));

        return teamMapper.toDto(teamRepository.save(team));
    }

    @Transactional
    public TeamDto updateTeam(Long teamId, UpdateTeamDto request) {
        UserDto currentUser = userService.getCurrentUser();
        Team team = teamRepository.findById(teamId)
            .orElseThrow(() -> new EntityNotFoundException("Team not found"));

        if (!currentUser.region().id().equals(team.getRegion().getId())) {
            throw new AccessDeniedException("You can only edit teams in your region");
        }

        team.setName(request.name());
        team.setDescription(request.description());

        Team updatedTeam = teamRepository.save(team);
        return teamMapper.toDto(updatedTeam);
    }

    @Transactional(readOnly = true)
    public TeamDto getTeam(Long teamId) {
        return teamRepository.findById(teamId)
            .map(teamMapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException("Team not found"));
    }

    @Transactional(readOnly = true)
    public List<TeamDto> getTeams(Long regionId) {
        List<Team> teams;
        if (regionId != null) {
            teams = teamRepository.findByRegionId(regionId);
        } else {
            teams = teamRepository.findAll();
        }
        return teams.stream()
                .map(teamMapper::toDto)
                .collect(Collectors.toList());
    }

    public TeamDto addMember(Long teamId, AddTeamMemberDto request) {
        UserDto currentUser = userService.getCurrentUser();
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));

        if (!currentUser.region().id().equals(team.getRegion().getId())) {
            throw new AccessDeniedException("You can only manage teams in your region");
        }

        UserDto userToAdd = userService.findById(request.userId());

        if (!team.getRegion().getId().equals(userToAdd.region().id())) {
            throw new IllegalArgumentException("Can only add users from the same region");
        }

        team.getUsers().add(userMapper.toEntity(userToAdd));

        return teamMapper.toDto(teamRepository.save(team));
    }

    public TeamDto removeMember(Long teamId, Long userId) {
        UserDto currentUser = userService.getCurrentUser();
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));

        if (!currentUser.region().id().equals(team.getRegion().getId())) {
            throw new AccessDeniedException("You can only manage teams in your region");
        }

        UserDto userToRemove = userService.findById(userId);

        if (team.getUsers().size() <= 1) {
            throw new IllegalStateException("Cannot remove the last team member");
        }

        team.getUsers().remove(userToRemove);

        return teamMapper.toDto(teamRepository.save(team));
    }
}
package com.laterna.xaxathonprime.team;

import com.laterna.xaxathonprime.region.RegionMapper;
import com.laterna.xaxathonprime.team.dto.AddTeamMemberDto;
import com.laterna.xaxathonprime.team.dto.CreateTeamDto;
import com.laterna.xaxathonprime.team.dto.TeamDto;
import com.laterna.xaxathonprime.team.dto.UpdateTeamDto;
import com.laterna.xaxathonprime.user.User;
import com.laterna.xaxathonprime.user.UserMapper;
import com.laterna.xaxathonprime.user.UserService;
import com.laterna.xaxathonprime.user.dto.UserDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;


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
    public Page<TeamDto> getTeams(Long regionId, Pageable pageable) {
        Page<Team> teams;
        if (regionId != null) {
            teams = teamRepository.findByRegionId(regionId, pageable);
        } else {
            teams = teamRepository.findAll(pageable);
        }
        return teams.map(teamMapper::toDto);
    }

    @Transactional
    public TeamDto addMember(Long teamId, AddTeamMemberDto request) {
        UserDto currentUser = userService.getCurrentUser();
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));

        if (currentUser.region() == null) {
            throw new IllegalStateException("Current user must be assigned to a region");
        }


        Long currentUserRegionId = currentUser.region().id();
        Long teamRegionId = team.getRegion().getId();
        if (!currentUserRegionId.equals(teamRegionId)) {
            throw new AccessDeniedException("You can only manage teams in your region");
        }

        UserDto userToAdd = userService.findById(request.userId());

        if (team.getUsers().stream().anyMatch(user -> user.getId().equals(userToAdd.id()))) {
            throw new IllegalStateException("User is already a member of this team");
        }

        team.getUsers().add(userMapper.toEntity(userToAdd));

        return teamMapper.toDto(teamRepository.save(team));
    }

    @Transactional
    public TeamDto removeMember(Long teamId, Long userId) {
        UserDto currentUser = userService.getCurrentUser();
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));

        if (!currentUser.region().id().equals(team.getRegion().getId())) {
            throw new AccessDeniedException("You can only manage teams in your region");
        }

        User userToDelete = team.getUsers().stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("User is not a member of this team"));

        team.getUsers().remove(userToDelete);

        return teamMapper.toDto(teamRepository.save(team));
    }

    @Transactional
    public void deleteTeam(Long teamId) {
        UserDto currentUser = userService.getCurrentUser();
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));

        if (!currentUser.region().id().equals(team.getRegion().getId())) {
            throw new AccessDeniedException("You can only delete teams in your region");
        }

        team.setUsers(new HashSet<>());
        teamRepository.save(team);

        teamRepository.delete(team);
    }

    public int countTeamsByRegionId(Long id) {
        return teamRepository.countTeamsByRegionId(id);
    }

    public int countParticipantsByRegionId(Long id) {
        return teamRepository.countParticipantsByRegionId(id);
    }

    public long countAll() {
        return teamRepository.count();
    }
}
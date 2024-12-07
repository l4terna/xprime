package com.laterna.xaxathonprime.team;

import com.laterna.xaxathonprime.eventrequest.dto.EventRequestDto;
import com.laterna.xaxathonprime.team.dto.AddTeamMemberDto;
import com.laterna.xaxathonprime.team.dto.CreateTeamDto;
import com.laterna.xaxathonprime.team.dto.TeamDto;
import com.laterna.xaxathonprime.team.dto.UpdateTeamDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<TeamDto> createTeam(@RequestBody CreateTeamDto request) {
        return ResponseEntity.ok(teamService.createTeam(request));
    }

    @PutMapping("/{teamId}")
    public ResponseEntity<TeamDto> updateTeam(
        @PathVariable Long teamId,
        @RequestBody UpdateTeamDto request
    ) {
        return ResponseEntity.ok(teamService.updateTeam(teamId, request));
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamDto> getTeam(@PathVariable Long teamId) {
        return ResponseEntity.ok(teamService.getTeam(teamId));
    }

    @DeleteMapping("/{teamId}")
    public void deleteTeam(@PathVariable Long teamId) {
        teamService.deleteTeam(teamId);
    }

    @GetMapping
    public ResponseEntity<Page<TeamDto>> getTeams(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) Long regionId) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), "id");
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<TeamDto> teams = teamService.getTeams(regionId, pageRequest);
        return ResponseEntity.ok(teams);
    }

    @PostMapping("/{teamId}/members")
    public ResponseEntity<TeamDto> addMember(
            @PathVariable Long teamId,
            @RequestBody AddTeamMemberDto request
    ) {
        return ResponseEntity.ok(teamService.addMember(teamId, request));
    }

    @DeleteMapping("/{teamId}/members/{userId}")
    public ResponseEntity<TeamDto> removeMember(
            @PathVariable Long teamId,
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(teamService.removeMember(teamId, userId));
    }
}
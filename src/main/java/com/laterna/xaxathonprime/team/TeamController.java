package com.laterna.xaxathonprime.team;

import com.laterna.xaxathonprime.team.dto.AddTeamMemberDto;
import com.laterna.xaxathonprime.team.dto.CreateTeamDto;
import com.laterna.xaxathonprime.team.dto.TeamDto;
import com.laterna.xaxathonprime.team.dto.UpdateTeamDto;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public ResponseEntity<List<TeamDto>> getTeams(@RequestParam(required = false) Long regionId) {
        return ResponseEntity.ok(teamService.getTeams(regionId));
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
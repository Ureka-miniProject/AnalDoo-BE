package com.Ureka.AnalDoo.domain.competition.dto.response.controller;

import com.Ureka.AnalDoo.domain.competition.dto.request.CompetitionCreateRequest;
import com.Ureka.AnalDoo.domain.competition.dto.response.CompetitionCreateResponse;
import com.Ureka.AnalDoo.domain.competition.dto.response.CompetitionDetailResponse;
import com.Ureka.AnalDoo.domain.competition.service.CompetitionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/competitions")
public class CompetitionController {
    private final CompetitionService competitionService;

    @PostMapping
    public ResponseEntity<CompetitionCreateResponse> createCompetition(@Valid @RequestBody CompetitionCreateRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        System.out.println(email + " " + authentication.getName());
        CompetitionCreateResponse response = competitionService.createCompetition(email, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{competitionId}")
    public ResponseEntity<Void> deleteCompetition(Authentication authentication,@PathVariable("competitionId") Long competitionId){

        competitionService.deleteCompetition(authentication.getName(),competitionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{competitionId}")
    public ResponseEntity<CompetitionDetailResponse> getCompetitionDetail(@PathVariable Long competitionId) {
        CompetitionDetailResponse response = competitionService.getCompetitionDetail(competitionId);
        return ResponseEntity.ok(response);
    }

}

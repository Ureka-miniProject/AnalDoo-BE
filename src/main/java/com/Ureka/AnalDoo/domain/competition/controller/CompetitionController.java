package com.Ureka.AnalDoo.domain.competition.controller;

import com.Ureka.AnalDoo.domain.competition.dto.request.CompetitionCreateRequest;
import com.Ureka.AnalDoo.domain.competition.dto.response.CompetitionCreateResponse;
import com.Ureka.AnalDoo.domain.competition.service.CompetitionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/competitions")
public class CompetitionController {
    private final CompetitionService competitionService;

    @PostMapping
    public ResponseEntity<CompetitionCreateResponse> createCompetition(@Valid @RequestBody CompetitionCreateRequest request) {
        CompetitionCreateResponse response = competitionService.createCompetition(request);
        return ResponseEntity.ok(response);
    }

}

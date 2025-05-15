package com.Ureka.AnalDoo.domain.competition.controller;

import com.Ureka.AnalDoo.domain.competition.dto.request.CompetitionCreateRequest;
import com.Ureka.AnalDoo.domain.competition.dto.response.CompetitionCreateResponse;
import com.Ureka.AnalDoo.domain.competition.dto.response.getCompetitionsResponse;
import com.Ureka.AnalDoo.domain.competition.dto.response.CompetitionDetailResponse;
import com.Ureka.AnalDoo.domain.competition.service.CompetitionService;
import com.Ureka.AnalDoo.domain.entity.enums.Local;
import com.Ureka.AnalDoo.domain.entity.enums.SportType;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
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
    public ResponseEntity<CompetitionCreateResponse> createCompetition(
            @Valid @RequestBody CompetitionCreateRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        System.out.println(email + " " + authentication.getName());
        CompetitionCreateResponse response = competitionService.createCompetition(email, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{competitionId}")
    public ResponseEntity<Void> deleteCompetition(Authentication authentication, @PathVariable("competitionId") Long competitionId) {

        competitionService.deleteCompetition(authentication.getName(), competitionId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/list")
    public ResponseEntity<getCompetitionsResponse> getCompetitions(@RequestParam("sportType") SportType sportType,
                                                                       @RequestParam(value = "local", required = false) Local local,
                                                                       @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                                                       @RequestParam(value = "lastDate", required = false)  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime lastDate,
                                                                       @RequestParam(value = "lastId", required = false) Long lastId,
                                                                       @PageableDefault(size = 10) Pageable pageable){

        getCompetitionsResponse response = competitionService.getCompetitions(date, sportType, local, lastDate, lastId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{competitionId}")
    public ResponseEntity<CompetitionDetailResponse> getCompetitionDetail(@PathVariable Long competitionId) {
        CompetitionDetailResponse response = competitionService.getCompetitionDetail(competitionId);
        return ResponseEntity.ok(response);
    }
}

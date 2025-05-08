package com.Ureka.AnalDoo.domain.competition.service;

import com.Ureka.AnalDoo.common.exception.RestApiException;
import com.Ureka.AnalDoo.common.exception.errorcode.UserErrorCode;
import com.Ureka.AnalDoo.domain.competition.dto.request.CompetitionCreateRequest;
import com.Ureka.AnalDoo.domain.competition.dto.response.CompetitionCreateResponse;
import com.Ureka.AnalDoo.domain.competition.repository.CompetitionRepository;
import com.Ureka.AnalDoo.domain.entity.Competition;
import com.Ureka.AnalDoo.domain.entity.CompetitionStatus;
import com.Ureka.AnalDoo.domain.entity.User;
import com.Ureka.AnalDoo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final UserRepository userRepository;

    // competition 생성
    @Transactional
    public CompetitionCreateResponse createCompetition(CompetitionCreateRequest request) {
        String email = ""; // 사용자 가져오는 방식 미정
        User manager = userRepository.findByEmail(email)
                .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));

        Competition competition = Competition.of(
                manager,
                request.getName(),
                request.getContent(),
                request.toCompetitionPeriod(),
                request.getEntryFee(),
                request.getEntryCount(),
                CompetitionStatus.OPEN,
                request.getSportType(),
                request.toAddress()
        );

        Competition savedCompetition = competitionRepository.save(competition);

        return new CompetitionCreateResponse(savedCompetition);

    }


}

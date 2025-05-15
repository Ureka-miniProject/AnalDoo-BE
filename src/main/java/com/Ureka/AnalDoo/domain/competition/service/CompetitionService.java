package com.Ureka.AnalDoo.domain.competition.service;

import com.Ureka.AnalDoo.common.exception.RestApiException;
import com.Ureka.AnalDoo.common.exception.errorcode.CompetitionErrorCode;
import com.Ureka.AnalDoo.common.exception.errorcode.UserErrorCode;
import com.Ureka.AnalDoo.domain.competition.dto.request.CompetitionCreateRequest;
import com.Ureka.AnalDoo.domain.competition.dto.response.CompetitionCreateResponse;
import com.Ureka.AnalDoo.domain.competition.dto.response.CompetitionSimpleResponse;
import com.Ureka.AnalDoo.domain.competition.dto.response.getCompetitionsResponse;
import com.Ureka.AnalDoo.domain.competition.repository.CompetitionRepository;
import com.Ureka.AnalDoo.domain.entity.Competition;
import com.Ureka.AnalDoo.domain.entity.User;
import com.Ureka.AnalDoo.domain.entity.enums.CompetitionStatus;
import com.Ureka.AnalDoo.domain.entity.enums.Local;
import com.Ureka.AnalDoo.domain.entity.enums.SportType;
import com.Ureka.AnalDoo.domain.payment.service.PaymentService;
import com.Ureka.AnalDoo.domain.reservation.repository.ReservationRepository;
import com.Ureka.AnalDoo.domain.user.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final UserRepository userRepository;
    private final PaymentService paymentService;
    private final ReservationRepository reservationRepository;

    // competition 생성
    @Transactional
    public CompetitionCreateResponse createCompetition(String email, CompetitionCreateRequest request) {
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

    @Transactional
    public void deleteCompetition(String email, Long competitionId) {

        Competition competition = competitionRepository.getById(competitionId);
        User user = userRepository.getByEmail(email);

        validateCompetitionRemove(user, competition);

        competition.delete();

        reservationRepository.findByCompetitionId(competitionId).forEach(reservation -> {

            reservation.delete();
            paymentService.cancelPayment(reservation);
        });

    }

    private void validateCompetitionRemove(User user, Competition competition) {
        if (!competition.getManager().getId().equals(user.getId())) {
            throw new RestApiException(CompetitionErrorCode.COMPETITION_USER_NOT_MATCH);
        }

        if (!competition.getPeriod().getCompetitionDate().isAfter(LocalDateTime.now().plusDays(7))) {
            throw new RestApiException(CompetitionErrorCode.COMPETITION_CANT_REMOVE);
        }
    }

    @Transactional(readOnly = true)
    public getCompetitionsResponse getCompetitions(LocalDate date, SportType sportType,
                                                   Local local, LocalDateTime lastDate, Long lastId,
                                                   Pageable pageable) {
        Slice<Competition> competitionSlice = competitionRepository.findAllByDateAndSportTypeAndLocal(
                date.atStartOfDay(), sportType, local, lastDate, lastId, pageable
        );

        List<CompetitionSimpleResponse> content = competitionSlice.getContent().stream()
                .map(CompetitionSimpleResponse::from)
                .toList();

        return getCompetitionsResponse.of(content, competitionSlice.hasNext());
    }
}

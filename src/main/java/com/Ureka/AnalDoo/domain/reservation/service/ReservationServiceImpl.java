package com.Ureka.AnalDoo.domain.reservation.service;

import com.Ureka.AnalDoo.common.exception.RestApiException;
import com.Ureka.AnalDoo.common.exception.errorcode.CompetitionErrorCode;
import com.Ureka.AnalDoo.common.exception.errorcode.ReservationErrorCode;
import com.Ureka.AnalDoo.domain.competition.repository.CompetitionRepository;
import com.Ureka.AnalDoo.domain.entity.Competition;
import com.Ureka.AnalDoo.domain.entity.enums.CompetitionStatus;
import com.Ureka.AnalDoo.domain.entity.Reservation;
import com.Ureka.AnalDoo.domain.entity.User;
import com.Ureka.AnalDoo.domain.reservation.dto.request.ReservationCreateRequest;
import com.Ureka.AnalDoo.domain.reservation.dto.response.ReservationCreateResponse;
import com.Ureka.AnalDoo.domain.reservation.repository.ReservationRepository;
import com.Ureka.AnalDoo.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final CompetitionRepository competitionRepository;

    @Transactional
    @Override
    public ReservationCreateResponse create(ReservationCreateRequest request, Long userId) {
        User user = userRepository.getById(userId);
        Competition competition = competitionRepository.findByIdWithLock(request.getCompetitionId())
                .orElseThrow(() -> new RestApiException(CompetitionErrorCode.COMPETITION_NOT_FOUND));

        validateReservationAvailable(competition);

        Reservation reservation = Reservation.of(user, competition);
        Reservation savedReservation = reservationRepository.save(reservation);
        competition.increaseEntryCount();

        return ReservationCreateResponse.from(savedReservation);
    }

    private void validateReservationAvailable(Competition competition) {
        LocalDateTime now = LocalDateTime.now();

        if (competition.getStatus() == CompetitionStatus.CLOSED || competition.getPeriod().getEndDate().isBefore(now)) {
            throw new RestApiException(ReservationErrorCode.RESERVATION_CLOSED);
        }

        if (competition.isFull()) {
            throw new RestApiException(ReservationErrorCode.RESERVATION_FULL);
        }
    }


}

package com.Ureka.AnalDoo.domain.reservation.service;

import com.Ureka.AnalDoo.common.exception.RestApiException;
import com.Ureka.AnalDoo.common.exception.errorcode.CompetitionErrorCode;
import com.Ureka.AnalDoo.common.exception.errorcode.ReservationErrorCode;
import com.Ureka.AnalDoo.domain.competition.repository.CompetitionRepository;
import com.Ureka.AnalDoo.domain.entity.Competition;
import com.Ureka.AnalDoo.domain.entity.Payment;
import com.Ureka.AnalDoo.domain.entity.enums.CompetitionStatus;
import com.Ureka.AnalDoo.domain.entity.Reservation;
import com.Ureka.AnalDoo.domain.entity.User;
import com.Ureka.AnalDoo.domain.entity.enums.PaymentStatus;
import com.Ureka.AnalDoo.domain.payment.repository.PaymentRepository;
import com.Ureka.AnalDoo.domain.payment.service.PaymentFacade;
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
    private final PaymentFacade paymentFacade;


    @Transactional
    @Override
    public ReservationCreateResponse create(ReservationCreateRequest request, String email) {
        User user = userRepository.getByEmail(email);
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

    @Transactional
    @Override
    public void delete(Long reservationId, String email) {

        User user = userRepository.getByEmail(email);
        Reservation reservation = reservationRepository.getById(reservationId);

        validateReservationRemove(reservation,user);

        reservation.delete();

        Competition competition = competitionRepository.findByIdWithLock(reservation.getCompetition().getId())
                .orElseThrow(() -> new RestApiException(CompetitionErrorCode.COMPETITION_NOT_FOUND));
        competition.decreaseEntryCount();

        paymentFacade.cancelPayment(reservation);

    }

    private void validateReservationRemove(final Reservation reservation,final User user){

        if(!reservation.getUser().getId().equals(user.getId())){
            throw new RestApiException(ReservationErrorCode.RESERVATION_USER_NOT_MATCH);
        }

    }
}

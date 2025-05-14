package com.Ureka.AnalDoo.domain.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.Ureka.AnalDoo.common.exception.RestApiException;
import com.Ureka.AnalDoo.common.exception.errorcode.ReservationErrorCode;
import com.Ureka.AnalDoo.domain.competition.repository.CompetitionRepository;
import com.Ureka.AnalDoo.domain.entity.Competition;
import com.Ureka.AnalDoo.domain.entity.Reservation;
import com.Ureka.AnalDoo.domain.entity.User;
import com.Ureka.AnalDoo.domain.entity.enums.CompetitionStatus;
import com.Ureka.AnalDoo.domain.reservation.dto.request.ReservationCreateRequest;
import com.Ureka.AnalDoo.domain.reservation.dto.response.ReservationCreateResponse;
import com.Ureka.AnalDoo.domain.reservation.repository.ReservationRepository;
import com.Ureka.AnalDoo.domain.user.repository.UserRepository;
import com.Ureka.AnalDoo.fixture.CompetitionFixture;
import com.Ureka.AnalDoo.fixture.ReservationFixture;
import com.Ureka.AnalDoo.fixture.UserFixture;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CompetitionRepository competitionRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Test
    @DisplayName("정상적인 대회 예약 요청은 성공적으로 처리된다")
    void should_reserve_successfully_when_competition_is_open_and_valid() {
        // given
        String userEmail = "1@naver.com";
        Long competitionId = 1L;

        User user = UserFixture.createUser("1@naver.com", "user1", "1111");
        Competition competition = CompetitionFixture.createCompetitionWithId(competitionId, user,
                CompetitionStatus.OPEN, 10);
        Reservation reservation = ReservationFixture.createReservation(user, competition);
        ReservationCreateRequest request = new ReservationCreateRequest(competitionId);

        given(userRepository.getByEmail(userEmail)).willReturn(user);
        given(competitionRepository.findByIdWithLock(competitionId)).willReturn(Optional.of(competition));
        given(reservationRepository.save(any(Reservation.class))).willReturn(reservation);

        // when
        ReservationCreateResponse response = reservationService.create(request, user.getEmail());

        // then
        assertThat(response.getReservationId()).isEqualTo(reservation.getId());
    }

    @Test
    @DisplayName("모집 상태가 CLOSED인 경우 예약은 실패한다")
    void should_fail_reservation_when_competition_status_is_closed() {
        // given
        String userEmail = "1@naver.com";
        Long competitionId = 1L;

        User user = UserFixture.createUser("1@naver.com", "user1", "1111");
        Competition closedCompetition = CompetitionFixture.createCompetitionWithId(competitionId, user,
                CompetitionStatus.CLOSED, 10);
        ReservationCreateRequest request = new ReservationCreateRequest(competitionId);

        given(userRepository.getByEmail(userEmail)).willReturn(user);
        given(competitionRepository.findByIdWithLock(competitionId)).willReturn(Optional.of(closedCompetition));

        // when & then
        assertThatThrownBy(() -> reservationService.create(request, user.getEmail()))
                .isInstanceOf(RestApiException.class)
                .hasFieldOrPropertyWithValue("errorCode", ReservationErrorCode.RESERVATION_CLOSED);
    }

    @Test
    @DisplayName("모집 기간이 지난 대회는 예약이 불가능하다")
    void should_fail_reservation_when_competition_end_date_has_passed() {
        // given
        String userEmail = "1@naver.com";
        Long competitionId = 1L;

        User user = UserFixture.createUser("1@naver.com", "user1", "1111");
        Competition expiredCompetition = CompetitionFixture.createExpiredCompetition(competitionId, user);
        ReservationCreateRequest request = new ReservationCreateRequest(competitionId);

        given(userRepository.getByEmail(userEmail)).willReturn(user);
        given(competitionRepository.findByIdWithLock(competitionId)).willReturn(Optional.of(expiredCompetition));

        // when & then
        assertThatThrownBy(() -> reservationService.create(request, user.getEmail()))
                .isInstanceOf(RestApiException.class)
                .hasFieldOrPropertyWithValue("errorCode", ReservationErrorCode.RESERVATION_CLOSED);
    }
}
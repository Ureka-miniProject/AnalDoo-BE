package com.Ureka.AnalDoo.domain.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.Ureka.AnalDoo.common.exception.RestApiException;
import com.Ureka.AnalDoo.domain.competition.repository.CompetitionRepository;
import com.Ureka.AnalDoo.domain.entity.Competition;
import com.Ureka.AnalDoo.domain.entity.CompetitionStatus;
import com.Ureka.AnalDoo.domain.entity.Reservation;
import com.Ureka.AnalDoo.domain.entity.User;
import com.Ureka.AnalDoo.domain.reservation.dto.request.ReservationCreateRequest;
import com.Ureka.AnalDoo.domain.reservation.repository.ReservationRepository;
import com.Ureka.AnalDoo.domain.user.repository.UserRepository;
import com.Ureka.AnalDoo.fixture.CompetitionFixture;
import com.Ureka.AnalDoo.fixture.UserFixture;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReservationConcurrencyTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        reservationRepository.deleteAll();
        competitionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("동시성 테스트: 100명 예약 요청 중 최대 신청 가능 인원 수만 성공")
    void reserve_concurrently_upToMaxOnly() throws InterruptedException {
        // given
        int peopleCnt = 100;
        int entryLimit = 10;

        User user = userRepository.save(UserFixture.createUser("manager@naver.com"));
        Competition competition = competitionRepository.save(
                CompetitionFixture.createCompetition(user, CompetitionStatus.OPEN, entryLimit));

        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(peopleCnt);

        // when
        for (int i = 0; i < peopleCnt; i++) {
            final Long uid = (long) (i + 1);
            User testUser = userRepository.save(UserFixture.createUser(uid + "@naver.com"));

            executorService.execute(() -> {
                try {
                    reservationService.create(new ReservationCreateRequest(competition.getId()), testUser.getId());
                } catch (RestApiException ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        Competition updated = competitionRepository.getById(competition.getId());
        assertThat(updated.getCurrentEntryCount()).isEqualTo(entryLimit);

        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations).hasSize(entryLimit); // 딱 10명만 성공
    }
}

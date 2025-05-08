package com.Ureka.AnalDoo.fixture;

import com.Ureka.AnalDoo.domain.entity.Address;
import com.Ureka.AnalDoo.domain.entity.Competition;
import com.Ureka.AnalDoo.domain.entity.CompetitionPeriod;
import com.Ureka.AnalDoo.domain.entity.enums.CompetitionStatus;
import com.Ureka.AnalDoo.domain.entity.enums.Local;
import com.Ureka.AnalDoo.domain.entity.enums.SportType;
import com.Ureka.AnalDoo.domain.entity.User;
import java.time.LocalDateTime;
import org.springframework.test.util.ReflectionTestUtils;

public class CompetitionFixture {

    public static Competition createCompetition(User manager, CompetitionStatus status, int entryLimit) {
        Competition competition = Competition.of(
                manager,
                "대회 제목",
                "테스트용 대회입니다.",
                createUpcomingPeriod(),
                5000,
                entryLimit,
                status,
                SportType.JIUJITSU,
                createAddress()
        );
        return competition;
    }

    public static Competition createCompetitionWithId(Long id, User manager, CompetitionStatus status, int limit) {
        Competition competition = createCompetition(manager, status, limit);
        ReflectionTestUtils.setField(competition, "id", id);
        return competition;
    }

    public static Competition createExpiredCompetition(Long competitionId, User manager) {
        Competition competition = Competition.of(
                manager,
                "마감된 대회",
                "기간이 지난 테스트용 대회입니다.",
                createPastPeriod(),
                5000,
                10,
                CompetitionStatus.OPEN,
                SportType.JIUJITSU,
                createAddress()
        );
        ReflectionTestUtils.setField(competition, "id", competitionId);
        return competition;
    }

    public static CompetitionPeriod createUpcomingPeriod() {
        LocalDateTime now = LocalDateTime.now();
        return new CompetitionPeriod(
                now.plusDays(1),
                now.plusDays(7),
                now.plusDays(10)
        );
    }

    public static CompetitionPeriod createPastPeriod() {
        LocalDateTime now = LocalDateTime.now();
        return new CompetitionPeriod(
                now.minusDays(10),
                now.minusDays(1),
                now.plusDays(1)
        );
    }

    public static Address createAddress() {
        return new Address(
                Local.SEOUL,
                "테헤란로 123",
                "06236",
                "101동 202호"
        );
    }
}

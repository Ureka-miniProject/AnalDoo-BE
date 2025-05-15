package com.Ureka.AnalDoo.domain.competition.repository;

import static com.Ureka.AnalDoo.domain.entity.QCompetition.competition;

import com.Ureka.AnalDoo.domain.entity.Competition;
import com.Ureka.AnalDoo.domain.entity.enums.CompetitionStatus;
import com.Ureka.AnalDoo.domain.entity.enums.Local;
import com.Ureka.AnalDoo.domain.entity.enums.SportType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@RequiredArgsConstructor
public class CompetitionRepositoryImpl implements CompetitonRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Competition> findAllByDateAndSportTypeAndLocal(LocalDateTime date, SportType sportType, Local local,
                                                                LocalDateTime lastDate, Long lastId,
                                                                Pageable pageable) {
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);
        int pageSize = pageable.getPageSize();

        List<Competition> results = queryFactory.selectFrom(competition)
                .where(
                        competition.isDeleted.eq(false),
                        competition.sportType.eq(sportType),
                        competition.status.eq(CompetitionStatus.OPEN),
                        competition.period.endDate.goe(LocalDateTime.now()),
                        localEq(local),
                        competition.period.competitionDate.between(startOfDay, endOfDay),
                        cursorCondition(lastDate, lastId)
                )
                .orderBy(
                        competition.period.competitionDate.asc(),
                        competition.id.asc()
                )
                .limit(pageSize + 1)
                .fetch();

        boolean hasNext = results.size() > pageSize;

        if (hasNext) {
            results.remove(pageSize);
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    private BooleanExpression localEq(Local local) {
        return local != null ? competition.address.local.eq(local) : null;
    }

    private BooleanExpression cursorCondition(LocalDateTime cursorDate, Long cursorId) {
        if (cursorDate == null || cursorId == null) {
            return null;
        }

        return competition.period.competitionDate.gt(cursorDate)
                .or(competition.period.competitionDate.eq(cursorDate)
                        .and(competition.id.gt(cursorId)));
    }
}

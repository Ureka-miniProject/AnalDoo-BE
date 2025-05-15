package com.Ureka.AnalDoo.domain.competition.repository;

import com.Ureka.AnalDoo.domain.entity.Competition;
import com.Ureka.AnalDoo.domain.entity.enums.Local;
import com.Ureka.AnalDoo.domain.entity.enums.SportType;
import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CompetitonRepositoryCustom{
    public Slice<Competition> findAllByDateAndSportTypeAndLocal(LocalDateTime date, SportType sportType, Local local, LocalDateTime cursorDate, Long cursorId, Pageable pageable);
}

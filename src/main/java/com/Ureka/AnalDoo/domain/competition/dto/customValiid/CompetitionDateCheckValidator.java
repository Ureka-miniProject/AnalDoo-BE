package com.Ureka.AnalDoo.domain.competition.dto.customValiid;

import com.Ureka.AnalDoo.domain.competition.dto.request.CompetitionCreateRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CompetitionDateCheckValidator implements ConstraintValidator<CompetitionDateCheck, CompetitionCreateRequest> {


    @Override
    public boolean isValid(CompetitionCreateRequest dto, ConstraintValidatorContext context) {
        LocalDateTime startDate = dto.getStartDate();
        LocalDateTime endDate = dto.getEndDate();
        LocalDateTime competitionDate = dto.getCompetitionDate();

        return !startDate.isAfter(endDate) && !endDate.isAfter(competitionDate);
    }
}

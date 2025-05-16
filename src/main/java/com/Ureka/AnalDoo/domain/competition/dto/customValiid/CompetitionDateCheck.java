package com.Ureka.AnalDoo.domain.competition.dto.customValiid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CompetitionDateCheckValidator.class)
public @interface CompetitionDateCheck {

    String message() default "날짜는 시작일자->종료일자->대회일자 순이 되어야 합니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

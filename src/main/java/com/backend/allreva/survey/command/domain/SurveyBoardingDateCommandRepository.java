package com.backend.allreva.survey.command.domain;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SurveyBoardingDateCommandRepository extends JpaRepository<SurveyBoardingDate, Long> {

    @Modifying
    @Query("DELETE FROM SurveyBoardingDate sbd WHERE sbd.survey = :survey")
    void deleteAllBySurveyForUpdate(@Param("survey") Survey survey);        // 물리삭제

    void deleteAllBySurvey(Survey survey);

    List<SurveyBoardingDate> findAllBySurvey(Survey survey);

}

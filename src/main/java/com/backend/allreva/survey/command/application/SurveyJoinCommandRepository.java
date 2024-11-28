package com.backend.allreva.survey.command.application;

import com.backend.allreva.survey.command.domain.SurveyJoin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyJoinCommandRepository extends JpaRepository<SurveyJoin, Long> {
}

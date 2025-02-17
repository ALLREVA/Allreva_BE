package com.backend.allreva.survey.command.application;

import com.backend.allreva.common.event.Events;
import com.backend.allreva.common.event.NotificationMessage;
import com.backend.allreva.concert.exception.ConcertNotFoundException;
import com.backend.allreva.concert.infra.dto.ConcertDateInfoResponse;
import com.backend.allreva.concert.infra.rdb.ConcertJpaRepository;
import com.backend.allreva.survey.command.application.request.OpenSurveyRequest;
import com.backend.allreva.survey.command.application.request.SurveyIdRequest;
import com.backend.allreva.survey.command.application.request.UpdateSurveyRequest;
import com.backend.allreva.survey.command.domain.*;
import com.backend.allreva.survey.exception.SurveyInvalidBoardingDateException;
import com.backend.allreva.survey.exception.SurveyNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SurveyCommandService {
    private final SurveyRepository surveyRepository;
    private final SurveyBoardingDateCommandRepository surveyBoardingDateCommandRepository;
    private final ConcertJpaRepository concertRepository;
    private final SurveyConverter surveyConverter;

    /**
     * 수요조사 개설
     */
    public Long openSurvey(final Long memberId,
                           final OpenSurveyRequest request) {
        //가용 날짜가 콘서트 진행 날짜인지 확인
        validateBoardingDates(request.concertId(), request.boardingDates());

        Survey survey = surveyRepository.save(surveyConverter.toSurvey(memberId, request));
        saveBoardingDates(survey, request.boardingDates());

        Events.raise(new SurveySavedEvent(survey));

        // push notification
        List<Long> recipientIds = List.of(memberId);
        Events.raise(
                NotificationMessage.NEW_SURVEY_REGISTERED
                        .toEvent(recipientIds, request.title())
        );

        return survey.getId();
    }

    /**
     * 수요조사 수정
     */
    public void updateSurvey(final Long memberId,
                             final UpdateSurveyRequest request) {
        Survey survey = findSurvey(request.surveyId());

        //가용 날짜가 콘서트 진행 날짜인지 확인
        validateBoardingDates(survey.getConcertId(), request.boardingDates());

        survey.isWriter(memberId);
        survey.update(request.title(),
                request.region(),
                request.endDate(),
                request.maxPassenger(),
                request.information()
        );
        updateBoardingDates(survey, request.boardingDates());
    }

    /**
     * 수요조사 삭제
     */
    public void removeSurvey(
            final Long memberId,
            final SurveyIdRequest surveyIdRequest
    ) {
        Survey survey = findSurvey(surveyIdRequest.surveyId());

        //작성자 확인
        survey.isWriter(memberId);

        surveyRepository.delete(survey);
        surveyBoardingDateCommandRepository.deleteAllBySurvey(survey);
        Events.raise(new SurveyDeletedEvent(survey.getId()));
    }

    /**
     * 수요조사 응답(신청)
     */

    private void saveBoardingDates(final Survey survey,
                                   final List<LocalDate> boardingDates) {
        boardingDates.forEach(date ->
                surveyBoardingDateCommandRepository.save(
                        SurveyBoardingDate.builder()
                                .survey(survey)
                                .date(date)
                                .build())
        );
    }

    private void updateBoardingDates(final Survey survey,
                                     final List<LocalDate> boardingDates) {
        surveyBoardingDateCommandRepository.deleteAllBySurveyForUpdate(survey);
        saveBoardingDates(survey, boardingDates);
    }

    private void validateBoardingDates(Long concertId, List<LocalDate> boardingDates) {
        ConcertDateInfoResponse dateInfo = findStartDateAndEndDateById(concertId);

        LocalDate concertStartDate = dateInfo.getStartDate();
        LocalDate concertEndDate = dateInfo.getEndDate();

        for (LocalDate boardingDate : boardingDates) {
            if (boardingDate.isBefore(concertStartDate) || boardingDate.isAfter(concertEndDate)) {
                throw new SurveyInvalidBoardingDateException();
            }
        }
    }

    private ConcertDateInfoResponse findStartDateAndEndDateById(final Long concertId) {
        return concertRepository.findStartDateAndEndDateById(concertId)
                .orElseThrow(ConcertNotFoundException::new);
    }


    private Survey findSurvey(final Long surveyId) {
        return surveyRepository.findById(surveyId)
                .orElseThrow(SurveyNotFoundException::new);
    }

}

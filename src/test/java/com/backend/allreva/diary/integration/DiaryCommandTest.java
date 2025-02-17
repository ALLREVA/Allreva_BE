package com.backend.allreva.diary.integration;

import com.backend.allreva.concert.command.domain.Concert;
import com.backend.allreva.concert.command.domain.ConcertRepository;
import com.backend.allreva.diary.command.application.DiaryCommandService;
import com.backend.allreva.diary.command.application.request.AddDiaryRequest;
import com.backend.allreva.diary.command.application.request.UpdateDiaryRequest;
import com.backend.allreva.diary.command.domain.ConcertDiary;
import com.backend.allreva.diary.command.domain.DiaryRepository;
import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.member.command.domain.MemberRepository;
import com.backend.allreva.support.IntegrationTestSupport;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

class DiaryCommandTest extends IntegrationTestSupport {

    @Autowired
    private DiaryRepository diaryRepository;
    @Autowired
    private ConcertRepository concertRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DiaryCommandService diaryCommandService;

    private Concert savedConcert;
    private Member savedMember;

    @BeforeEach
    void setUp() {
        savedConcert = createTestConcert();
        concertRepository.save(savedConcert);
        savedMember = createTestMember();
        memberRepository.save(savedMember);
    }

    @Transactional
    @DisplayName("공연 기록에 대한 정보를 입력하면 데이터베이스에 저장된다")
    @Test
    void saveDiaryTest() {

        // Given
        AddDiaryRequest request = new AddDiaryRequest(
                savedConcert.getId(),
                LocalDate.now(),
                "episode",
                "content",
                "seatName",
                List.of()
        );

        // When
        Long diaryId = diaryCommandService.add(request, savedMember.getId());
        ConcertDiary diary = diaryRepository.findById(diaryId).get();

        // Then
        Assertions.assertThat(diary.getConcertId()).isEqualTo(savedConcert.getId());
    }

    @Transactional
    @DisplayName("기존 공연 기록에 대한 정보를 수정하면 수정된 공연 기록이 데이터베이스에 저장된다")
    @Test
    void updateDiaryTest() {
        // Given
        AddDiaryRequest request = new AddDiaryRequest(
                savedConcert.getId(),
                LocalDate.now(),
                "episode",
                "content",
                "seatName",
                List.of()
        );
        Long diaryId = diaryCommandService.add(request, savedMember.getId());

        UpdateDiaryRequest updateRequest = new UpdateDiaryRequest(
                diaryId,
                savedConcert.getId(),
                LocalDate.now(),
                "episode",
                "updatedContent",
                "seatName",
                List.of()
        );

        // When
        diaryCommandService.update(updateRequest, savedMember.getId());
        ConcertDiary diary = diaryRepository.findById(diaryId).get();

        // Then
        Assertions.assertThat(diary.getConcertId()).isNotEqualTo("content");
        Assertions.assertThat(diary.getContent()).isEqualTo("updatedContent");
    }

    @Transactional
    @DisplayName("공연 기록을 삭제하면 해당 공연 기록을 조회할 수 없다")
    @Test
    void deleteDiaryTest() {

        // Given
        AddDiaryRequest request = new AddDiaryRequest(
                savedConcert.getId(),
                LocalDate.now(),
                "episode",
                "content",
                "seatName",
                List.of()

        );
        Long diaryId = diaryCommandService.add(request , savedMember.getId());

        // When
        diaryCommandService.delete(diaryId, savedMember.getId());

        // Then
        Assertions.assertThat(diaryRepository.findById(diaryId)).isEmpty();
    }

}

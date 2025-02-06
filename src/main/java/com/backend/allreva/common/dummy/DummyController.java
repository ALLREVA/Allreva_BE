package com.backend.allreva.common.dummy;

import com.backend.allreva.common.model.Image;
import com.backend.allreva.concert.command.domain.Concert;
import com.backend.allreva.concert.command.domain.ConcertRepository;
import com.backend.allreva.concert.command.domain.value.*;
import com.backend.allreva.diary.command.application.request.AddDiaryRequest;
import com.backend.allreva.diary.command.domain.ConcertDiary;
import com.backend.allreva.diary.command.domain.DiaryRepository;
import com.backend.allreva.hall.command.domain.ConcertHall;
import com.backend.allreva.hall.command.domain.ConcertHallRepository;
import com.backend.allreva.hall.command.domain.value.ConvenienceInfo;
import com.backend.allreva.hall.command.domain.value.Location;
import com.backend.allreva.member.command.application.MemberArtistCommandService;
import com.backend.allreva.member.command.application.MemberInfoCommandService;
import com.backend.allreva.member.command.application.request.MemberArtistRequest;
import com.backend.allreva.member.command.application.request.MemberRegisterRequest;
import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.member.command.domain.value.LoginProvider;
import com.backend.allreva.rent.command.application.RentCommandService;
import com.backend.allreva.rent.command.application.request.RentRegisterRequest;
import com.backend.allreva.rent.command.domain.value.BusSize;
import com.backend.allreva.rent.command.domain.value.BusType;
import com.backend.allreva.rent_join.command.application.RentJoinCommandService;
import com.backend.allreva.rent_join.command.application.request.RentJoinApplyRequest;
import com.backend.allreva.rent_join.command.domain.value.RefundType;
import com.backend.allreva.seat_review.command.application.SeatReviewService;
import com.backend.allreva.seat_review.command.application.dto.ReviewCreateRequest;
import com.backend.allreva.survey.command.application.SurveyCommandService;
import com.backend.allreva.survey.command.application.request.OpenSurveyRequest;
import com.backend.allreva.survey.command.domain.value.Region;
import com.backend.allreva.survey_join.command.application.SurveyJoinCommandService;
import com.backend.allreva.survey_join.command.application.request.JoinSurveyRequest;
import com.backend.allreva.survey_join.command.domain.value.BoardingType;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RequestMapping("/dummy")
@RestController
public class DummyController {

    private final ConcertRepository concertRepository;
    private final ConcertHallRepository concertHallRepository;

    private final SurveyCommandService surveyCommandService;
    private final SurveyJoinCommandService surveyJoinCommandService;

    private final RentCommandService rentCommandService;
    private final RentJoinCommandService rentJoinCommandService;

    private final DiaryRepository diaryRepository;
    private final SeatReviewService seatReviewService;

    private final MemberInfoCommandService memberInfoCommandService;

    @Operation(summary = "죽고싶을때 누르는 버튼 (더미 데이터 추가)",
            description = "zl존관현zl 이 그림자 분신술로 널 살려줌")
    @PostMapping("/oh-my-god")
    public void createDummy() {

        Concert concert = createConcertAndHallDummy();
        List<Member> members = createMemberDummy();

        createSurveyDummy(concert, members);
        createRentDummy(concert, members);
        createDiaryDummy(concert.getId(), members.get(0).getId());
        createReviewDummy(concert, members.get(0));
    }


    // Member Dummy
    private List<Member> createMemberDummy() {
        List<Member> members = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            MemberRegisterRequest request = new MemberRegisterRequest(
                    "khan_" + i + "@example.com",
                    "zl존관현zl_" + i,
                    "더미다임마_" + i,
                    LoginProvider.ORIGINAL,
                    List.of(new MemberArtistRequest("스포티파이 아티스트 아이디??", "baby lion 현상"))
            );
            Member member = memberInfoCommandService.registerMember(request, new Image("없어임마"));
            members.add(member);
        }
        return members;
    }


    // Concert & Hall Dummy
    private Concert createConcertAndHallDummy() {
        var concertHall = ConcertHall.builder()
                .id("hallCode")
                .name("hallName")
                .seatScale(2000)
                .convenienceInfo(
                        ConvenienceInfo.builder()
                                .hasCafe(true)
                                .hasParkingLot(true)
                                .hasRestaurant(true)
                                .hasStore(true)
                                .hasDisabledParking(true)
                                .hasDisabledToilet(true)
                                .hasElevator(true)
                                .hasRunway(true)
                                .build()
                )
                .location(Location.builder()
                        .latitude(0.0)
                        .longitude(1.2)
                        .address("address")
                        .build())
                .build();
        concertHallRepository.save(concertHall);

        // Concert 저장
        var dateInfo = DateInfo.builder()
                .startDate(LocalDate.now().plusYears(1))
                .endDate(LocalDate.now().plusYears(1).plusWeeks(1))
                .timeTable("timeTable")
                .build();

        var concertInfo = ConcertInfo.builder()
                .title("더미다 이자식아")
                .price("50000")
                .host("킹관현")
                .performStatus(ConcertStatus.IN_PROGRESS)
                .dateInfo(dateInfo)
                .build();

        var sellers = Set.of(
                Seller.builder()
                        .name("관현엔터테인먼트")
                        .salesUrl("kwan.url")
                        .build(),
                Seller.builder()
                        .name("지존엔터테인먼트")
                        .salesUrl("zizon.url")
                        .build(),
                Seller.builder()
                        .name("kopis 죽어")
                        .salesUrl("kill.kopis.url")
                        .build()
        );

        var concert = Concert.builder()
                .code(Code.builder()
                        .concertCode("concertCode")
                        .hallCode("hallCode")
                        .build())
                .concertInfo(concertInfo)
                .sellers(sellers)
                .poster(new Image("example.poster.image.url"))
                .detailImages(List.of(new Image("example.detail.image.url"), new Image("example.detail.image2.url")))
                .build();
        concertRepository.save(concert);
        return concert;
    }


    // Survey & SurveyJoin Dummy
    private void createSurveyDummy(Concert concert, List<Member> members) {

        Long managerId = members.get(0).getId();
        LocalDate startDate = concert.getConcertInfo().getDateInfo().getStartDate();
        LocalDate endDate = concert.getConcertInfo().getDateInfo().getEndDate();

        for (int i = 1; i <= 11; i++) {
            OpenSurveyRequest request = new OpenSurveyRequest(
                    "수요조사 제목 더미_" + i,
                    concert.getId(),
                    List.of(startDate.plusDays(1)),
                    "아기사자 하현상_" + i,
                    Region.경기,
                    endDate,
                    25,
                    "아기사자 현상이의 " + i + "번째 재롱잔치"
            );
            Long surveyId = surveyCommandService.openSurvey(managerId, request);

            for (int j = 1; j <= 11; j++) {
                JoinSurveyRequest joinRequest = new JoinSurveyRequest(
                        surveyId,
                        startDate.plusDays(1),
                        BoardingType.UP,
                        2,
                        false
                );
                surveyJoinCommandService.createSurveyResponse(
                        members.get(j).getId(),
                        joinRequest
                );
            }
        }
    }


    // Rent Dummy
    private void createRentDummy(Concert concert, List<Member> members) {
        LocalDate startDate = concert.getConcertInfo().getDateInfo().getStartDate();
        for (int i = 1; i <= 11; i++) {
            RentRegisterRequest request = new RentRegisterRequest(
                    concert.getId(),
                    "차대절이다 임마_" + i,
                    "아기사자 현상이다 임마_" + i,
                    com.backend.allreva.rent.command.domain.value.Region.경기,
                    "내 계좌다 임마_" + i,
                    "우리집 앞 " + i + "번지",
                    "업타임~~",
                    "다운타임베이비야~",
                    List.of(startDate.plusDays(1)),
                    BusSize.LARGE,
                    BusType.PREMIUM,
                    25,
                    4444444,
                    4444444,
                    3333333,
                    25,
                    startDate.minusDays(1),
                    "채팅url??",
                    RefundType.REFUND,
                    "차대절 더미데이터_" + i
            );

            Long rentId = rentCommandService.registerRent(
                    request,
                    new Image("rent.image.dummy"),
                    members.get(0).getId()
            );

            for (int j = 1; j <= 11; j++) {
                RentJoinApplyRequest joinRequest = new RentJoinApplyRequest(
                        rentId,
                        startDate.plusDays(1),
                        com.backend.allreva.rent_join.command.domain.value.BoardingType.UP,
                        2,
                        "zl존관현zl_" + i,
                        "지금",
                        "010-8415-6715",
                        RefundType.REFUND,
                        "계좌번호"
                );
                rentJoinCommandService.applyRent(
                        joinRequest,
                        members.get(i).getId()
                );
            }
        }
    }

    // Diary Dummy
    private void createDiaryDummy(Long concertId, Long memberId) {

        for (int i = 1; i <= 11; i++) {
            AddDiaryRequest request = new AddDiaryRequest(
                    concertId,
                    LocalDate.now(),
                    "에피소드_" + i,
                    "즐거웠다.." + i,
                    i + "번째 좌석"
            );
            ConcertDiary diary = request.to();
            diary.addMemberId(memberId);

            diaryRepository.save(diary);
        }
    }

    private void createReviewDummy(Concert concert, Member member) {
        for (int i = 1; i <= 11; i++) {
            ReviewCreateRequest request = new ReviewCreateRequest(
                    LocalDate.now(),
                    "아기사자 현상이의 " + i + "번째 재롱잔치",
                    5,
                    i + "번째 좌석",
                    "즐거워따...",
                    concert.getCode().getHallCode(),
                    List.of(new Image("dummy.image.url"))
            );
            seatReviewService.createSeatReview(request, member);
        }

    }
}

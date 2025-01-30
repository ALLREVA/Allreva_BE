package com.backend.allreva.support;

import com.backend.allreva.common.config.JpaAuditingConfig;
import com.backend.allreva.common.model.Email;
import com.backend.allreva.common.model.Image;
import com.backend.allreva.concert.command.domain.Concert;
import com.backend.allreva.concert.command.domain.value.*;
import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.member.command.domain.value.LoginProvider;
import com.backend.allreva.member.command.domain.value.MemberRole;
import com.backend.allreva.survey.command.application.request.OpenSurveyRequest;
import com.backend.allreva.survey.command.domain.value.Region;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static java.util.List.of;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "aws.region=us-east-1"
//        , "view.count.schedule.rate=20" // 이거 숫자를 낮추면 스케줄러 몇백번씩 실행????
})
@MockBean(JpaAuditingConfig.class)
@AutoConfigureMockMvc(addFilters = false)
public abstract class IntegrationTestSupport {

    protected AsyncAspect asyncAspect = new AsyncAspect();

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    // 테스트용 Member 객체 생성
    protected Member createTestMember() {
        return Member.builder()
                .email(new Email("example@example.com"))
                .memberRole(MemberRole.USER)
                .loginProvider(LoginProvider.GOOGLE)
                .nickname("JohnDoe")
                .introduce("Hello, I'm John.")
                .profileImageUrl("http://example.com/profile.jpg")
                .build();
    }

    // 테스트용 Concert 객체 생성
    protected Concert createTestConcert() {
        return Concert.builder()
                .code(Code.builder()
                        .hallCode("123")
                        .concertCode("456")
                        .build())
                .concertInfo(ConcertInfo.builder()
                        .title("Sample Concert")
                        .price("price")
                        .performStatus(ConcertStatus.IN_PROGRESS)
                        .host("host")
                        .dateInfo(DateInfo.builder()
                                .startDate(LocalDate.of(2030, 12, 1))
                                .endDate(LocalDate.of(2030, 12, 2))
                                .timeTable("timetable")
                                .build())
                        .build())
                .poster(new Image("http://example.com/poster.jpg"))
                .detailImages(List.of(new Image("http://example.com/detail1.jpg"), new Image("http://example.com/detail2.jpg")))
                .sellers(Set.of(Seller.builder()
                        .name("Sample Seller")
                        .salesUrl("http://seller.com")
                        .build()))
                .build();
    }

    protected OpenSurveyRequest createOpenSurveyRequest(Long concertId, LocalDate endDate, Region region) {
        return new OpenSurveyRequest(
                "하현상 콘서트: Elegy [서울] 수요조사 모집합니다.",
                concertId,
                of(LocalDate.of(2030, 12, 1), LocalDate.of(2030, 12, 2)),
                "하현상",
                region,
                endDate,
                25,
                "이틀 모두 운영합니다."
        );
    }
}

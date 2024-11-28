package com.backend.allreva.support;

import com.backend.allreva.common.config.JpaAuditingConfig;
import com.backend.allreva.common.model.Email;
import com.backend.allreva.common.model.Image;
import com.backend.allreva.concert.command.domain.Concert;
import com.backend.allreva.concert.command.domain.value.*;
import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.member.command.domain.value.LoginProvider;
import com.backend.allreva.member.command.domain.value.MemberRole;
import com.backend.allreva.survey.command.application.dto.OpenSurveyRequest;
import com.backend.allreva.survey.command.domain.value.Region;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Set;

import static java.util.List.of;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "aws.region=us-east-1",
        "view.count.schedule.rate=20"
})
@MockBean(JpaAuditingConfig.class)
@AutoConfigureMockMvc(addFilters = false)
public abstract class IntegrationTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected EntityManager entityManager;

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
                .concertInfo(new ConcertInfo("Sample Concert", "2024-12-01", ConcertStatus.IN_PROGRESS, "host",
                        new DateInfo(LocalDate.of(2024, 11, 30), LocalDate.of(2024, 12, 1), "timetable")))
                .poster(new Image("http://example.com/poster.jpg"))
                .detailImages(Set.of(new Image("http://example.com/detail1.jpg"), new Image("http://example.com/detail2.jpg")))
                .sellers(Set.of(new Seller("Sample Seller", "http://seller.com")))
                .build();
    }

    protected OpenSurveyRequest createOpenSurveyRequest(Long concertId, LocalDate endDate, Region region) {
        return new OpenSurveyRequest(
                "하현상 콘서트: Elegy [서울] 수요조사 모집합니다.",
                concertId,
                of("2024.11.30(토)", "2024.12.01(일)"),
                "하현상",
                region,
                endDate,
                25,
                "이틀 모두 운영합니다."
        );
    }
}

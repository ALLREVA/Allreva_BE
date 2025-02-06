package com.backend.allreva.chatting;

import com.backend.allreva.chatting.chat.integration.model.ChatParticipantRepository;
import com.backend.allreva.chatting.chat.single.command.application.SingleChatCommandService;
import com.backend.allreva.chatting.chat.single.command.domain.MemberSingleChatRepository;
import com.backend.allreva.chatting.chat.single.command.domain.SingleChatRepository;
import com.backend.allreva.chatting.chat.single.query.SingleChatQueryService;
import com.backend.allreva.common.event.Events;
import com.backend.allreva.member.command.domain.AddedMemberEvent;
import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.member.command.domain.MemberRepository;
import com.backend.allreva.member.command.domain.value.MemberRole;
import com.backend.allreva.member.fixture.MemberFixture;
import com.backend.allreva.support.IntegrationTestSupport;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SingleChatServiceTest extends IntegrationTestSupport {

    @Autowired
    private SingleChatCommandService singleChatCommandService;
    @Autowired
    private SingleChatQueryService singleChatQueryService;

    @Autowired
    private MemberSingleChatRepository memberSingleChatRepository;
    @Autowired
    private SingleChatRepository singleChatRepository;
    @Autowired
    private ChatParticipantRepository participantRepository;

    @Autowired
    private MemberRepository memberRepository;


    private Member memberA;
    private Member memberB;

    @BeforeEach
    void setup() throws InterruptedException {
        Member firstMember = MemberFixture.createMemberFixture(1L, MemberRole.USER);
        memberA = memberRepository.save(firstMember);
        Member secondMember = MemberFixture.createMemberFixture(2L, MemberRole.USER);
        memberB = memberRepository.save(secondMember);

        asyncAspect.init();
        AddedMemberEvent addedEvent = new AddedMemberEvent(memberA.getId());
        Events.raise(addedEvent);
        asyncAspect.await();

        asyncAspect.init();
        AddedMemberEvent addedEvent2 = new AddedMemberEvent(memberB.getId());
        Events.raise(addedEvent2);
        asyncAspect.await();
    }

    @AfterEach
    void teardown() {
        memberRepository.deleteAll();
        participantRepository.deleteAll();
        singleChatRepository.deleteAll();
        memberSingleChatRepository.deleteAll();
    }

    @DisplayName("개인 채팅 생성 시 참여하는 멤버의 채팅방 목록에 채팅방이 각각 추가된다.")
    @Test
    void createTest() throws InterruptedException {

        // Given, When
        asyncAspect.init(2);
        Long singleChatId = singleChatCommandService.startSingleChatting(
                memberA.getId(),
                memberB.getId()
        );
        asyncAspect.await();

        // Then
        var memberIds = memberSingleChatRepository
                .findAllMemberIdBySingleChatId(singleChatId);
        Assertions.assertThat(memberIds).hasSize(2);

        var participantDocs = participantRepository
                .findByMemberIdIn(memberIds);
        Assertions.assertThat(participantDocs).hasSize(2);
    }

    @DisplayName("나의 채팅방 제목은 상대방 이름이고, 상대의 채팅방 제목은 내 이름이다.")
    @Test
    void summaryTest() throws InterruptedException {

        // Given
        asyncAspect.init(2);
        Long singleChatId = singleChatCommandService.startSingleChatting(
                memberA.getId(),
                memberB.getId()
        );
        asyncAspect.await();

        // When
        var result = singleChatQueryService
                .findSingleChatInfo(memberA, singleChatId);

        var otherResult = singleChatQueryService
                .findSingleChatInfo(memberB, singleChatId);

        // Then
        Assertions.assertThat(result.getTitle())
                .isEqualTo(memberB.getMemberInfo().getNickname());

        Assertions.assertThat(otherResult.getTitle())
                .isEqualTo(memberA.getMemberInfo().getNickname());
    }
}

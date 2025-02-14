package com.backend.allreva.chatting;

import com.backend.allreva.chatting.chat.group.command.application.GroupChatCommandService;
import com.backend.allreva.chatting.chat.group.command.application.request.AddGroupChatRequest;
import com.backend.allreva.chatting.chat.group.command.domain.GroupChat;
import com.backend.allreva.chatting.chat.group.command.domain.GroupChatRepository;
import com.backend.allreva.chatting.chat.group.command.domain.MemberGroupChatRepository;
import com.backend.allreva.chatting.chat.group.query.GroupChatQueryService;
import com.backend.allreva.chatting.chat.group.query.response.GroupChatDetailResponse;
import com.backend.allreva.chatting.chat.integration.model.ChatParticipantRepository;
import com.backend.allreva.chatting.chat.integration.model.value.ChatSummary;
import com.backend.allreva.chatting.chat.integration.model.value.ChatType;
import com.backend.allreva.common.event.Events;
import com.backend.allreva.common.exception.NotFoundException;
import com.backend.allreva.common.model.Image;
import com.backend.allreva.member.command.domain.AddedMemberEvent;
import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.member.command.domain.MemberRepository;
import com.backend.allreva.member.command.domain.value.MemberRole;
import com.backend.allreva.member.fixture.MemberFixture;
import com.backend.allreva.rent.command.application.RentCommandFacade;
import com.backend.allreva.rent.infra.rdb.RentJpaRepository;
import com.backend.allreva.support.IntegrationTestSupport;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.backend.allreva.rent.fixture.RentRegisterRequestFixture.createRentRegisterRequestFixture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GroupChatServiceTest extends IntegrationTestSupport {

    @Autowired
    private GroupChatCommandService groupChatCommandService;
    @Autowired
    private GroupChatQueryService groupChatQueryService;

    @Autowired
    private RentCommandFacade rentCommandFacade;

    @Autowired
    private ChatParticipantRepository participantRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberGroupChatRepository memberGroupChatRepository;
    @Autowired
    private GroupChatRepository groupChatRepository;
    @Autowired
    private RentJpaRepository rentRepository;


    private Member savedMember;

    @BeforeEach
    void setup() throws InterruptedException {
        Member member = MemberFixture.createMemberFixture(1L, MemberRole.USER);
        savedMember = memberRepository.save(member);

        asyncAspect.init();
        AddedMemberEvent addedEvent = new AddedMemberEvent(savedMember.getId());
        Events.raise(addedEvent);
        asyncAspect.await();
    }

    @AfterEach
    void teardown() {
        memberRepository.deleteAll();
        participantRepository.deleteAll();
        memberGroupChatRepository.deleteAll();
        groupChatRepository.deleteAll();
        rentRepository.deleteAll();
    }


    @DisplayName("회원은 자신이 속한 단체 채팅의 정보를 조회할 수 있다")
    @Test
    void findDetailTest() throws InterruptedException {

        // Given
        AddGroupChatRequest request = new AddGroupChatRequest("title", 10);
        asyncAspect.init(2);
        Long groupChatId = groupChatCommandService
                .add(request, new Image("dummy"), savedMember.getId());
        asyncAspect.await();

        // When
        GroupChatDetailResponse result = groupChatQueryService
                .findGroupChatInfo(savedMember.getId(), groupChatId);

        // Then
        Assertions.assertThat(result.getMe().getMemberId()).isEqualTo(savedMember.getId());
        Assertions.assertThat(result.getManager().getMemberId()).isEqualTo(savedMember.getId());
    }

    @DisplayName("자신이 속하지 않은 단체 채팅 정보는 조회할 수 없다")
    @Test
    void validateDetailTest() throws InterruptedException {
        // Given
        AddGroupChatRequest request = new AddGroupChatRequest("title", 10);
        asyncAspect.init(2);
        Long groupChatId = groupChatCommandService
                .add(request, new Image("dummy"), savedMember.getId());
        asyncAspect.await();

        // When  // Then
        long savedMemberId = savedMember.getId() + 1;
        assertThatThrownBy(() -> groupChatQueryService
                .findGroupChatInfo(savedMemberId, groupChatId))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("차대절 글을 생성하면 글 작성자가 방장인 단체 채팅방이 개설된다.")
    @Test
    void createTest() throws InterruptedException {

        // Given
        var request = createRentRegisterRequestFixture();

        // When
        asyncAspect.init(2);
        rentCommandFacade.registerRent(request, new Image("test"), savedMember.getId());
        asyncAspect.await();

        // Then
        var participantDoc = participantRepository.findById(savedMember.getId())
                .orElseThrow(NotFoundException::new);
        Assertions.assertThat(participantDoc.getChatSummaries())
                .hasSize(1);
    }

    @DisplayName("단체 채팅방에 참가하면 참가한 멤버의 채팅방 목록에 추가된다.")
    @Test
    void joinGroupChatListTest() throws InterruptedException {

        // Given
        GroupChat groupChat = GroupChat.builder()
                .title("title test")
                .managerId(savedMember.getId())
                .capacity(10)
                .thumbnail(new Image("image test"))
                .build();

        groupChatRepository.save(groupChat);


        // When
        asyncAspect.init(2);
        groupChat.addHeadcount(savedMember.getId());
        asyncAspect.await();

        var participantDoc = participantRepository
                .findChatParticipantDocByMemberId(savedMember.getId())
                .orElseThrow(NotFoundException::new);


        // Then
        var chatSummary = ChatSummary.of(groupChat.getId(), ChatType.GROUP);
        assertThat(participantDoc.getChatSummaries()).contains(chatSummary);
    }

    @DisplayName("단체 채팅방의 uuid 로 단체 채팅의 개요를 조회할 수 있다. ")
    @Test
    void overviewTest() {

        // Given
        GroupChat groupChat = GroupChat.builder()
                .title("title test")
                .managerId(savedMember.getId())
                .capacity(10)
                .thumbnail(new Image("image test"))
                .build();

        groupChatRepository.save(groupChat);

        // When
        var result = groupChatQueryService
                .findOverview(groupChat.getUuid().toString());

        // Then
        Assertions.assertThat(result.getTitle())
                .isEqualTo(groupChat.getTitle().getValue());

    }


}

package com.backend.allreva.chatting;

import com.backend.allreva.chatting.chat.group.command.domain.GroupChat;
import com.backend.allreva.chatting.chat.group.command.domain.GroupChatRepository;
import com.backend.allreva.chatting.chat.group.command.domain.value.Title;
import com.backend.allreva.chatting.chat.integration.model.ChatParticipantDoc;
import com.backend.allreva.chatting.chat.integration.model.ChatParticipantRepository;
import com.backend.allreva.chatting.chat.integration.model.value.ChatSummary;
import com.backend.allreva.chatting.chat.integration.model.value.ChatType;
import com.backend.allreva.common.event.Events;
import com.backend.allreva.common.exception.CustomException;
import com.backend.allreva.common.exception.NotFoundException;
import com.backend.allreva.common.model.Image;
import com.backend.allreva.member.command.domain.AddedMemberEvent;
import com.backend.allreva.support.IntegrationTestSupport;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GroupChatTest extends IntegrationTestSupport {

    @Autowired
    private ChatParticipantRepository participantRepository;
    @Autowired
    private GroupChatRepository groupChatRepository;



    @DisplayName("그룹채팅을 수정하면 기존 그룹채팅의 내용이 바뀐다.")
    @Test
    void updateInfoTest() {

        // Given
        GroupChat groupChat = GroupChat.builder()
                .title("title")
                .description("description")
                .managerId(1L)
                .capacity(10)
                .thumbnail(new Image("image"))
                .build();

        // When
        groupChat.updateInfo(
                1L,
                "new title",
                "new description",
                new Image("new image")
        );

        // Then
        assertThat(groupChat)
                .extracting("title")
                .isEqualTo(new Title("new title"));
    }


    @DisplayName("방장만 그룹채팅을 수정할 수 있다.")
    @Test
    void validateUpdateInfoTest() {

        // Given
        GroupChat groupChat = GroupChat.builder()
                .title("title")
                .description("description")
                .managerId(1L)
                .capacity(10)
                .thumbnail(new Image("image"))
                .build();

        // When, Then
        assertThatThrownBy(() -> groupChat.updateInfo(
                2L,
                "new title",
                "new description",
                new Image("new image")
        )).isInstanceOf(CustomException.class);
    }


    @DisplayName("인원이 1명이 아니면 방장도 단체 채팅을 삭제할 수 없다.")
    @Test
    void validateDeleteTest() {

        // Given
        Long managerId = 1L;

        GroupChat groupChat = GroupChat.builder()
                .title("title")
                .description("description")
                .managerId(managerId)
                .capacity(10)
                .thumbnail(new Image("image"))
                .build();
        groupChat.addHeadcount(managerId);

        // When, Then
        assertThatThrownBy(() -> groupChat
                .validateForDelete(managerId))
                .isInstanceOf(CustomException.class);
    }

    @DisplayName("단체 채팅방에 참가하면 해당 단체 채팅방의 인원수가 1 증가한다.")
    @Test
    void joinGroupChatTest() {

        // Given
        Long managerId = 1L;
        Long memberId = 2L;

        GroupChat groupChat = GroupChat.builder()
                .title("title")
                .description("description")
                .managerId(managerId)
                .capacity(10)
                .thumbnail(new Image("image"))
                .build();

        // When
        groupChat.addHeadcount(memberId);

        // Then
        assertThat(groupChat.getHeadcount()).isEqualTo(2);
    }

    @DisplayName("단체 채팅방에 참가하면 참가한 멤버의 채팅방 목록에 추가된다.")
    @Test
    void joinGroupChatListTest() throws InterruptedException {

        // Given
        Long managerId = 1L;
        Long memberId = 2L;

        AddedMemberEvent addedEvent = new AddedMemberEvent(memberId);
        Events.raise(addedEvent);

        GroupChat groupChat = GroupChat.builder()
                .title("title")
                .description("description")
                .managerId(managerId)
                .capacity(10)
                .thumbnail(new Image("image"))
                .build();

        groupChatRepository.save(groupChat);


        // When
        asyncAspect.init(2);
        groupChat.addHeadcount(memberId);


        /**
         * 처음에 Document 가 생성이 되어야해
         */
        ChatParticipantDoc participantDoc = participantRepository.findChatParticipantDocByMemberId(memberId)
                .orElseThrow(NotFoundException::new);


        // Then
        asyncAspect.await();
        ChatSummary chatSummary = ChatSummary.of(groupChat.getId(), ChatType.GROUP);
        assertThat(participantDoc.getChatSummaries()).contains(chatSummary);

        participantRepository.deleteById(memberId);
    }
}

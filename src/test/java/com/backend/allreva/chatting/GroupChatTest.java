package com.backend.allreva.chatting;

import com.backend.allreva.chatting.chat.group.command.domain.GroupChat;
import com.backend.allreva.chatting.chat.group.command.domain.value.Title;
import com.backend.allreva.common.exception.CustomException;
import com.backend.allreva.common.model.Image;
import com.backend.allreva.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GroupChatTest extends IntegrationTestSupport {


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
}

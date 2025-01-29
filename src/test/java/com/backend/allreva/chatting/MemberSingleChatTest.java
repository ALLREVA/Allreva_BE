package com.backend.allreva.chatting;

import com.backend.allreva.chatting.chat.single.command.domain.MemberSingleChat;
import com.backend.allreva.chatting.chat.single.command.domain.value.OtherMember;
import com.backend.allreva.common.model.Image;
import com.backend.allreva.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MemberSingleChatTest extends IntegrationTestSupport {


    @DisplayName("개인 채팅을 시작하면 발신자와 수신자 각각 개인 채팅이 생긴다.")
    @Test
    void startSingleChatTest() {

        // Given
        Long singleChatId = 1L;
        Long memberId = 1L;
        Long otherMemberId = 2L;

        OtherMember member = new OtherMember(
                memberId,
                "memberName",
                new Image("image1")
        );
        OtherMember otherMember = new OtherMember(
                otherMemberId,
                "otherMemberName",
                new Image("image2")
        );

        // When
        Set<MemberSingleChat> chats = MemberSingleChat
                .startFirstChat(singleChatId, member, otherMember);


        // Then
        assertThat(chats)
                .extracting("otherMember")
                .contains(otherMember, member);
    }
}

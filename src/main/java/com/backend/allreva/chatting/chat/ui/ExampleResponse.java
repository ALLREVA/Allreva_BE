package com.backend.allreva.chatting.chat.ui;

import com.backend.allreva.common.model.Image;

import java.time.LocalDateTime;

public record ExampleResponse(

        String payload,
        LocalDateTime sendTime,
        int unreadMemberCount,

        Sender sender,

        LocalDateTime lastReadTime
) {

    private static class Sender {

        // memberId 에 그래도 본인 아이디는 넣어줘야하나?
        public static final Sender ME = new Sender();

        String memberId;
        String name;
        Image memberImage;

        boolean isMe;

        public Sender() {
            this.isMe = true;
        }

        public Sender(String memberId, String name, Image memberImage) {
            this.memberId = memberId;
            this.name = name;
            this.memberImage = memberImage;
            this.isMe = false;
        }
    }
}

package com.backend.allreva.chatting.chat.ui;

import com.backend.allreva.common.model.Image;

public record ChatSummaryResponse(

        String roomId,
        Boolean useNotification,

        String nickname,
        Image userImage,

        String lastMessage,
        String lastMessageTime,
        int unreadMessageCount

) {
}

package com.backend.allreva.chat.ui;

import com.backend.allreva.common.model.Image;

public record ChatRoomSummaryResponse(

        String roomId,
        Boolean useNotification,

        String nickname,
        Image userImage,

        String lastMessage,
        String lastMessageTime,
        int unreadMessageCount

) {
}

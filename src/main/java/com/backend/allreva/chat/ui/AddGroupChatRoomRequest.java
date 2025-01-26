package com.backend.allreva.chat.ui;

import jakarta.validation.constraints.NotEmpty;

public record AddGroupChatRoomRequest(

        @NotEmpty
        String chatRoomName

) {
}

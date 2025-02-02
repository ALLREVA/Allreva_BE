package com.backend.allreva.chatting.chat.group.query.response;

import com.backend.allreva.chatting.chat.group.command.domain.value.Title;
import com.backend.allreva.common.model.Image;

public record GroupChatSummaryResponse(

        Long id,

        Title title,
        Image Thumbnail,
        int headcount
) {
}

package com.backend.allreva.chatting.group_chat.query;

import com.backend.allreva.chatting.group_chat.command.domain.value.Description;
import com.backend.allreva.chatting.group_chat.command.domain.value.Title;
import com.backend.allreva.chatting.member_chatting.Participant;
import com.backend.allreva.common.model.Image;

import java.util.List;

public record GroupChatInfoResponse(

        Image groupChatThumbnail,
        Title title,
        Description description,
        List<Participant> participants
) {

}

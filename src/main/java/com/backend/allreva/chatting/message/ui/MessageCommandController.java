package com.backend.allreva.chatting.message.ui;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.chatting.chat.query.model.chat_room.PreviewMessage;
import com.backend.allreva.chatting.chat.query.model.chat_room.PreviewMessageUpdater;
import com.backend.allreva.chatting.chat.query.model.chat_room.RoomType;
import com.backend.allreva.chatting.message.command.MessageCommandService;
import com.backend.allreva.chatting.message.domain.GroupMessage;
import com.backend.allreva.chatting.message.domain.SingleMessage;
import com.backend.allreva.chatting.message.domain.value.Content;
import com.backend.allreva.chatting.message.infra.MessageSseService;
import com.backend.allreva.member.command.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class MessageCommandController {

    public static final String SUB_PERSONAL_DESTINATION = "/personal/room";
    public static final String SUB_GROUP_DESTINATION = "/group/room/";

    private final MessageCommandService messageCommandService;
    private final MessageSseService messageSseService;

    private final PreviewMessageUpdater previewMessageUpdater;

    private final SimpMessagingTemplate messagingTemplate;


    @MessageMapping("/single/{chatId}")
    public void sendSingleMessage(
            @DestinationVariable final Long chatId,
            @AuthMember final Member member,
            final Content content
    ) {
        String destination = SUB_PERSONAL_DESTINATION + chatId;
        SingleMessage singleMessage = messageCommandService
                .saveSingleMessage(chatId, content, member);

        messagingTemplate.convertAndSend(destination, singleMessage);

        messageSseService.sendSummaryToSingleChat(chatId, new PreviewMessage(
                singleMessage.getMessageNumber(),
                content.getPayload(),
                singleMessage.getSentAt()
        ));
        previewMessageUpdater.update(
                member.getId(),
                chatId,
                RoomType.SINGLE,
                singleMessage.getMessageNumber(),
                content.getPayload(),
                singleMessage.getSentAt()
        );
    }


    @MessageMapping("/group/{groupChatId}")
    public void sendGroupMessage(
            @DestinationVariable final Long groupChatId,
            @AuthMember final Member member,
            final Content content
    ) {
        String destination = SUB_GROUP_DESTINATION + groupChatId;
        GroupMessage groupMessage = messageCommandService
                .saveGroupMessage(groupChatId, content, member);

        messagingTemplate.convertAndSend(destination, groupMessage);

        messageSseService.sendSummaryToGroupChat(groupChatId, new PreviewMessage(
                groupMessage.getMessageNumber(),
                content.getPayload(),
                groupMessage.getSentAt()
        ));
        previewMessageUpdater.update(
                member.getId(),
                groupChatId,
                RoomType.GROUP,
                groupMessage.getMessageNumber(),
                content.getPayload(),
                groupMessage.getSentAt()
        );
    }


}

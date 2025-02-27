package com.backend.allreva.chatting.message.ui;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.chatting.chat.integration.model.value.ChatType;
import com.backend.allreva.chatting.chat.integration.model.value.PreviewMessage;
import com.backend.allreva.chatting.chat.integration.application.ChatParticipantService;
import com.backend.allreva.chatting.message.command.MessageCommandService;
import com.backend.allreva.chatting.message.domain.GroupMessage;
import com.backend.allreva.chatting.message.domain.SingleMessage;
import com.backend.allreva.chatting.message.domain.value.Content;
import com.backend.allreva.chatting.notification.MessageSseService;
import com.backend.allreva.member.command.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class MessageCommandController {

    public static final String SUB_PERSONAL_DESTINATION = "/personal/room";
    public static final String SUB_GROUP_DESTINATION = "/group/room/";

    private final MessageCommandService messageCommandService;
    private final MessageSseService messageSseService;

    private final ChatParticipantService chatParticipantService;

    private final SimpMessagingTemplate messagingTemplate;


    @MessageMapping("/single/connection/{singleChatId}")
    public void sendSingleMessage(
            @DestinationVariable final Long singleChatId,
            @AuthMember final Member member,
            final Content content
    ) {
        String destination = SUB_PERSONAL_DESTINATION + singleChatId;
        SingleMessage singleMessage = messageCommandService
                .saveSingleMessage(singleChatId, content, member);

        messagingTemplate.convertAndSend(destination, singleMessage);

        PreviewMessage previewMessage = new PreviewMessage(
                singleMessage.getMessageNumber(),
                content.getPayload(),
                singleMessage.getSentAt()
        );
        messageSseService.sendSummaryNotification(
                singleChatId,
                ChatType.SINGLE,
                previewMessage,
                member
        );
        chatParticipantService.updatePreviewMessage(
                member.getId(),
                singleChatId,
                ChatType.SINGLE,
                previewMessage
        );
    }


    @MessageMapping("/group/connection/{groupChatId}")
    public void sendGroupMessage(
            @DestinationVariable final Long groupChatId,
            @AuthMember final Member member,
            final Content content
    ) {
        String destination = SUB_GROUP_DESTINATION + groupChatId;
        GroupMessage groupMessage = messageCommandService
                .saveGroupMessage(groupChatId, content, member);

        messagingTemplate.convertAndSend(destination, groupMessage);

        PreviewMessage previewMessage = new PreviewMessage(
                groupMessage.getMessageNumber(),
                content.getPayload(),
                groupMessage.getSentAt()
        );
        messageSseService.sendSummaryNotification(
                groupChatId,
                ChatType.GROUP,
                previewMessage,
                member
        );
        chatParticipantService.updatePreviewMessage(
                member.getId(),
                groupChatId,
                ChatType.GROUP,
                previewMessage
        );
    }


}

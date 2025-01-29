package com.backend.allreva.chatting.chat.integration.application;

import com.backend.allreva.chatting.chat.integration.model.ChatParticipantDoc;
import com.backend.allreva.chatting.chat.integration.model.ChatParticipantRepository;
import com.backend.allreva.chatting.chat.integration.model.value.ChatInfoSummary;
import com.backend.allreva.chatting.chat.integration.model.value.ChatType;
import com.backend.allreva.chatting.chat.single.command.domain.SingleChatRepository;
import com.backend.allreva.chatting.chat.single.command.domain.value.OtherMember;
import com.backend.allreva.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ChatParticipantService {

    private final ChatParticipantRepository chatParticipantRepository;

    private final SingleChatRepository singleChatRepository;

    @Transactional
    public void updatePreviewMessage(
            final Long memberId,
            final Long roomId,
            final ChatType chatType,
            final long previewMessageNumber,
            final String previewText,
            final LocalDateTime sentAt
    ) {
        ChatParticipantDoc document = chatParticipantRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        if (document.existsInSummaries(roomId, chatType)) { // 채팅방 목록에 존재하면 previewMessage 만 업데이트
            document.updatePreviewMessage(
                    roomId,
                    chatType,
                    previewMessageNumber,
                    previewText,
                    sentAt
            );
            chatParticipantRepository.save(document);
            return;
        }

        if (chatType.equals(ChatType.SINGLE)) { // 채팅방 목록에 없는데 개인 채팅인 경우
            addSingleChatSummary(roomId, memberId, document);
        }
    }

    private void addSingleChatSummary(
            final Long roomId,
            final Long memberId,
            final ChatParticipantDoc document
    ) {
        OtherMember otherMember = singleChatRepository
                .findOtherMemberInfo(memberId, roomId);

        ChatInfoSummary chatInfoSummary = new ChatInfoSummary(
                otherMember.getNickname(),
                otherMember.getThumbnail(),
                2
        );
        document.addChatSummary(roomId, ChatType.SINGLE, chatInfoSummary);
        chatParticipantRepository.save(document);
    }
}

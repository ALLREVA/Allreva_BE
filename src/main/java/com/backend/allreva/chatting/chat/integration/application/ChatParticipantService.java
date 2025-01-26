package com.backend.allreva.chatting.chat.integration.application;

import com.backend.allreva.chatting.chat.group.command.domain.GroupChatRepository;
import com.backend.allreva.chatting.chat.group.query.GroupChatSummaryResponse;
import com.backend.allreva.chatting.chat.integration.model.ChatParticipantDoc;
import com.backend.allreva.chatting.chat.integration.model.ChatParticipantRepository;
import com.backend.allreva.chatting.chat.integration.model.value.ChatInfoSummary;
import com.backend.allreva.chatting.chat.integration.model.value.ChatType;
import com.backend.allreva.chatting.chat.single.command.domain.OtherMember;
import com.backend.allreva.chatting.chat.single.command.domain.SingleChatRepository;
import com.backend.allreva.common.model.Image;
import com.backend.allreva.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ChatParticipantService {

    private final ChatParticipantRepository chatParticipantRepository;

    private final SingleChatRepository singleChatRepository;
    private final GroupChatRepository groupChatRepository;

    public void updateGroupChatInfoSummary(
            final Long memberId,
            final Long groupChatId,
            final String title,
            final Image thumbnail
    ) {
        ChatParticipantDoc document = chatParticipantRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        if (document.existsInSummaries(groupChatId, ChatType.GROUP)) {
            document.updateChatInfoSummary(
                    groupChatId,
                    ChatType.GROUP,
                    title,
                    thumbnail
            );
            chatParticipantRepository.save(document);
        }
    }

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
            addSingleChatSummary(memberId, roomId, document);
        }
    }

    @Transactional
    public void addSingleChatSummary(
            final Long memberId,
            final Long roomId,
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

    @Transactional
    public void addSingleChatSummary(
            final Long memberId,
            final Long singleChatId
    ) {
        ChatParticipantDoc document = chatParticipantRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        OtherMember otherMember = singleChatRepository
                .findOtherMemberInfo(memberId, singleChatId);

        ChatInfoSummary chatInfoSummary = new ChatInfoSummary(
                otherMember.getNickname(),
                otherMember.getThumbnail(),
                2
        );
        document.addChatSummary(singleChatId, ChatType.SINGLE, chatInfoSummary);
        chatParticipantRepository.save(document);
    }

    @Transactional
    public void addGroupChatSummary(
            final Long memberId,
            final String uuid
    ) {
        ChatParticipantDoc document = chatParticipantRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        GroupChatSummaryResponse groupChatSummary = groupChatRepository
                .findGroupChatSummary(UUID.fromString(uuid));
        ChatInfoSummary chatInfoSummary = new ChatInfoSummary(
                groupChatSummary.title().getValue(),
                groupChatSummary.Thumbnail(),
                groupChatSummary.headcount()
        );
        document.addChatSummary(groupChatSummary.id(), ChatType.GROUP, chatInfoSummary);
    }

    @Transactional
    public void leaveChat(
            final Long memberId,
            final Long roomId,
            final ChatType chatType
    ) {
        ChatParticipantDoc document = chatParticipantRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        document.removeChatRoom(roomId, chatType);
        chatParticipantRepository.save(document);
    }
}

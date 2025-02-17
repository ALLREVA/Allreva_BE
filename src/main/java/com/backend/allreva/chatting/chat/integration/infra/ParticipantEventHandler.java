package com.backend.allreva.chatting.chat.integration.infra;

import com.backend.allreva.chatting.chat.group.command.domain.GroupChat;
import com.backend.allreva.chatting.chat.group.command.domain.GroupChatRepository;
import com.backend.allreva.chatting.chat.group.command.domain.MemberGroupChatRepository;
import com.backend.allreva.chatting.chat.group.command.domain.event.*;
import com.backend.allreva.chatting.chat.integration.model.ChatParticipantDoc;
import com.backend.allreva.chatting.chat.integration.model.ChatParticipantRepository;
import com.backend.allreva.chatting.chat.integration.model.EnteredChatEvent;
import com.backend.allreva.chatting.chat.integration.model.value.ChatInfoSummary;
import com.backend.allreva.chatting.chat.integration.model.value.ChatSummary;
import com.backend.allreva.chatting.chat.integration.model.value.ChatType;
import com.backend.allreva.chatting.chat.integration.model.value.PreviewMessage;
import com.backend.allreva.chatting.chat.single.command.domain.event.AddedSingleChatEvent;
import com.backend.allreva.chatting.chat.single.command.domain.event.LeavedSingleChatEvent;
import com.backend.allreva.chatting.chat.single.command.domain.value.OtherMember;
import com.backend.allreva.chatting.chat.single.command.domain.SingleChatRepository;
import com.backend.allreva.chatting.chat.single.command.domain.event.StartedSingleChatEvent;
import com.backend.allreva.chatting.message.domain.GroupMessageRepository;
import com.backend.allreva.chatting.message.domain.SingleMessageRepository;
import com.backend.allreva.chatting.notification.event.ConnectedEvent;
import com.backend.allreva.common.exception.NotFoundException;
import com.backend.allreva.member.command.domain.AddedMemberEvent;
import com.backend.allreva.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

@RequiredArgsConstructor
@Component
public class ParticipantEventHandler {

    private final GroupChatRepository groupChatRepository;
    private final SingleChatRepository singleChatRepository;

    private final GroupMessageRepository groupMessageRepository;
    private final SingleMessageRepository singleMessageRepository;

    private final MemberGroupChatRepository memberGroupChatRepository;

    private final ChatParticipantRepository participantRepository;


    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onMessage(final AddedMemberEvent event) {
        ChatParticipantDoc participantDoc = new ChatParticipantDoc(event.getMemberId());
        participantRepository.save(participantDoc);
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onMessage(final EnteredChatEvent event) {
        ChatParticipantDoc participantDoc = participantRepository.findById(event.getMemberId())
                        .orElseThrow(NotFoundException::new);
        participantDoc.updateLastReadMessageNumber(
                event.getChatId(),
                event.getChatType(),
                event.getLastReadMessageNumber()
        );
        participantRepository.save(participantDoc);
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onMessage(final ConnectedEvent event) {
        ChatParticipantDoc participant = participantRepository.findById(event.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        SortedSet<ChatSummary> chatSummaries = participant.getChatSummaries();
        List<ChatSummary> summariesCopy = new ArrayList<>(chatSummaries);

        summariesCopy.forEach(summary -> {
            Long chatId = summary.getChatId();
            PreviewMessage previewMessage = findPreviewMessage(
                    summary.getChatType(),
                    chatId
            );
            participant.updatePreviewMessage(
                    chatId,
                    summary.getChatType(),
                    previewMessage
            );
        });
        participantRepository.save(participant);
    }

    private PreviewMessage findPreviewMessage(
            final ChatType chatType,
            final Long chatId
    ) {
        if (chatType.equals(ChatType.SINGLE)) {
            return singleMessageRepository
                    .findPreviewMessageBySingleChatId(chatId);
        }
        return groupMessageRepository
                .findPreviewMessageByGroupChatId(chatId);
    }


    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onMessage(final AddedGroupChatEvent event) {
        addGroupChatSummary(event.getMemberId(), event.getGroupChatId());
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onMessage(final JoinedGroupChatEvent event) {
        addGroupChatSummary(event.getMemberId(), event.getGroupChatId());
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onMessage(final AddedSingleChatEvent event) {
        ChatParticipantDoc memberDocument
                = addSingleChatSummary(event.getSingleChatId(), event.getMemberId());

        ChatParticipantDoc otherMemberDocument
                = addSingleChatSummary(event.getSingleChatId(), event.getOtherMemberId());

        participantRepository.saveAll(
                Set.of(memberDocument, otherMemberDocument)
        );
    }
    

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onMessage(final UpdatedGroupChatEvent event) {

        Set<Long> memberIds = memberGroupChatRepository
                .findAllMemberIdByGroupChatId(event.getGroupChatId());

        Set<ChatParticipantDoc> participantDocs = participantRepository
                .findByMemberIdIn(memberIds);

        participantDocs.forEach(document -> document.updateChatInfoSummary(
                event.getGroupChatId(),
                ChatType.GROUP,
                event.getTitle(),
                event.getThumbnail()
        ));
        participantRepository.saveAll(participantDocs);
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onMessage(final LeavedGroupChatEvent event) {
        removeGroupChat(event.getMemberId(), event.getGroupChatId());
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onMessage(final LeavedSingleChatEvent event) {
        ChatParticipantDoc participantDoc = participantRepository.findChatParticipantDocByMemberId(event.getMemberId())
                .orElseThrow(NotFoundException::new);

        participantDoc.removeChatRoom(event.getSingleChatId(), ChatType.SINGLE);
        participantRepository.save(participantDoc);
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onMessage(final DeletedGroupChatEvent event) {
        removeGroupChat(event.getMemberId(), event.getGroupChatId());
    }


    // 채팅방 목록에 추가
    private void addGroupChatSummary(
            final Long memberId,
            final Long groupChatId
    ) {
        ChatParticipantDoc participantDoc = participantRepository.findChatParticipantDocByMemberId(memberId)
                .orElseThrow(NotFoundException::new);

        GroupChat groupChat = groupChatRepository.findById(groupChatId)
                .orElseThrow(NotFoundException::new);

        ChatInfoSummary chatInfoSummary = new ChatInfoSummary(
                groupChat.getTitle().getValue(),
                groupChat.getThumbnail(),
                groupChat.getHeadcount()
        );

        participantDoc.addChatSummary(
                groupChatId,
                ChatType.GROUP,
                chatInfoSummary
        );
        participantRepository.save(participantDoc);
    }

    private ChatParticipantDoc addSingleChatSummary(
            final Long singleChatId,
            final Long memberId
    ) {
        ChatParticipantDoc memberDocument = participantRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        OtherMember otherMember = singleChatRepository
                .findOtherMemberInfo(memberId, singleChatId);

        ChatInfoSummary memberInfoSummary = new ChatInfoSummary(
                otherMember.getNickname(),
                otherMember.getThumbnail(),
                2
        );

        memberDocument.addChatSummary(
                singleChatId,
                ChatType.SINGLE,
                memberInfoSummary
        );
        return memberDocument;
    }

    private void removeGroupChat(
            final Long memberId,
            final Long groupChatId
    ) {
        ChatParticipantDoc participantDoc = participantRepository.findChatParticipantDocByMemberId(memberId)
                .orElseThrow(NotFoundException::new);

        participantDoc.removeChatRoom(groupChatId, ChatType.GROUP);
        participantRepository.save(participantDoc);
    }
}

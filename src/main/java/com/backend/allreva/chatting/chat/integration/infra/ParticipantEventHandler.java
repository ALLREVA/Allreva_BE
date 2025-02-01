package com.backend.allreva.chatting.chat.integration.infra;

import com.backend.allreva.chatting.chat.group.command.domain.GroupChat;
import com.backend.allreva.chatting.chat.group.command.domain.GroupChatRepository;
import com.backend.allreva.chatting.chat.group.command.domain.MemberGroupChatRepository;
import com.backend.allreva.chatting.chat.group.command.domain.event.*;
import com.backend.allreva.chatting.chat.integration.model.ChatParticipantDoc;
import com.backend.allreva.chatting.chat.integration.model.ChatParticipantRepository;
import com.backend.allreva.chatting.chat.integration.model.value.ChatInfoSummary;
import com.backend.allreva.chatting.chat.integration.model.value.ChatType;
import com.backend.allreva.chatting.chat.single.command.domain.event.LeavedSingleChatEvent;
import com.backend.allreva.chatting.chat.single.command.domain.value.OtherMember;
import com.backend.allreva.chatting.chat.single.command.domain.SingleChatRepository;
import com.backend.allreva.chatting.chat.single.command.domain.event.StartedSingleChatEvent;
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

import java.util.Set;

@RequiredArgsConstructor
@Component
public class ParticipantEventHandler {

    private final GroupChatRepository groupChatRepository;
    private final SingleChatRepository singleChatRepository;

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
    public void onMessage(final StartedSingleChatEvent event) {
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
                .findMemberIdByGroupChatId(event.getGroupChatId());

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

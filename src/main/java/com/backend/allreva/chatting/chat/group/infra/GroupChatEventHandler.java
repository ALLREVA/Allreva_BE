package com.backend.allreva.chatting.chat.group.infra;

import com.backend.allreva.chatting.chat.group.command.domain.MemberGroupChat;
import com.backend.allreva.chatting.chat.group.command.domain.MemberGroupChatRepository;
import com.backend.allreva.chatting.chat.group.command.domain.event.AddedGroupChatEvent;
import com.backend.allreva.chatting.chat.group.command.domain.event.DeletedGroupChatEvent;
import com.backend.allreva.chatting.chat.group.command.domain.event.JoinedGroupChatEvent;
import com.backend.allreva.chatting.chat.group.command.domain.event.LeavedGroupChatEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Service
public class GroupChatEventHandler {

    private final MemberGroupChatRepository memberGroupChatRepository;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onMessage(final AddedGroupChatEvent event) {
        MemberGroupChat memberGroupChat = new MemberGroupChat(event.getMemberId(), event.getGroupChatId());
        memberGroupChatRepository.save(memberGroupChat);
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onMessage(final JoinedGroupChatEvent event) {
        MemberGroupChat memberGroupChat = new MemberGroupChat(event.getMemberId(), event.getGroupChatId());
        memberGroupChatRepository.save(memberGroupChat);
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onMessage(final LeavedGroupChatEvent event) {
        memberGroupChatRepository.deleteAllByGroupChatIdAndMemberId(event.getGroupChatId(), event.getMemberId());
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onMessage(final DeletedGroupChatEvent event) {
        memberGroupChatRepository.deleteAllByGroupChatIdAndMemberId(event.getGroupChatId(), event.getMemberId());
    }

}

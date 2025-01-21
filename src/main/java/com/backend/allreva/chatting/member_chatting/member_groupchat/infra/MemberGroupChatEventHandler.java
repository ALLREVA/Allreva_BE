package com.backend.allreva.chatting.member_chatting.member_groupchat.infra;

import com.backend.allreva.chatting.group_chat.command.domain.AddedGroupChatEvent;
import com.backend.allreva.chatting.group_chat.command.domain.DeletedGroupChatEvent;
import com.backend.allreva.chatting.member_chatting.member_groupchat.command.domain.MemberGroupChat;
import com.backend.allreva.chatting.member_chatting.member_groupchat.command.domain.MemberGroupChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberGroupChatEventHandler {

    private final MemberGroupChatRepository memberGroupChatRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onMessage(final AddedGroupChatEvent event) {
        MemberGroupChat memberGroupChat = new MemberGroupChat(event.getMemberId(), event.getGroupChatId());
        memberGroupChatRepository.save(memberGroupChat);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onMessage(final DeletedGroupChatEvent event) {
        memberGroupChatRepository.deleteAllByGroupChatIdAndMemberId(event.getGroupChatId(), event.getMemberId());
    }

}

package com.backend.allreva.chatting.chat.single.infra;

import com.backend.allreva.chatting.chat.single.command.domain.MemberSingleChat;
import com.backend.allreva.chatting.chat.single.command.domain.MemberSingleChatRepository;
import com.backend.allreva.chatting.chat.single.command.domain.event.StartedSingleChatEvent;
import com.backend.allreva.chatting.chat.single.command.domain.value.OtherMember;
import com.backend.allreva.member.query.application.MemberDetailRepository;
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
public class SingleChatEventHandler {

    private final MemberSingleChatRepository memberSingleChatRepository;
    private final MemberDetailRepository memberDetailRepository;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onMessage(final StartedSingleChatEvent event) {
        OtherMember member = memberDetailRepository.findMemberSummary(event.getMemberId());
        OtherMember otherMember = memberDetailRepository.findMemberSummary(event.getOtherMemberId());

        Set<MemberSingleChat> chats = MemberSingleChat.startFirstChat(
                event.getSingleChatId(),
                member,
                otherMember
        );
        memberSingleChatRepository.saveAll(chats);
    }

}

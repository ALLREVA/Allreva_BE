package com.backend.allreva.chatting.member_chatting.member_chat.command;

import com.backend.allreva.chatting.member_chatting.member_chat.command.domain.MemberChatRepository;
import com.backend.allreva.chatting.member_chatting.member_chat.command.domain.MemberChat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberChatCommandService {

    private final MemberChatRepository repository;

    /*@Transactional
    public Long joinChatRoom(
            final Long memberId,
            final Long chatId
    ) {
        MemberChat memberChat = new MemberChat(memberId, chatId);
        repository.save(memberChat);
        return chatId;
    }*/

    @Transactional
    public void leaveChatRoom(
            final Long memberId,
            final Long chatId
    ) {

    }
}

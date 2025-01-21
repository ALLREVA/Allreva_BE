package com.backend.allreva.chatting.member_chatting.member_chat;

import com.backend.allreva.chatting.member_chatting.member_chat.command.domain.MemberChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberChatQueryService {

    private MemberChatRepository memberChatRepository;


}

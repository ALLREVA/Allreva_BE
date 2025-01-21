package com.backend.allreva.chatting.message.query;

import com.backend.allreva.chatting.message.domain.GroupMessageRepository;
import com.backend.allreva.chatting.message.domain.SingleMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MessageQueryService {

    private final SingleMessageRepository singleMessageRepository;
    private final GroupMessageRepository groupMessageRepository;




}

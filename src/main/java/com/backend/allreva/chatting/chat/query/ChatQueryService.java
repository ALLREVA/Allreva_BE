package com.backend.allreva.chatting.chat.query;

import com.backend.allreva.chatting.chat.domain.ChatRepository;
import com.backend.allreva.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ChatQueryService {

    private final ChatRepository chatRepository;

    public Long findIdByUUID(final UUID uuid) {
        return chatRepository.findIdByUUID(uuid)
                .orElseThrow(NotFoundException::new);
    }
}

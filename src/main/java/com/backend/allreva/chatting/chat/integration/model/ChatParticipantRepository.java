package com.backend.allreva.chatting.chat.integration.model;

import com.backend.allreva.chatting.chat.integration.infra.ChatParticipantCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.Set;

public interface ChatParticipantRepository extends MongoRepository<ChatParticipantDoc, Long>, ChatParticipantCustomRepository {

    Optional<ChatParticipantDoc> findChatParticipantDocByMemberId(Long memberId);

    Set<ChatParticipantDoc> findByMemberIdIn(Set<Long> memberIds);
}

package com.backend.allreva.chatting.chat.integration.model;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.Set;

public interface ChatParticipantRepository extends MongoRepository<ChatParticipantDoc, Long> {

    Optional<ChatParticipantDoc> findChatParticipantDocByMemberId(Long memberId);

    Set<ChatParticipantDoc> findByMemberIdIn(Set<Long> memberIds);
}

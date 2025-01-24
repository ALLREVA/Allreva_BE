package com.backend.allreva.chatting.chat.integration.model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatParticipantRepository extends MongoRepository<ChatParticipantDoc, Long> {
}

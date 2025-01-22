package com.backend.allreva.chatting.chat.query.model.document;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberChatRoomRepository extends MongoRepository<MemberChatRoomDoc, Long> {
}

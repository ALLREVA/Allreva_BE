package com.backend.allreva.chatting.chat_room.model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberChatRoomRepository extends MongoRepository<MemberChatRoomDoc, Long> {
}

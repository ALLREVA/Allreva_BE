package com.backend.allreva.chatting.message.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupMessageRepository extends MongoRepository<GroupMessage, String> {
}

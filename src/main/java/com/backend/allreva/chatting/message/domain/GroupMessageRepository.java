package com.backend.allreva.chatting.message.domain;

import com.backend.allreva.chatting.message.infra.GroupMessageCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupMessageRepository extends MongoRepository<GroupMessage, String>, GroupMessageCustomRepository {
}

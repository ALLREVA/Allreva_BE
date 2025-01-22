package com.backend.allreva.chatting.message.domain;

import com.backend.allreva.chatting.message.infra.SingleMessageCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SingleMessageRepository extends MongoRepository<SingleMessage, String>, SingleMessageCustomRepository {



}

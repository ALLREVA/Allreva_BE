package com.backend.allreva.chatting.chat.single_chat.domain;

import com.backend.allreva.chatting.chat.infra.SingleChatDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SingleChatRepository extends JpaRepository<SingleChat, Long>, SingleChatDslRepository {

}

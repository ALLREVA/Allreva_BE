package com.backend.allreva.chatting.chat.single.command.domain;

import com.backend.allreva.chatting.chat.single.infra.SingleChatDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SingleChatRepository extends JpaRepository<SingleChat, Long>, SingleChatDslRepository {

}

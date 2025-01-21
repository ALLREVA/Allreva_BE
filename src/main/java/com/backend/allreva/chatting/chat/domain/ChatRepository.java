package com.backend.allreva.chatting.chat.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c.id FROM Chat c WHERE c.uuid = :uuid")
    Optional<Long> findIdByUUID(UUID uuid);
}

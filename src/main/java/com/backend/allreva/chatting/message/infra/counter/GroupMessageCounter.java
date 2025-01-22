package com.backend.allreva.chatting.message.infra.counter;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document
public class GroupMessageCounter {

    @Id
    private Long groupChatId;

    private long number;

    public GroupMessageCounter(
            final Long groupChatId,
            final long number
    ) {
        this.groupChatId = groupChatId;
        this.number = number;
    }
}

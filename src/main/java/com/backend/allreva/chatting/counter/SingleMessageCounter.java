package com.backend.allreva.chatting.counter;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document
public class SingleMessageCounter {

    @Id
    private Long chatId;

    private long number;

    public SingleMessageCounter(
            final Long chatId,
            final long number
    ) {
        this.chatId = chatId;
        this.number = number;
    }
}

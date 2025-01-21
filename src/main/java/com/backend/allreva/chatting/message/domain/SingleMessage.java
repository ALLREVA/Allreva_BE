package com.backend.allreva.chatting.message.domain;

import com.backend.allreva.chatting.message.domain.value.Content;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document
public class SingleMessage {

    @Id
    @Field(value = "_id", targetType = FieldType.OBJECT_ID)
    private String id;

    private Long chatId;
    private long messageNumber;

    private Content content;
    private LocalDateTime sentAt;

    public SingleMessage(
            final Long chatId,
            final long messageNumber,
            final Content content
    ) {
        this.chatId = chatId;
        this.messageNumber = messageNumber;
        this.content = content;
        this.sentAt = LocalDateTime.now();
    }
}

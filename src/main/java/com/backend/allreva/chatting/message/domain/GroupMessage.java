package com.backend.allreva.chatting.message.domain;

import com.backend.allreva.chatting.Participant;
import com.backend.allreva.chatting.message.domain.value.Content;
import com.backend.allreva.common.model.Image;
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
public class GroupMessage {

    @Id
    @Field(value = "_id", targetType = FieldType.OBJECT_ID)
    private String id;

    private Long groupChatId;
    private long messageNumber;

    private Content content;

    private Participant sender;
    private LocalDateTime sentAt;

    public GroupMessage(
            final Long groupChatId,
            final long messageNumber,
            final Content content,
            final Long memberId,
            final String nickname,
            final String profileImageUrl
    ) {
        this.groupChatId = groupChatId;
        this.messageNumber = messageNumber;
        this.content = content;

        this.sender = new Participant(
                memberId,
                nickname,
                new Image(profileImageUrl)
        );
        this.sentAt = LocalDateTime.now();
    }
}

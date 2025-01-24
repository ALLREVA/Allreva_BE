package com.backend.allreva.chatting.chat.group.command.domain;

import com.backend.allreva.chatting.exception.CannotDeleteGroupChatException;
import com.backend.allreva.chatting.exception.InvalidWriterException;
import com.backend.allreva.chatting.chat.group.command.domain.value.Description;
import com.backend.allreva.chatting.chat.group.command.domain.value.Title;
import com.backend.allreva.common.model.Image;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(columnList = "manager_id"))
@Entity
public class GroupChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID uuid;

    private Long managerId;

    @Embedded
    private Title title;
    @Embedded
    private Description description;
    @Embedded
    private Image thumbnail;

    private int headcount;
    private int capacity;


    @Builder
    public GroupChat(
            final Long managerId,
            final String title,
            final String description,
            final Image thumbnail,
            final int capacity
    ) {
        this.uuid = UUID.randomUUID();
        this.managerId = managerId;
        this.title = new Title(title);
        this.description = new Description(description);
        this.thumbnail = thumbnail;
        this.headcount = 1;
        this.capacity = capacity;
    }

    public void updateInfo(
            final Long managerId,
            final String title,
            final String description,
            final Image thumbnail
    ) {
        this.managerId = managerId;
        this.title = new Title(title);
        this.description = new Description(description);
        this.thumbnail = thumbnail;
    }

    public void validateForDelete(final Long memberId) {
        validateManager(memberId);
        if (this.headcount != 1) {
            throw new CannotDeleteGroupChatException();
        }
    }

    public void validateManager(final Long memberId) {
        if (this.managerId.equals(memberId)) {
            return;
        }
            throw new InvalidWriterException();
    }


}

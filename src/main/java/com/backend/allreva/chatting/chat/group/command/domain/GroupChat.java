package com.backend.allreva.chatting.chat.group.command.domain;

import com.backend.allreva.chatting.chat.group.command.domain.event.DeletedGroupChatEvent;
import com.backend.allreva.chatting.chat.group.command.domain.event.JoinedGroupChatEvent;
import com.backend.allreva.chatting.chat.group.command.domain.event.LeavedGroupChatEvent;
import com.backend.allreva.chatting.chat.group.command.domain.event.UpdatedGroupChatEvent;
import com.backend.allreva.chatting.chat.group.command.domain.value.Description;
import com.backend.allreva.chatting.chat.group.command.domain.value.Title;
import com.backend.allreva.chatting.exception.CannotDeleteGroupChatException;
import com.backend.allreva.chatting.exception.InvalidWriterException;
import com.backend.allreva.common.event.Events;
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
        validateManager(managerId);
        this.managerId = managerId;
        this.title = new Title(title);
        this.description = new Description(description);
        this.thumbnail = thumbnail;

        UpdatedGroupChatEvent updatedEvent = new UpdatedGroupChatEvent(
                managerId,
                this.id,
                title,
                thumbnail
        );
        Events.raise(updatedEvent);
    }

    public void validateForDelete(final Long memberId) {
        validateManager(memberId);
        if (this.headcount != 1) {
            throw new CannotDeleteGroupChatException();
        }

        DeletedGroupChatEvent deletedEvent
                = new DeletedGroupChatEvent(this.id, memberId);
        Events.raise(deletedEvent);
    }

    public void validateManager(final Long memberId) {
        if (this.managerId.equals(memberId)) {
            return;
        }
            throw new InvalidWriterException();
    }

    public void addHeadcount(final Long memberId) {
        this.headcount++;

        JoinedGroupChatEvent joinedEvent
                = new JoinedGroupChatEvent(memberId, this.id);
        Events.raise(joinedEvent);
    }

    public void subtractHeadcount(final Long memberId) {
        this.headcount--;

        LeavedGroupChatEvent leavedEvent
                = new LeavedGroupChatEvent(memberId, this.id);
        Events.raise(leavedEvent);
    }


}

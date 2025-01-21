package com.backend.allreva.chatting.chat_room.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class RoomKey implements Comparable<RoomKey> {

    private final Long roomId;
    private final RoomType roomType;

    public RoomKey(
            final Long roomId,
            final RoomType roomType
    ) {
        this.roomId = roomId;
        this.roomType = roomType;
    }

    @Override
    public int compareTo(final RoomKey other) {
        int idCompare = this.roomId.compareTo(other.roomId);
        if (idCompare != 0) {
            return idCompare;
        }
        return this.roomType.compareTo(other.roomType);
    }
}

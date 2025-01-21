package com.backend.allreva.chatting.message.domain.value;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content {

    private Type type;
    private String payload;

    public Content(
            final Type type,
            final String payload
    ) {
        this.type = type;
        this.payload = payload;
    }
}

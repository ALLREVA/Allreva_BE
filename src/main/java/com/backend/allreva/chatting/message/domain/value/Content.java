package com.backend.allreva.chatting.message.domain.value;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content {

    private ContentType contentType;
    private String payload;

    public Content(
            final ContentType contentType,
            final String payload
    ) {
        this.contentType = contentType;
        this.payload = payload;
    }
}

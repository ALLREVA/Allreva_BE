package com.backend.allreva.chatting.message.domain.value;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Content {

    private ContentType contentType;
    private String payload;

}

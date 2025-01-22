package com.backend.allreva.chatting.chat.group_chat.command.domain.value;

import com.backend.allreva.chatting.exception.TooLongTitleException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Title {

    @Column(name = "title")
    private String value;

    public Title(final String value) {
        if (value.length() > 20) {
            throw new TooLongTitleException();
        }
        this.value = value;
    }
}

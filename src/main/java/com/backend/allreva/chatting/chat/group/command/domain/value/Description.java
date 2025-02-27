package com.backend.allreva.chatting.chat.group.command.domain.value;

import com.backend.allreva.chatting.exception.TooLongDescriptionException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Description {

    @Column(name = "description")
    private String value;

    public Description(String value) {
        if (value.length() > 50) {
            throw new TooLongDescriptionException();
        }
        this.value = value;
    }
}

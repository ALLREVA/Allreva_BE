package com.backend.allreva.common.model;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Embeddable
public class Image {
    private String url;

    public Image(final String url) {
        this.url = url;
    }
}

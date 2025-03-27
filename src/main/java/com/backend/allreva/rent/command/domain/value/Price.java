package com.backend.allreva.rent.command.domain.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Price {

    @Column(nullable = false)
    private int roundPrice;

    @Column(nullable = false)
    private int upTimePrice;

    @Column(nullable = false)
    private int downTimePrice;
}

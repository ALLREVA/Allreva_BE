package com.backend.allreva.rent.command.domain;

import com.backend.allreva.common.event.Event;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RentClosedEvent extends Event {

    private Long rentId;

    public RentClosedEvent(Long rentId) {
        this.rentId = rentId;
    }
}

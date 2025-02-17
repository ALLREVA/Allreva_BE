package com.backend.allreva.rent.fixture;

import com.backend.allreva.common.model.Image;
import com.backend.allreva.rent.command.application.request.RentRegisterRequest;
import com.backend.allreva.rent.command.domain.value.BusSize;
import com.backend.allreva.rent.command.domain.value.BusType;
import com.backend.allreva.rent_join.command.domain.value.RefundType;
import com.backend.allreva.rent.command.domain.value.Region;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RentRegisterRequestFixture {

    public static RentRegisterRequest createRentRegisterRequestFixture() {
        return new RentRegisterRequest(
                1L,
                "title",
                "artistName",
                Region.서울,
                "depositAccount",
                "boardingArea",
                "09:00",
                "23:00",
                List.of(
                        LocalDate.of(2024, 9, 20),
                        LocalDate.of(2024, 9, 21),
                        LocalDate.of(2024, 9, 22)
                ),
                BusSize.LARGE,
                BusType.STANDARD,
                28,
                30000,
                30000,
                30000,
                30,
                LocalDate.of(2024, 9, 13),
                "charUrl",
                RefundType.BOTH,
                "information",
                new Image("imageUrl")
        );
    }
}

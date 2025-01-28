package com.backend.allreva.hall.command.domain;


import com.backend.allreva.hall.command.domain.value.ConvenienceInfo;
import com.backend.allreva.hall.command.domain.value.Location;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ConcertHall {

    @Id
    private String id;

    @Column(nullable = false)
    private String name; // fcltyName + prfplcName
    private int seatScale;
    private double star;
    private long totalStars;
    private long reviewCount;

    @Embedded
    private ConvenienceInfo convenienceInfo;
    @Embedded
    private Location location;


    @Builder
    private ConcertHall(
            final String id,
            final String name,
            final int seatScale,
            final ConvenienceInfo convenienceInfo,
            final Location location
    ) {
        this.id = id;
        this.name = name;
        this.seatScale = seatScale;
        this.star = 0.0;
        this.totalStars = 0;
        this.reviewCount = 0;
        this.convenienceInfo = convenienceInfo;
        this.location = location;
    }

    public void updateStar(int starDelta, int countDelta) {
        this.totalStars += starDelta;
        this.reviewCount += countDelta;
        this.star = (double) totalStars / reviewCount;
    }
}

package com.backend.allreva.rent.command.domain;

import com.backend.allreva.common.event.Events;
import com.backend.allreva.common.model.BaseEntity;
import com.backend.allreva.rent_join.exception.PassengersMaximumReachedException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is NULL")
@SQLDelete(sql = "UPDATE rent_boarding_info SET deleted_at = NOW() WHERE id = ?")
@Entity
@Table(name = "rent_boarding_info", indexes = {
        @Index(name = "idx_rent_boarding_info_rent_date", columnList = "rent_id, date")
})
public class RentBoardingInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Rent rent;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private int recruitmentCount; // 모집 인원

    @Column(nullable = false)
    private int passengerCount; // 신청 인원

    @Builder
    private RentBoardingInfo(
            Rent rent,
            LocalDate date,
            int recruitmentCount
    ) {
        this.rent = rent;
        this.date = date;
        this.recruitmentCount = recruitmentCount;
        this.passengerCount = 0;
    }

    protected void assignRent(Rent rent) {
        this.rent = rent;
    }

    public void addPassengerCount(int passengerCount) {
        checkPassengersMaximumReached(passengerCount);
        this.passengerCount += passengerCount;

        checkIfRecruitmentCompleted();
    }

    private void checkPassengersMaximumReached(int passengerCount) {
        if (this.passengerCount + passengerCount > recruitmentCount) {
            throw new PassengersMaximumReachedException();
        }
    }

    private void checkIfRecruitmentCompleted() {
        if (passengerCount == recruitmentCount) {
            // Recruitment is complete, raise domain event
            Events.raise(new RentClosedEvent(this.id));
        }
    }
}


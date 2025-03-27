package com.backend.allreva.rent_join.command.domain;

import com.backend.allreva.common.model.BaseEntity;
import com.backend.allreva.rent_join.command.application.request.RentJoinUpdateRequest;
import com.backend.allreva.rent_join.command.domain.value.BoardingType;
import com.backend.allreva.rent_join.command.domain.value.Depositor;
import com.backend.allreva.rent_join.command.domain.value.RefundType;
import com.backend.allreva.rent_join.exception.RentJoinAccessDeniedException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is NULL")
@SQLDelete(sql = "UPDATE rent_join SET deleted_at = NOW() WHERE id = ?")
@Entity
@Table(name = "rent_join", indexes = {
        @Index(name = "idx_rent_join_member_id", columnList = "member_id")
})
public class RentJoin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false, name = "rent_form_id")
    private Long rentId;

    @Embedded
    private Depositor depositor;

    @Column(nullable = false)
    private int passengerNum;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardingType boardingType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefundType refundType;

    @Column(nullable = false)
    private String refundAccount;

    @Column(nullable = false)
    private LocalDate boardingDate; //이용날짜

    public void updateRentJoin(final RentJoinUpdateRequest request) {
        this.depositor = Depositor.builder()
                .depositorName(request.depositorName())
                .depositorTime(request.depositorTime())
                .phone(request.phone())
                .build();
        this.passengerNum = request.passengerNum();
        this.boardingType = request.boardingType();
        this.refundType = request.refundType();
        this.refundAccount = request.refundAccount();
        this.boardingDate = request.boardingDate();
    }

    public void validateMine(Long memberId) {
        if (!this.memberId.equals(memberId)) {
            throw new RentJoinAccessDeniedException();
        }
    }
}

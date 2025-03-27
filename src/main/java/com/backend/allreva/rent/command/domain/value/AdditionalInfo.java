package com.backend.allreva.rent.command.domain.value;

import com.backend.allreva.rent_join.command.domain.value.RefundType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class AdditionalInfo {

    @Column(nullable = false, name = "eddate")
    private LocalDate endDate; // 모집마감날짜

    @Column(nullable = false)
    private String chatUrl; // 채팅방 날짜

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefundType refundType; // 환불 정책

    private String information; // 안내 사항
}

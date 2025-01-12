package com.backend.allreva.seat_review.command.domain;

import com.backend.allreva.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.validator.constraints.Range;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE seat_review SET deleted_at = NOW() WHERE id = ?")
public class SeatReview extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String seat;

    @Column(nullable = true)
    private String content;

    @Column(nullable = false)
    @Range(min = 0, max = 5)
    private int star;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String hallId;
}

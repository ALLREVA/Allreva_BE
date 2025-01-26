package com.backend.allreva.seat_review.command.domain;

import com.backend.allreva.common.model.BaseEntity;
import com.backend.allreva.seat_review.command.application.dto.ReviewUpdateRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
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

    @Column(nullable = false)
    private LocalDate viewDate;

    public void updateSeatReview(ReviewUpdateRequest request) {
        this.seat = request.seat();
        this.content = request.content();
        this.star = request.star();
        this.viewDate = LocalDate.now();
        this.hallId = request.hallId();
    }
}

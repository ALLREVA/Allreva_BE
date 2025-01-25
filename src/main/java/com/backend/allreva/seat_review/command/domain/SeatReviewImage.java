package com.backend.allreva.seat_review.command.domain;

import com.backend.allreva.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
@Getter
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE seat_review_image SET deleted_at = NOW() WHERE id = ?")
public class SeatReviewImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private Long seatReviewId;

    @Column(nullable = false)
    private Integer orderNum;
}

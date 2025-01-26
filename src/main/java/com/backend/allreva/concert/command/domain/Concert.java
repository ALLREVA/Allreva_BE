package com.backend.allreva.concert.command.domain;

import com.backend.allreva.common.event.Events;
import com.backend.allreva.common.model.BaseEntity;
import com.backend.allreva.common.model.Image;
import com.backend.allreva.concert.command.domain.value.Code;
import com.backend.allreva.concert.command.domain.value.ConcertInfo;
import com.backend.allreva.concert.command.domain.value.Seller;
import com.backend.allreva.concert.infra.dto.KopisConcertResponse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "idx_hall_code", columnList = "hall_code"))
@Entity
public class Concert extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long viewCount;

    @Embedded
    private Code code;

    @Embedded
    private ConcertInfo concertInfo;

    @ElementCollection
    @CollectionTable(
            name = "concert_episode",
            joinColumns = @JoinColumn(name = "id")
    )
    @Column(name = "episode")
    private List<String> episodes;


    @Embedded
    @AttributeOverride(name = "url", column = @Column(name = "poster"))
    private Image poster;

    @ElementCollection
    @CollectionTable(
            name = "concert_image",
            joinColumns = @JoinColumn(name = "id")
    )
    private List<Image> detailImages;


    @ElementCollection
    @CollectionTable(
            name = "concert_seller",
            joinColumns = @JoinColumn(name = "id")
    )
    private Set<Seller> sellers;


    public void updateFrom(
            final String hallCode,
            final KopisConcertResponse.Db db
    ) {
        this.concertInfo = KopisConcertResponse.toConcertInfo(db);
        this.detailImages = KopisConcertResponse.toDetailImages(db.getStyurls().getStyurl());
        this.sellers = KopisConcertResponse.toSellers(db.getRelates().getRelate());
        this.poster = KopisConcertResponse.toIntroduceImage(db.getPoster());
        this.code = Code.builder()
                .concertCode(db.getConcertCode())
                .hallCode(hallCode)
                .build();
        this.episodes = KopisConcertResponse.toEpisodes(db.getDtguidance());
    }


    @Builder
    private Concert(
            final Code code,
            final ConcertInfo concertInfo,
            final List<String> episodes,
            final Image poster,
            final List<Image> detailImages,
            final Set<Seller> sellers
    ) {
        this.code = code;
        this.concertInfo = concertInfo;
        this.episodes = episodes;
        this.poster = poster;
        this.detailImages = detailImages;
        this.sellers = sellers;
        this.viewCount = 0L;
    }

    public void addViewCount(final int count) {
        this.viewCount += count;
        Events.raise(new ViewAddedEvent(this));
    }
}

package com.backend.allreva.concert.query.application.response;


import com.backend.allreva.common.model.Image;
import com.backend.allreva.concert.command.domain.value.ConcertInfo;
import com.backend.allreva.concert.command.domain.value.Seller;
import com.backend.allreva.hall.command.domain.value.ConvenienceInfo;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;
import java.util.Set;

@Builder(access = AccessLevel.PRIVATE)
public record ConcertDetailResponse(

        Image poster,
        List<Image> detailImages,
        ConcertInfo concertInfo,
        Set<Seller> sellers,

        String hallCode,
        String hallName,
        Integer seatScale,
        ConvenienceInfo convenienceInfo,
        String address

) {
    public static final ConcertDetailResponse EMPTY = ConcertDetailResponse.builder().build();
}

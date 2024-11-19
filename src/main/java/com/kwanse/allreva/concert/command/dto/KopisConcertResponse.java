package com.kwanse.allreva.concert.command.dto;

import com.kwanse.allreva.common.converter.DataConverter;
import com.kwanse.allreva.concert.command.domain.Concert;
import com.kwanse.allreva.concert.command.domain.value.ConcertInfo;
import com.kwanse.allreva.concert.command.domain.value.ConcertStatus;
import com.kwanse.allreva.concert.command.domain.value.IntroduceImage;
import com.kwanse.allreva.concert.command.domain.value.Seller;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KopisConcertResponse {
    private String concertcd; //공연 code
    private String prfnm; //공연명
    private String prfpdfrom; //시작 날짜
    private String prfpdto; //종료 날짜
    private String hallId; //공연장소 kopis code
    private String poster; //포스터
    private String pcseguidance; //가격
    private String prfstate; //공연상태
    private List<String> styurls; //소개이미지 list
    private List<Relate> relates; //판매처 list

    public static Concert toEntity(final KopisConcertResponse response) {
        return Concert.builder()
                .concertInfo(toConcertInfo(response))
                .poster(toIntroduceImage(response.poster))
                .detailImages(toDetailImages(response.styurls))
                .sellers(toSellers(response.relates))
                .concertcd(response.concertcd)
                .hallId(response.hallId)
                .build();
    }

    public static ConcertInfo toConcertInfo(final KopisConcertResponse response) {
        return ConcertInfo.builder()
                .title(response.prfnm)
                .price(response.pcseguidance)
                .stdate(DataConverter.convertToLocalDate(response.prfpdfrom))
                .eddate(DataConverter.convertToLocalDate(response.prfpdto))
                .prfstate(ConcertStatus.convertToConcertStatus(response.prfstate))
                .build();
    }

    public static Seller toSeller(final Relate relate) {
        return Seller.builder()
                .relateName(relate.getRelatenm())
                .relateUrl(relate.getRelateurl())
                .build();
    }

    public static List<Seller> toSellers(final List<Relate> relates) {
        return relates.stream().map(KopisConcertResponse::toSeller).toList();
    }
    public static IntroduceImage toIntroduceImage(final String image) {
        return new IntroduceImage(image);
    }

    public static List<IntroduceImage> toDetailImages(final List<String> styurls) {
        return styurls.stream().map(KopisConcertResponse::toIntroduceImage).toList();
    }

}

package com.fourroro.nolleogasil_backend.dto.travelpath;


import com.fourroro.nolleogasil_backend.entity.travelpath.TravelDate;
import lombok.*;
/**
 * 추천된 여행 일정의 일자와 연관된 ID들을 담을 수 있는 DTO클래스입니다.
 * @author 전선민
 * @since 2024-01-10
 */
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TravelDateDto {

    private Long traveldateId;      //PK
    private String dates;           //여행 일자
    private Long recommendationId;  //FK (recommendationId)

    public static TravelDateDto changeToDto(TravelDate entity) {
        return TravelDateDto.builder()
                .traveldateId(entity.getTraveldateId())
                .dates(entity.getDates())
                .recommendationId(entity.getRecommendation().getRecommendationId())
                .build();
    }
}

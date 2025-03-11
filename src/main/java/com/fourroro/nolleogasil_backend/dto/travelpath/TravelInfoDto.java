package com.fourroro.nolleogasil_backend.dto.travelpath;

import com.fourroro.nolleogasil_backend.entity.travelpath.TravelInfo;
import lombok.*;
/**
 * 추천된 여행 일정의 내용과 연관된 ID들을 담을 수 있는 DTO클래스입니다.
 * @author 전선민
 * @since 2024-01-10
 */
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TravelInfoDto {
    private Long travelinfoId;      //PK
    private String infos;           //여행 내용
    private Long recommendationId;  //FK (recommendationId)

    public static TravelInfoDto changeToDto(TravelInfo entity) {
        return TravelInfoDto.builder()
                .travelinfoId(entity.getTravelinfoId())
                .infos(entity.getInfos())
                .recommendationId(entity.getRecommendation().getRecommendationId())
                .build();
    }
}

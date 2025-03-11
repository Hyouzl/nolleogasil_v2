package com.fourroro.nolleogasil_backend.dto.travelpath;

import com.fourroro.nolleogasil_backend.entity.travelpath.Recommendation;
import lombok.*;
/**
 * 추천 여행일정 ID에 매칭되는 DTO클래스입니다.
 * @author 전선민
 * @since 2024-01-10
 */
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationDto {
    private Long recommendationId;  //PK
    private Long travelpathId;      //FK (travelpathId)


    public static RecommendationDto changeToDto(Recommendation entity) {
        return RecommendationDto.builder()
                .recommendationId(entity.getRecommendationId())
                .travelpathId(entity.getTravelPath().getTravelpathId())
                .build();
    }

}

package com.fourroro.nolleogasil_backend.dto.travelpath;

import com.fourroro.nolleogasil_backend.entity.travelpath.Keyword;
import lombok.*;

/**
 * 여행 일정 폼 내의 질문항목들 중 다중 선택 항목들을 담을 수 있는 DTO클래스입니다.
 * @author 전선민
 * @since 2024-01-10
 */
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class KeywordDto {
    private Long keywordId;            //PK
    private String party;              //일행
    private String place;              //장소
    private String concept;            //컨셉
    private String food;               //음식
    private Long travelpathId;         //FK (travelpathId)

    public static KeywordDto changeToDto(Keyword entity) {
        return KeywordDto.builder()
                .keywordId(entity.getKeywordId())
                .party(entity.getParty())
                .place(entity.getPlace())
                .concept(entity.getConcept())
                .food(entity.getFood())
                .travelpathId(entity.getTravelPath().getTravelpathId())
                .build();
    }
}

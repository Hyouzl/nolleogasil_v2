package com.fourroro.nolleogasil_backend.dto.travelpath;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 여행 일정 폼 내의 질문항목 정보들을 담을 수 있는 DTO클래스입니다.
 * @author 전선민
 * @since 2024-01-10
 */

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ConditionDto implements Serializable {

    private String destination;         //목적지
    private String startDate;           //출발 날짜
    private String endDate;             //오는 날짜
    private List<String> partyItems;    //일행
    private List<String> placeItems;    //장소
    private List<String> conceptItems;  //컨셉
    private List<String> foodItems;     //음식
    private boolean pet;                //반려동물

}

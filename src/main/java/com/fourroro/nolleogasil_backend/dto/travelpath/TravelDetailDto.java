package com.fourroro.nolleogasil_backend.dto.travelpath;

import lombok.*;

import java.io.Serializable;
/**
 * 추천된 여행 일정의 모든 항목을 담을 수 있는 DTO클래스입니다.
 * @author 전선민
 * @since 2024-01-10
 */

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TravelDetailDto implements Serializable {
    private String destination;                 //목적지
    private String startDate;                   //출발 날짜
    private String endDate;		                //오는 날짜
    private String partyItems;                  //일행
    private String placeItems;                  //장소
    private String conceptItems;                //컨셉
    private String foodItems;                   //음식
    private ResultDto resultDto;                //여행 일자 + 정보

    public boolean checkNullField() {
        return destination == null || startDate == null || endDate == null ||
                partyItems == null || placeItems == null || conceptItems == null || foodItems == null ||
                resultDto.getDates() == null || resultDto.getInfos() == null;
    }
}

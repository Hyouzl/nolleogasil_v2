package com.fourroro.nolleogasil_backend.dto.travelpath;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 추천된 여행 일정의 일자와 내용을 담을 수 있는 클래스입니다.
 * @author 전선민
 * @since 2024-01-10
 */
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResultDto implements Serializable {
    private List<String> dates;     //여행 일자
    private List<String> infos;     //여행 내용



}

/**
 * 맛집메이트의 메이트 공고글 작성 폼을 위한 Dto 객체를 구성, 관리하는 클래스입니다.
 * @author 홍유리
 * @since 2024-01-05
 */
package com.fourroro.nolleogasil_backend.dto.mate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@JsonFormat
public class MateFormDto {

    private String title;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate eatDate;
    private String eatTime;
    private String gender;
    private int count;
    private String comments;

}

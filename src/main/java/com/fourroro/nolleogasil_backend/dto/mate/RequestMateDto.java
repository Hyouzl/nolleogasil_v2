/**
 * 맛집메이트의 메이트 공고글 폼과 장소를 위한 Dto 객체를 구성, 관리하는 클래스입니다.
 * @author 홍유리
 * @since 2024-01-05
 */
package com.fourroro.nolleogasil_backend.dto.mate;


import com.fourroro.nolleogasil_backend.dto.place.PlaceDto;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RequestMateDto {

    private MateFormDto mateFormDto;
    private PlaceDto placeDto;


}

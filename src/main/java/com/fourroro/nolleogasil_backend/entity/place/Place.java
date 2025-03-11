/**
 * Place Table에 매칭되는 Entity입니다.
 * @author 박초은
 * @since 2024-01-05
 */
package com.fourroro.nolleogasil_backend.entity.place;

import com.fourroro.nolleogasil_backend.dto.place.PlaceDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Place")
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place {

    @Id
    private Long placeId;            //장소 id

    private String placeName;           //장소 이름
    private String placeAddress;        //주소
    private String placeRoadAddress;    //도로명 주소
    private String placePhone;          //전화번호
    private String placeUrl;            //장소 url
    private double placeLat;            //위도
    private double placeLng;            //경도
    private int placeCat;               //장소 카테고리(맛집(1), 카페(2), 숙소(3), 관광지(4) 中 1)

    //dto -> entity
    public static Place changeToEntity(PlaceDto dto) {
        return Place.builder()
                .placeId(dto.getPlaceId())
                .placeName(dto.getPlaceName())
                .placeAddress(dto.getPlaceAddress())
                .placeRoadAddress(dto.getPlaceRoadAddress())
                .placePhone(dto.getPlacePhone())
                .placeUrl(dto.getPlaceUrl())
                .placeLat(dto.getPlaceLat())
                .placeLng(dto.getPlaceLng())
                .placeCat(dto.getPlaceCat())
                .build();
    }

}

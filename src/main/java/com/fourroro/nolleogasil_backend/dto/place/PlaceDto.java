/**
 * 장소 정보를 담을 수 있는 Dto클래스입니다.
 * 위시리스트(내 장소)에 저장되거나, 맛집메이트가 개설되는 장소를 저장하기 위해 존재합니다.
 * @author 박초은
 * @since 2024-01-05
 */
package com.fourroro.nolleogasil_backend.dto.place;

import com.fourroro.nolleogasil_backend.entity.place.Place;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDto {

    private Long placeId;            //PK //장소 id
    private String placeName;           //장소 이름
    private String placeAddress;        //주소
    private String placeRoadAddress;    //도로명 주소
    private String placePhone;          //장소 전화번호
    private String placeUrl;            //장소 url
    private double placeLat;            //위도
    private double placeLng;            //경도
    private int placeCat;               //맛집(1), 카페(2), 숙소(3), 관광지(4) 中 1
    private double distance;               //현재위치에서부터의 거리

    public void setPlaceCat(int placeCat) {
        this.placeCat = placeCat;
    }

    public void setDistance(double distance) { this.distance = distance; }

    public static PlaceDto changeToDto(Place entity) {
        return PlaceDto.builder()
                .placeId(entity.getPlaceId())
                .placeName(entity.getPlaceName())
                .placeAddress(entity.getPlaceAddress())
                .placeRoadAddress(entity.getPlaceRoadAddress())
                .placePhone(entity.getPlacePhone())
                .placeUrl(entity.getPlaceUrl())
                .placeLat(entity.getPlaceLat())
                .placeLng(entity.getPlaceLng())
                .placeCat(entity.getPlaceCat())
                .build();
    }

}

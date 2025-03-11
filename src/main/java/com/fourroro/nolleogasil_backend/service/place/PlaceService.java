/**
 * 장소 관리를 위한 Service interface입니다.
 * @author 박초은
 * @since 2024-01-10
 */
package com.fourroro.nolleogasil_backend.service.place;

import com.fourroro.nolleogasil_backend.dto.place.PlaceDto;
import com.fourroro.nolleogasil_backend.entity.place.Place;

public interface PlaceService {

    /** insert place */
    public Place insertPlace(PlaceDto placeDto);

    /** place 유무 확인 */
    public boolean checkPlaceColumn(Long placeId);

    /** place category 변경(string -> int) */
    public int changeToPlaceCat(String category);

    /** 두 지점 간 거리 계산(km) */
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2);

}

/**
 * 장소 관리를 위한 Service클래스입니다.
 * @author 박초은
 * @since 2024-01-10
 */
package com.fourroro.nolleogasil_backend.service.place;

import com.fourroro.nolleogasil_backend.dto.place.PlaceDto;
import com.fourroro.nolleogasil_backend.entity.place.Place;
import com.fourroro.nolleogasil_backend.repository.place.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;

    /** insert place */
    @Override
    @Transactional
    public Place insertPlace(PlaceDto placeDto) {
        Place place = Place.changeToEntity(placeDto);
        Place savedPlace = placeRepository.save(place);

        return savedPlace;
    }

    /** place 유무 확인 */
    @Override
    @Transactional
    public boolean checkPlaceColumn(Long placeId) {
        return placeRepository.existsById(placeId);
    }

    /** place category 변경(string -> int) */
    @Override
    public int changeToPlaceCat(String category) {
        int placeCat;
        switch (category) {
            case "FD6":
                placeCat = 1; //식당
                break;
            case "CE7":
                placeCat = 2; //카페
                break;
            case "AD5":
                placeCat = 3; //숙소
                break;
            case "AT4":
                placeCat = 4; //관광지
                break;
            default:
                placeCat = 0;
                break;
        }

        return placeCat;
    }

    /* Harversine 공식
        => 지구 곡률을 고려해 위도와 경도를 기반으로 두 지점 간의 대원거리를 계산하는 공식
    1. 두 지점의 위도(lat1, lat2)와 경도(lon1, lon2)의 차를 각각 라디안으로 변환(각도를 삼각함수에 적용하기 위해 표준화된 단위로 변환)
    2. 위도와 경도의 차이를 각각 계산해 Δlat(델타 라트)와 Δlon(델타 론)를 구함
    3. 변수 a 계산(두 지점 간의 곡선 삼각형의 넓이)
    4. 변수 c 계산(중심각, 두 지점 간의 대원거리를 계산하는 데 사용)
    5. 구의 반지름(EARTH_RADIUS)와 중심각(c)를 곱해 최종적으로 대원거리 계산 */

    // 지구의 반지름(km)
    private static final double EARTH_RADIUS_KM = 6371.0;

    /** 두 지점 간 거리 계산(km) -> Haversine 공식 사용 */
    @Override
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        //위도, 경도를 라디안으로 변환
        double radLat1 = Math.toRadians(lat1);
        double radLon1 = Math.toRadians(lon1);
        double radLat2 = Math.toRadians(lat2);
        double radLon2 = Math.toRadians(lon2);

        //위도, 경도의 차이 계산
        double dLat = radLat2 - radLat1;
        double dLon = radLon2 - radLon1;

        //a: 곡선 삼각형의 넓이(두 지점 사이의 각에 대한 cos과 sin의 제곱의 합)
        //지구 곡률을 고려한 거리 계산에 필요
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(radLat1) * Math.cos(radLat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        //c: 중심각 (두 지점 간의 대원거리를 계산하는데 사용, 중간값a의 제곱근과 1-a의 제곱근을 사용)
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        //두 지점간의 거리(km)
        return EARTH_RADIUS_KM * c;
    }

}

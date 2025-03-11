package com.fourroro.nolleogasil_backend.service.travelpath;

import com.fourroro.nolleogasil_backend.dto.travelpath.TravelDateDto;
import com.fourroro.nolleogasil_backend.entity.travelpath.Recommendation;
import org.springframework.stereotype.Service;
import com.fourroro.nolleogasil_backend.entity.travelpath.TravelDate;
/**
 * {@link TravelDate} 엔티티를 관리하기 위한 서비스 인터페이스입니다.
 * @author 전선민
 * @since 2024-01-10
 */
@Service
public interface TravelDateService {

    /** travelDate 추가 */
    public void insertTravelDate (Recommendation recommendation, TravelDateDto travelDateDto);
}

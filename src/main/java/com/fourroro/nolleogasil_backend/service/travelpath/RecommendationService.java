package com.fourroro.nolleogasil_backend.service.travelpath;

import com.fourroro.nolleogasil_backend.dto.travelpath.RecommendationDto;
import com.fourroro.nolleogasil_backend.entity.travelpath.Recommendation;
import com.fourroro.nolleogasil_backend.entity.travelpath.TravelPath;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
/**
 * {@link Recommendation} 엔티티를 관리하기 위한 서비스 인터페이스입니다.
 * @author 전선민
 * @since 2024-01-10
 */
@Service
public interface RecommendationService {

    /** Recommendation 추가 */
    public Recommendation insertRecommendation (TravelPath travelPath, RecommendationDto recommendationDto);
    /** Recommendation 조회 */
    public Optional<Recommendation> getRecommendation (Long recommendationId);
    /** Recommendation ID를 이용해 TravelDate 목록 조회 */
    public List<String> getTravelDateList (Long recommendationId);
    /** Recommendation ID를 이용해 TravelInfo 목록 조회 */
    public List<String> getTravelInfoList (Long recommendationId);
}

package com.fourroro.nolleogasil_backend.service.travelpath;

import com.fourroro.nolleogasil_backend.dto.travelpath.RecommendationDto;
import com.fourroro.nolleogasil_backend.entity.travelpath.Recommendation;
import com.fourroro.nolleogasil_backend.entity.travelpath.TravelPath;
import com.fourroro.nolleogasil_backend.repository.travelpath.RecommendationRepository;
import com.fourroro.nolleogasil_backend.repository.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * {@link RecommendationService} 인터페이스를 구현한 클래스입니다.
 * @author 전선민
 * @since 2024-01-10
 */
@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService{

    private final RecommendationRepository recommendationRepository;
    private final UsersRepository usersRepository;

    /** Recommendation 추가 */
    @Override
    @Transactional
    public Recommendation insertRecommendation (TravelPath travelPath, RecommendationDto recommendationDto){

        Recommendation recommendation = Recommendation.changeToEntity(recommendationDto, travelPath);

        if(recommendation.getTravelPath().getTravelpathId() != null) {
            System.out.println("recommendation: " + recommendation.getTravelPath().getTravelpathId());

            try {
                recommendationRepository.save(recommendation);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return recommendation;

    }

    /** Recommendation 조회 */
    @Override
    @Transactional
    public Optional<Recommendation> getRecommendation (Long recommendationId) {

        Optional<Recommendation> recommendation = Optional.empty();

        try {
            recommendation = recommendationRepository.findById(recommendationId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recommendation;
    }

    /** Recommendation ID를 이용해 TravelDate 목록 조회 */
    @Override
    @Transactional
    public List<String> getTravelDateList (Long recommendationId){

        List<String> travelDateList = new ArrayList<>();

        try{
            travelDateList = recommendationRepository.findDatesByRecommendationRecommendationId(recommendationId);
        }catch(Exception e) {
            e.printStackTrace();
        }

        return travelDateList;
    }

    /** Recommendation ID를 이용해 TravelInfo 목록 조회 */
    @Override
    @Transactional
    public List<String> getTravelInfoList (Long recommendationId){

        List<String> travelInfoList = new ArrayList<>();

        try{
            travelInfoList = recommendationRepository.findInfosByRecommendationRecommendationId(recommendationId);
        }catch(Exception e){
            e.printStackTrace();
        }

        return travelInfoList;
    }

}

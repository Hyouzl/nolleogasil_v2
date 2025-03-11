package com.fourroro.nolleogasil_backend.service.travelpath;

import com.fourroro.nolleogasil_backend.dto.travelpath.TravelInfoDto;
import com.fourroro.nolleogasil_backend.entity.travelpath.Recommendation;
import com.fourroro.nolleogasil_backend.entity.travelpath.TravelInfo;
import com.fourroro.nolleogasil_backend.repository.travelpath.TravelInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
/**
 * {@link TravelInfoService} 인터페이스를 구현한 클래스입니다.
 * @author 전선민
 * @since 2024-01-10
 */
@Service
@RequiredArgsConstructor
public class TravelInfoServiceImpl implements TravelInfoService{

    private final TravelInfoRepository travelInfoRepository;

    /** travelInfo 추가 */
    @Override
    @Transactional
    public void insertTravelInfo (Recommendation recommendation, TravelInfoDto travelInfoDto) {

        TravelInfo travelInfo = TravelInfo.changeToEntity(travelInfoDto, recommendation);

        try {
            travelInfoRepository.save(travelInfo);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    /** travelInfo 조회 */
    @Override
    @Transactional
    public List<TravelInfo> getTravelInfoById(Long recommendationId) {

        List<TravelInfo> travelInfos = new ArrayList<>();
        try {
            travelInfos = travelInfoRepository.findByRecommendationRecommendationId(recommendationId);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return travelInfos;
    }

    /** travelInfo 갱신 */
    @Override
    @Transactional
    public void updateTravelInfo(List<TravelInfoDto> travelInfoDtos,  Recommendation recommendation){
        List<TravelInfo> travelInfos = new ArrayList<>();
        try {
            for (int i = 0; i < travelInfoDtos.size(); i++) {
                travelInfos.add(TravelInfo.changeToEntity(travelInfoDtos.get(i), recommendation));
            }
            travelInfoRepository.saveAllAndFlush(travelInfos);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

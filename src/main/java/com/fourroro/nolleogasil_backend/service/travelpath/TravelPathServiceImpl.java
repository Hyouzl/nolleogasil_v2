package com.fourroro.nolleogasil_backend.service.travelpath;

import com.fourroro.nolleogasil_backend.dto.travelpath.TravelPathDto;
import com.fourroro.nolleogasil_backend.entity.travelpath.TravelPath;
import com.fourroro.nolleogasil_backend.entity.users.Users;
import com.fourroro.nolleogasil_backend.global.exception.GlobalException;
import com.fourroro.nolleogasil_backend.global.exception.ResultCode;
import com.fourroro.nolleogasil_backend.repository.travelpath.RecommendationRepository;
import com.fourroro.nolleogasil_backend.repository.travelpath.TravelPathRepository;
import com.fourroro.nolleogasil_backend.repository.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
/**
 * {@link TravelPathService} 인터페이스를 구현한 클래스입니다.
 * @author 전선민
 * @since 2024-01-10
 */
@Service
@RequiredArgsConstructor
public class TravelPathServiceImpl implements TravelPathService {

    private final TravelPathRepository travelPathRepository;

    private final RecommendationRepository recommendationRepository;

    private final UsersRepository usersRepository;

    /** travelPath 추가 */
    @Override
    @Transactional
    public TravelPath insertTravelPath (TravelPathDto travelPathDto){

        Users users = usersRepository.findById(travelPathDto.getUsersId())
                .orElseThrow(() -> new GlobalException(ResultCode.NOT_FOUND_USER));

        System.out.println(users.getUsersId());
        TravelPath travelPath = TravelPath.changeToEntity(travelPathDto, users);

        try {
            travelPathRepository.save(travelPath);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return travelPath;
    }


    /** TravelPath 목록 내림차순 조회 (keyword와 조인) */
    @Override
    @Transactional
    public List<TravelPath> getTravelPathListDesc(Long usersId) {

        Users users = usersRepository.findById(usersId)
                .orElseThrow(() -> new GlobalException(ResultCode.NOT_FOUND_USER));

        List<TravelPath> travelPathList = new ArrayList<>();
        try{
            travelPathList  = travelPathRepository.findTravelPathsWithKeywordByUsersUsersIdOrderByTravelPathIdDesc(users.getUsersId());
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return travelPathList;
    }


    /** TravelPath 목록 오름차순 조회 (keyword와 조인) */
    @Override
    @Transactional
    public List<TravelPath> getTravelPathListAsc(Long usersId) {

        Users users = usersRepository.findById(usersId)
                .orElseThrow(() -> new GlobalException(ResultCode.NOT_FOUND_USER));

        List<TravelPath> travelPathList = new ArrayList<>();
        try{
            travelPathList  = travelPathRepository.findTravelPathsWithKeywordByUsersUsersIdOrderByTravelPathIdAsc(users.getUsersId());
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return travelPathList;
    }


    /** TravelPath 목록 지역순 조회 (keyword와 조인) */
    @Override
    @Transactional
    public List<TravelPath> getTravelPathListByArrival(Long usersId) {

        Users users = usersRepository.findById(usersId)
                .orElseThrow(() -> new GlobalException(ResultCode.NOT_FOUND_USER));

        List<TravelPath> travelPathList = new ArrayList<>();
        try{
            travelPathList  = travelPathRepository.findTravelPathsWithKeywordByUsersUsersIdOrderByArrivalAsc(users.getUsersId());
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return travelPathList;
    }


    /** travelpathId로 TravelPath, Keyword, Recommendation 조회 */
    @Override
    @Transactional
    public TravelPath getTravelPathById(Long travelPathId){

        TravelPath travelPath = new TravelPath();
        try {
            travelPath = travelPathRepository.findTravelPathWithKeywordAndRecommendationById(travelPathId);
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return travelPath;
    }


    /** user가 저장한 TravelPath의 총 개수 계산 */
    @Override
    @Transactional
    public Long countTravelPath(Long usersId){
        return travelPathRepository.countByUsersUsersId(usersId);
    }


    /** TravelpathId와 관련된 정보 delete */
    @Override
    @Transactional
    public void deleteTravelPathById(Long travelPathId){
        try {
            travelPathRepository.deleteById(travelPathId);
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

}

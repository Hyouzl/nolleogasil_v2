package com.fourroro.nolleogasil_backend.controller.travelpath;

import com.fourroro.nolleogasil_backend.auth.jwt.util.TokenProvider;
import com.fourroro.nolleogasil_backend.dto.travelpath.*;
import com.fourroro.nolleogasil_backend.entity.travelpath.Recommendation;
import com.fourroro.nolleogasil_backend.entity.travelpath.TravelInfo;
import com.fourroro.nolleogasil_backend.entity.travelpath.TravelPath;
import com.fourroro.nolleogasil_backend.service.travelpath.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
* 이 클래스는 여행일정 관리를 위한 컨트롤러입니다.
* @author 전선민
* @since 2024-01-05
*/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/travelpath")
public class TravelPathController {

    private final TravelPathService travelPathService;
    private final KeywordService keywordService;
    private final RecommendationService recommendationService;
    private final TravelDateService travelDateService;
    private final TravelInfoService travelInfoService;
    private final TokenProvider tokenProvider;


    /**
     * session에 저장된 usersDto의 사용자 ID를 가져오는 함수
     *
     * @param session 현재 사용자의 세션 객체
     * @return 조회된 사용자 id

    private Long getSessionUsersId(HttpSession session) {
        UsersDto usersDto = (UsersDto) session.getAttribute("users");
        return usersDto.getUsersId();
    }   */

    /**
     * Form에서 받은 데이터를 ConditionDto로 받아 객체화하여 반환하는 함수
     *
     * @param conditionDto 폼 데이터 정보를 포함하는 DTO 객체
     * @return ConditionDto 객체를 포함한 ResponseEntity 객체
     */
    @PostMapping("/form")
    public ResponseEntity<ConditionDto> dataFromForm(@RequestBody ConditionDto conditionDto) {
        return ResponseEntity.status(HttpStatus.OK).body(conditionDto);
    }


    /**
     * 새로운 여행일정 데이터를 저장하는 함수
     *
     * @param travelDetailDto 여행 세부 정보를 포함하는 DTO 객체
     * @param session 현재 사용자의 세션 객체
     * @return 여행일정 목록 페이지의 경로를 포함한 ResponseEntity 객체
     */
    @PostMapping("/create")
     @Transactional

    public ResponseEntity insertTravelPathData(@RequestBody TravelDetailDto travelDetailDto,
                                               @RequestHeader("Authorization") String authorizationHeader){

        if(travelDetailDto.checkNullField()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Some fields are null");
        }
        // 1. JWT 토큰 추출
        String token = authorizationHeader.replace("Bearer ", "");

        // 2. 토큰에서 userId 추출
        Long usersId = Long.valueOf(tokenProvider.getClaims(token).getSubject());

        TravelPathDto travelPathDto = TravelPathDto.builder()
                .arrival(travelDetailDto.getDestination())
                .startDate(travelDetailDto.getStartDate())
                .endDate(travelDetailDto.getEndDate())
                .usersId(usersId)
                .build();

        TravelPath savedTravelPath = travelPathService.insertTravelPath(travelPathDto);

        KeywordDto keywordDto = KeywordDto.builder()
                .party(travelDetailDto.getPartyItems())
                .place(travelDetailDto.getPlaceItems())
                .concept(travelDetailDto.getConceptItems())
                .food(travelDetailDto.getFoodItems())
                .travelpathId(savedTravelPath.getTravelpathId())
                .build();

        RecommendationDto recommendationDto = RecommendationDto.builder()
                .travelpathId(savedTravelPath.getTravelpathId())
                .build();

        keywordService.insertKeyword(savedTravelPath, keywordDto);
        Recommendation savedRecommendation = recommendationService.insertRecommendation(savedTravelPath, recommendationDto);

        for(String date : travelDetailDto.getResultDto().getDates()) {
            TravelDateDto travelDateDto = TravelDateDto.builder()
                    .dates(date)
                    .recommendationId(savedRecommendation.getRecommendationId())
                    .build();

            travelDateService.insertTravelDate(savedRecommendation, travelDateDto);
        }

        for(String info : travelDetailDto.getResultDto().getInfos()) {
            TravelInfoDto travelInfoDto = TravelInfoDto.builder()
                    .infos(info)
                    .recommendationId(savedRecommendation.getRecommendationId())
                    .build();

            travelInfoService.insertTravelInfo(savedRecommendation, travelInfoDto);
        }
        return ResponseEntity.ok("/travelPathList");
    }


    /**
     * 저장한 여행일정 정보들을 조회하는 함수 (기본: 최신순)
     *
     * @param sortBy 정렬 기준을 나타내는 문자열 (예: "최신순", "오래된순")
     * @param session 현재 사용자의 세션 객체
     * @return 여행일정 정보를 포함한 ResponseEntity 객체
     */
    @GetMapping("/travelpathList")
    public ResponseEntity<List<Map<String, Object>>> getTravelPathList(@RequestParam(name="sortBy") String sortBy, @RequestHeader("Authorization") String authorizationHeader) {
        // 1. JWT 토큰 추출
        String token = authorizationHeader.replace("Bearer ", "");

        // 2. 토큰에서 userId 추출
        Long usersId = Long.valueOf(tokenProvider.getClaims(token).getSubject());

        List<TravelPath> travelPathList = new ArrayList<>();
        if(sortBy.equals("최신순")){
            travelPathList = travelPathService.getTravelPathListDesc(usersId);
        }
        else if(sortBy.equals("오래된순")){
            travelPathList = travelPathService.getTravelPathListAsc(usersId);
        }
        else {
            travelPathList = travelPathService.getTravelPathListByArrival(usersId);
        }

        List<Map<String, Object>> resultList = new ArrayList<>();

        for(TravelPath travelPath : travelPathList) {
            Map<String, Object> travelPathMap = new HashMap<>();
            travelPathMap.put("travelPathDto", TravelPathDto.changeToDto(travelPath));
            travelPathMap.put("keywordDto", KeywordDto.changeToDto(travelPath.getKeyword()));
            resultList.add(travelPathMap);
        }

        if (!resultList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(resultList);
        } else {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }


    /**
     * 여행일정 ID로 상세정보를 조회하는 함수
     *
     * @param travelpathId 여행일정 ID를 나타내는 Long 객체
     * @param session 현재 사용자의 세션 객체
     * @return 여행 상세 정보를 포함한 ResponseEntity 객체
     */
    @PostMapping("/travelDataDetail")
    public ResponseEntity showDetailByTravelPathId(@RequestBody Long  travelpathId) {

        TravelPath travelPath = travelPathService.getTravelPathById(travelpathId);

        Long recommendationId = travelPath.getRecommendation().getRecommendationId();
        List<String> travelDates = recommendationService.getTravelDateList(recommendationId);
        List<String> travelInfos = recommendationService.getTravelInfoList(recommendationId);

        ResultDto resultDto = new ResultDto(travelDates, travelInfos);
        TravelDetailDto travelDetailDto = new TravelDetailDto(
                travelPath.getArrival(),
                travelPath.getStartDate(),
                travelPath.getEndDate(),
                travelPath.getKeyword().getParty(),
                travelPath.getKeyword().getPlace(),
                travelPath.getKeyword().getConcept(),
                travelPath.getKeyword().getFood(),
                resultDto
        );

        Map<String, Object> detailResponse = new HashMap<>();
        detailResponse.put("travelDetailDto", travelDetailDto);
        detailResponse.put("recommendationId", recommendationId);

        return ResponseEntity.ok(detailResponse);
    }


    /**
     * 사용자가 정보 수정 시 새로운 내용을 저장하는 함수
     *
     * @param recommendationId 추천일정 ID를 나타내는 Long 객체
     * @param resultDto 기존 여행 정보(일자, 추천내용)를 포함하는 ResultDto 객체
     * @return 상태 코드 200과 함께 빈 ResponseEntity 객체
     */
    @PutMapping("/{recommendationId}")
    public ResponseEntity<String> updateTravelPathData(@PathVariable Long recommendationId, @RequestBody ResultDto resultDto){

        Optional<Recommendation> recommendation = recommendationService.getRecommendation(recommendationId);

        //조회 후 변경
        List<TravelInfo> previousInfos = travelInfoService.getTravelInfoById(recommendationId);
        List<String> newInfos = resultDto.getInfos();
        if(previousInfos.size() != newInfos.size()){
            throw new IllegalArgumentException("이전 infos와 새로운 infos의 크기가 다릅니다.");
        }

        List<TravelInfoDto> newTravelInfoDtos = new ArrayList<>();
        for(int i = 0; i < previousInfos.size(); i++){
            TravelInfo travelInfo = previousInfos.get(i);
            TravelInfoDto travelInfoDto = TravelInfoDto.changeToDto(travelInfo);
            travelInfoDto.setInfos(newInfos.get(i));
            newTravelInfoDtos.add(travelInfoDto);
        }

        travelInfoService.updateTravelInfo(newTravelInfoDtos, recommendation.get());

        return ResponseEntity.status(HttpStatus.OK).build();
    }


    /**
     * 사용자가 저장한 여행경로 정보 개수를 조회하는 함수
     *
     * @param session 현재 사용자의 세션 객체
     * @return 여행경로 정보 개수를 포함한 ResponseEntity 객체
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTravelPath ( @RequestHeader("Authorization") String authorizationHeader){
        // 1. JWT 토큰 추출
        String token = authorizationHeader.replace("Bearer ", "");

        // 2. 토큰에서 userId 추출
        Long usersId = Long.valueOf(tokenProvider.getClaims(token).getSubject());

        Long count = travelPathService.countTravelPath(usersId);

        return ResponseEntity.status(HttpStatus.OK).body(count);
    }


    /**
     * 주어진 여행일정 ID로 여행경로 정보를 삭제하는 함수
     *
     * @param travelpathId 여행일정 ID를 나타내는 Long 객체
     * @return 상태 코드 200과 함께 빈 ResponseEntity 객체
     */
    @DeleteMapping("/{travelpathId}")
    public ResponseEntity deleteTravelPath(@PathVariable Long travelpathId){
        travelPathService.deleteTravelPathById(travelpathId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
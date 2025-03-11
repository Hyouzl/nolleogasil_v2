/**
 * 맛집메이트 관리를 위한 Service interface입니다.
 * @author 박초은, 홍유리
 * @since 2024-01-10
 */
package com.fourroro.nolleogasil_backend.service.mate;

import com.fourroro.nolleogasil_backend.dto.mate.MateDto;
import com.fourroro.nolleogasil_backend.dto.mate.MateFormDto;
import com.fourroro.nolleogasil_backend.dto.place.PlaceDto;
import com.fourroro.nolleogasil_backend.entity.mate.Mate;

import java.util.List;

public interface MateService {

    /** insert Mate */
    public MateDto insertMate(MateFormDto requestMateDto, PlaceDto placeDto, Long usersId);

    /** mate 공고 글 조회(날짜순) */
    public List<MateDto> getMateList(Integer placeId, int placeCat);

    /** 사용자가 개설한 mate 공고 글 조회(최신작성순) */
    public List<MateDto> getMateListByUsersId(Long usersId);

    /** mate 공고 글 조회(거리순) */
    public List<MateDto> getMateListOrderByDistance(Integer placeId, int placeCat, double currentLat, double currentLng);

    /** 1개의 mate 공고 글 조회 */
    public Mate getMate(Long mateId);

    public MateDto getMateBychatroom(Long chatroomId);

    public PlaceDto getPlaceDto(Long chatRoomId);

    /** mate 삭제 */
    public void deleteMate(Long mateId);

}
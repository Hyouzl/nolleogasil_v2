/**
 * 맛집메이트 관리를 위한 Controller입니다.
 * @author 박초은
 * @author 홍유리 - createMateForm()
 * @since 2024-01-10
 */
package com.fourroro.nolleogasil_backend.controller.mate;

import com.fourroro.nolleogasil_backend.auth.jwt.util.TokenProvider;
import com.fourroro.nolleogasil_backend.dto.chat.ChatRoomAndPlaceDto;
import com.fourroro.nolleogasil_backend.dto.chat.ChatRoomDto;
import com.fourroro.nolleogasil_backend.dto.mate.MateDto;
import com.fourroro.nolleogasil_backend.dto.mate.MateMemberDto;
import com.fourroro.nolleogasil_backend.dto.mate.RequestMateDto;
import com.fourroro.nolleogasil_backend.dto.users.UsersDto;
import com.fourroro.nolleogasil_backend.entity.mate.Mate;
import com.fourroro.nolleogasil_backend.service.chat.ChatRoomService;
import com.fourroro.nolleogasil_backend.service.mate.MateMemberService;
import com.fourroro.nolleogasil_backend.service.mate.MateService;
import com.fourroro.nolleogasil_backend.service.place.PlaceService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mate")
public class MateController {

    private final MateService mateService;
    private final PlaceService placeService;
    private final MateMemberService mateMemberService;
    private final ChatRoomService chatRoomService;
    private final TokenProvider tokenProvider;



    /**
     * session에 저장된 UsersDto의 사용자 ID를 가져오는 함수
     *
     * @return 현재 세션에 저장된 사용자 ID
     */

    @PostMapping("/mateForm")
    public ResponseEntity<ChatRoomAndPlaceDto> creatMateForm(@RequestBody RequestMateDto requestMateDto,
                                                             @RequestParam(name = "category") String category,  @RequestHeader("Authorization") String authorizationHeader) {

        ChatRoomDto chatRoomDto = null;
        ChatRoomAndPlaceDto chatRoomAndPlaceDto = null;
        try {
            // 장소 카테고리 변경 후 mateDto에 저장
            int placeCat = requestMateDto.getPlaceDto().getPlaceCat();
            if (placeCat == 0) {
                placeCat = placeService.changeToPlaceCat(category);
                requestMateDto.getPlaceDto().setPlaceCat(placeCat);

            }
            // 1. JWT 토큰 추출
            String token = authorizationHeader.replace("Bearer ", "");

            // 2. 토큰에서 userId 추출
            Long userId = Long.valueOf(tokenProvider.getClaims(token).getSubject());

            //메이트 공고글 생성
            MateDto mateDto = mateService.insertMate(requestMateDto.getMateFormDto(),requestMateDto.getPlaceDto(),userId);
            //메이트 공고글에 해당하는 챗룸 생성
            chatRoomDto = chatRoomService.createRoom(mateDto,userId);
            //해당 사용자 메이트 멤버로 등록.
            //memberDto 반환하는 로직 MateMemberService에 메서드로 만듬!
            MateMemberDto mateMemberDto = mateMemberService.creatMateMemberDto(mateDto, chatRoomDto.getChatroomId(), userId);
            mateMemberService.insertMateMember(mateMemberDto);
            chatRoomAndPlaceDto = ChatRoomAndPlaceDto.createDto(chatRoomDto,requestMateDto.getPlaceDto(),mateDto);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(chatRoomAndPlaceDto, HttpStatus.OK);
    }

    /**
     * 맛집메이트 목록 조회
     * 1. 지도에서 장소 이름 클릭 시, 해당 장소에 개설된 맛집메이트만 조회
     * 2. 메인화면에서 맛집메이트 클릭 시, 모집 중인 모든 맛집메이트 조회
     *      -> 장소 카테고리 클릭 시, 해당하는 카테고리의 맛집메이트 공고 글만 조회
     *
     * @param placeId 해당 장소 ID
     * @param placeCat 선택한 장소 카테고리 (전체(0), 맛집(1), 카페(2), 숙소(3), 관광지(4))
     * @param currentLat 사용자 현재 위치의 위도
     * @param currentLng 사용자 현재 위치의 경도
     * @param sorted 선택한 정렬 기준(날짜순 - 현재 날짜 및 시간과 가까운 순, 가까운 순 - 사용자의 현재 위치에서 가까운 순)
     * @return 조회된 맛집메이트 목록을 포함한 HTTP 상태 코드가 200인 ResponseEntity 객체,
     *         서버 오류 발생 시, HTTP 상태 코드가 500인 ResponseEntity 객체
     */
    @GetMapping
    public ResponseEntity<List<MateDto>> getMateList(@RequestParam Integer placeId,
                                                     @RequestParam Integer placeCat,
                                                     @RequestParam Double currentLat,
                                                     @RequestParam Double currentLng,
                                                     @RequestParam(defaultValue = "날짜순") String sorted) {
        try {
            List<MateDto> mateList;
            if (sorted.equals("날짜순")) {
                mateList = mateService.getMateList(placeId, placeCat);
            } else {
                mateList = mateService.getMateListOrderByDistance(placeId, placeCat, currentLat, currentLng);
            }
            return ResponseEntity.ok(mateList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 1개의 맛집메이트 정보 조회
     *
     * @param mateId 조회할 맛집메이트 ID
     * @return 조회된 맛집메이트의 정보를 포함한 HTTP 상태 코드가 200인 ResponseEntity 객체,
     *         조회된 맛집메이트의 정보가 없을 시 HTTP 상태 코드가 404인 ResponseEntity 객체,
     *         HTTP 상태 코드가 400(잘못된 요청 시), 500(서버 오류 발생 시)인 ResponseEntity 객체
     */
    @GetMapping("/{mateId}")
    public ResponseEntity<MateDto> getMate(@PathVariable Long mateId) {
        try {
            if (mateId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            Mate mate = mateService.getMate(mateId);
            if (mate == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            return ResponseEntity.ok(MateDto.changeToDto(mate));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 로그인한 사용자가 개설한 맛집메이트 목록 조회
     *
     * @return 조회된 맛집메이트 목록을 포함한 HTTP 상태 코드가 200인 ResponseEntity 객체,
     *         서버 오류 발생 시 HTTP 상태 코드가 500인 ResponseEntity 객체
     */
    @GetMapping("/my-create")
    public ResponseEntity<List<MateDto>> getMateListByUsersId(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // 1. JWT 토큰 추출
            String token = authorizationHeader.replace("Bearer ", "");

            // 2. 토큰에서 userId 추출
            Long userId = Long.valueOf(tokenProvider.getClaims(token).getSubject());

            List<MateDto> mateList = mateService.getMateListByUsersId(userId);
            return ResponseEntity.ok(mateList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 맛집메이트 정보 삭제
     * 단, 아직 사용된 부분 없음
     *
     * @param mateId 삭제할 맛집메이트 ID
     * @return HTTP 상태 코드가 204(삭제 성공 시), 400(잘못된 요청 시), 500(서버 오류 발생 시)인 ResponseEntity 객체
     */
    @DeleteMapping("/{mateId}")
    public ResponseEntity<Void> deleteMate(@PathVariable Long mateId) {
        try {
            if (mateId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            mateService.deleteMate(mateId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
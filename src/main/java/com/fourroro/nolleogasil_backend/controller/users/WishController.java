/**
 * 위시(내 장소) 관리를 위한 Controller입니다.
 * @author 박초은
 * @since 2024-01-10
 */
package com.fourroro.nolleogasil_backend.controller.users;

import com.fourroro.nolleogasil_backend.auth.jwt.util.TokenProvider;
import com.fourroro.nolleogasil_backend.dto.place.PlaceDto;
import com.fourroro.nolleogasil_backend.dto.users.WishDto;
import com.fourroro.nolleogasil_backend.entity.place.Place;
import com.fourroro.nolleogasil_backend.entity.users.Wish;
import com.fourroro.nolleogasil_backend.service.place.PlaceService;
import com.fourroro.nolleogasil_backend.service.users.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wish")
public class WishController {

    private final WishService wishService;
    private final PlaceService placeService;
    private final TokenProvider tokenProvider;



    /**
     * 위시리스트(내 장소)에 추가 버튼(빈 하트) 클릭 시, 위시 정보 저장
     *
     * @param placeDto 저장할 장소 객체
     * @param category 저장할 장소의 카테고리 코드 (kakao에서 제공되는 장소 코드)
     * @param session 현재 사용자의 세션 객체
     * @return HTTP 상태 코드가 201(신청 저장 성공 시), 400(잘못된 요청 시), 500(서버 오류 발생 시)인 ResponseEntity 객체
     */
    @PostMapping
    public ResponseEntity<Void> createWish(@RequestBody PlaceDto placeDto, @RequestParam String category, @RequestHeader("Authorization") String authorizationHeader)  {
        try {
            int placeCat = placeService.changeToPlaceCat(category);
            if (placeCat == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            // 1. JWT 토큰 추출
            String token = authorizationHeader.replace("Bearer ", "");

            // 2. 토큰에서 userId 추출
            Long userId = Long.valueOf(tokenProvider.getClaims(token).getSubject());


            placeDto.setPlaceCat(placeCat);
            WishDto wishDto = WishDto.builder()
                    .usersId(userId)
                    .place(placeDto)
                    .build();

            //이미 place table에 해당 place가 존재하는지
            if (placeService.checkPlaceColumn(placeDto.getPlaceId())) {
                wishService.insertWish(wishDto);
            } else {
                System.out.println("place가 존재하지 않습니다.");
                //해당 place가 DB에 존재하지 않는다면, insert place
                Place place = placeService.insertPlace(placeDto);
                wishService.insertWish(wishDto);
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 로그인한 사용자의 위시리스트(내 장소)에 해당 장소가 있는지 조회
     * 해당 장소가 있다면 지도에서 버튼이 채워진 하트(위시에서 제거), 없다면 빈 하트(위시에 추가)로 출력
     *
     * @param placeId 해당 장소 ID
     * @param session 현재 사용자의 세션 객체
     * @return 위시리스트에 있으면 true, 없으면 false값을 포함한 HTTP 상태 코드가 200인 ResponseEntity 객체,
     *         서버 오류 발생 시 HTTP 상태 코드가 500인 ResponseEntity 객체
     */
    @GetMapping("/{placeId}/status")
    public ResponseEntity<Boolean> checkingWishStatus(@PathVariable Integer placeId,  @RequestHeader("Authorization") String authorizationHeader) {
        try {
            // 1. JWT 토큰 추출
            String token = authorizationHeader.replace("Bearer ", "");

            // 2. 토큰에서 userId 추출
            Long userId = Long.valueOf(tokenProvider.getClaims(token).getSubject());

            boolean isWish = wishService.checkWishColumn(userId, placeId);

            return ResponseEntity.ok(isWish);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 로그인한 사용자의 위시리스트(내 장소) 조회
     * 장소 카테고리 선택 시, 해당하는 장소만 조회
     *
     * @param placeCat 선택한 장소 카테고리 (전체(0), 맛집(1), 카페(2), 숙소(3), 관광지(4))
     * @param sortBy 선택한 정렬 기준 (기본순, 최신순, 오래된 순)
     * @param session 현재 사용자의 세션 객체
     * @return 필터링과 정렬이 완료된 위시리스트를 포함한 HTTP 상태 코드가 200인 ResponseEntity 객체,
     *         서버 오류 발생 시 HTTP 상태 코드가 500인 ResponseEntity 객체
     */
    @GetMapping("/{placeCat}")
    public ResponseEntity<List<WishDto>> getWishList(@PathVariable int placeCat, @RequestParam String sortBy,
                                                     @RequestHeader("Authorization") String authorizationHeader) {
        try {
            // 1. JWT 토큰 추출
            String token = authorizationHeader.replace("Bearer ", "");

            // 2. 토큰에서 userId 추출
            Long userId = Long.valueOf(tokenProvider.getClaims(token).getSubject());

            List<Wish> wishList;
            if (sortBy.equals("기본순")) {
                wishList = wishService.getWishList(userId, placeCat);
            } else {
                wishList = wishService.getSortedWishList(userId, placeCat, sortBy);
            }

            List<WishDto> wishDtoList = new ArrayList<>();
            for (Wish wish : wishList) {
                WishDto wishDto = WishDto.changeToDto(wish);
                wishDtoList.add(wishDto);
            }

            return ResponseEntity.ok(wishDtoList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 로그인한 사용자의 위시리스트(내 장소)에 저장된 카테고리 별 장소 개수 조회
     * 장소 카테고리 선택 시, 해당하는 장소의 개수만 조회
     *
     * @param placeCat 선택한 장소 카테고리 (전체(0), 맛집(1), 카페(2), 숙소(3), 관광지(4))
     * @param session 현재 사용자의 세션 객체
     * @return 조회된 장소 개수를 포함한 HTTP 상태 코드가 200인 ResponseEntity 객체,
     *         서버 오류 발생 시 HTTP 상태 코드가 500인 ResponseEntity 객체
     */
    @GetMapping("/{placeCat}/count")
    public ResponseEntity<Long> countWish(@PathVariable int placeCat, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            // 1. JWT 토큰 추출
            String token = authorizationHeader.replace("Bearer ", "");

            // 2. 토큰에서 userId 추출
            Long userId = Long.valueOf(tokenProvider.getClaims(token).getSubject());

            Long count;
            if (placeCat == 0) {  //0: 전체
                count = wishService.countWish(userId);
            } else {  //1: 맛집, 2: 카페, 3: 관광지, 4: 숙소
                count = wishService.countWishByPlaceCat(userId, placeCat);
            }
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 위시리스트(내 장소)에서 제거 버튼(채워진 하트) 클릭 시, 해당 위시 정보 삭제
     * 1. wishId가 0이라면, 지도에서 넘어온 요청 -> placeId와 usersId로 삭제하고자 하는 wishId 조회 후 삭제
     * 2. wishId가 0이 아니라면, 위시리스트(내 장소)에서 넘어온 요청 -> wishId로 삭제
     *
     * @param wishId 삭제할 위시 ID
     * @param placeId 삭제할 장소 ID
     * @param session 현재 사용자의 세션 객체
     * @return HTTP 상태 코드가 204(삭제 성공 시), 400(잘못된 요청 시), 500(서버 오류 발생 시)인 ResponseEntity 객체
     */
    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWish(@PathVariable Long wishId, @RequestParam(required = false) Integer placeId,
                                           @RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (wishId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            // 1. JWT 토큰 추출
            String token = authorizationHeader.replace("Bearer ", "");

            // 2. 토큰에서 userId 추출
            Long userId = Long.valueOf(tokenProvider.getClaims(token).getSubject());


            //wishId가 0이라면, 지도에서 넘어온 요청 -> 삭제하고자 하는 wishId를 조회한 후, 삭제
            if (wishId == 0) {
                Wish wish = wishService.getWishByUsersIdAndPlaceId(userId, placeId);
                wishService.deleteWish(wish.getWishId());
            } else {
                //wishId가 0이 아니라면 내 장소에서 넘어온 요청
                wishService.deleteWish(wishId);
            }
            return ResponseEntity.noContent().build();
        }  catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
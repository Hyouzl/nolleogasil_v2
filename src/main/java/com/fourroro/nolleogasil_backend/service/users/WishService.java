/**
 * 위시(내 장소) 관리를 위한 Service interface입니다.
 * @author 박초은
 * @since 2024-01-10
 */
package com.fourroro.nolleogasil_backend.service.users;

import com.fourroro.nolleogasil_backend.dto.users.WishDto;
import com.fourroro.nolleogasil_backend.entity.users.Wish;

import java.util.List;

public interface WishService {

    /** insert wish */
    public void insertWish(WishDto wishDto);

    /** wish 목록 조회 */
    public List<Wish> getWishList(Long usersId, int placeCat);

    /** wish 목록 정렬 조회 */
    public List<Wish> getSortedWishList(Long usersId, int placeCat, String sortBy);

    /** 1개의 wish 조회(wishId를 찾기 위해) */
    public Wish getWishByUsersIdAndPlaceId(Long usersId, Integer placeId);

    /** wish 유무 확인 */
    public Boolean checkWishColumn(Long usersId, Integer placeId);

    /** 저장된 총 wish 개수 조회 */
    public Long countWish(Long usersId);

    /** 저장된 해당 placeCate의 wish 개수 조회 */
    public Long countWishByPlaceCat(Long usersId, int placeCat);

    /** wish 삭제 */
    public void deleteWish(Long wishId);

}
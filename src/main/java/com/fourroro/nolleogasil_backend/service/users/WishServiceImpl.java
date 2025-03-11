/**
 * 위시(내 장소) 관리를 위한 Service클래스입니다.
 * @author 박초은
 * @since 2024-01-10
 */
package com.fourroro.nolleogasil_backend.service.users;

import com.fourroro.nolleogasil_backend.dto.users.WishDto;
import com.fourroro.nolleogasil_backend.entity.place.Place;
import com.fourroro.nolleogasil_backend.entity.users.Users;
import com.fourroro.nolleogasil_backend.entity.users.Wish;
import com.fourroro.nolleogasil_backend.repository.place.PlaceRepository;
import com.fourroro.nolleogasil_backend.repository.users.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishServiceImpl implements WishService {

    private final WishRepository wishRepository;
    private final UsersService usersService;
    private final PlaceRepository placeRepository;

    /** insert wish */
    @Transactional
    @Override
    public void insertWish(WishDto wishDto) {
        Users users = usersService.findByUsersId(wishDto.getUsersId());
        Place place = placeRepository.findById(wishDto.getPlace().getPlaceId()).orElseThrow(()-> new RuntimeException("장소 정보가 없습니다."));
        Wish wish = Wish.changeToEntity(users, place);
        wishRepository.save(wish);
    }

    /** wish 목록 조회 */
    @Override
    public List<Wish> getWishList(Long usersId, int placeCat) {
        if (placeCat == 0) {
            //전체 wish 목록 조회
            return wishRepository.findByUsersUsersId(usersId);
        } else {
            //해당 placeCat의 wish 목록 조회(맛집(1), 카페(2), 숙소(3), 관광지(4))
            return wishRepository.findByUsersUsersIdAndPlacePlaceCat(usersId, placeCat);
        }
    }

    /** wish 목록 정렬 조회 */
    @Override
    public List<Wish> getSortedWishList(Long usersId, int placeCat, String sortBy) {
        //전체 wish 목록 정렬
        if (placeCat == 0) {
            if (sortBy.equals("최신순")) {
                return wishRepository.findByUsersUsersIdOrderByWishIdDesc(usersId);
            } else { //오래된 순
                return wishRepository.findByUsersUsersIdOrderByWishIdAsc(usersId);
            }
        } else {
            //해당 placeCat의 wish 목록 정렬
            if (sortBy.equals("최신순")) {
                return wishRepository.findByUsersUsersIdAndPlacePlaceCatOrderByWishIdDesc(usersId, placeCat);
            } else { //오래된 순
                return wishRepository.findByUsersUsersIdAndPlacePlaceCatOrderByWishIdAsc(usersId, placeCat);
            }
        }
    }

    /** 1개의 wish 조회(wishId를 찾기 위해) */
    @Override
    public Wish getWishByUsersIdAndPlaceId(Long usersId, Integer placeId) {
        return wishRepository.findByUsersUsersIdAndPlacePlaceId(usersId, placeId);
    }

    /** wish 유무 확인 */
    @Override
    public Boolean checkWishColumn(Long usersId, Integer placeId) {
        return wishRepository.existsByUsersUsersIdAndPlacePlaceId(usersId, placeId);
    }

    /** 저장된 총 wish 개수 조회 */
    @Override
    public Long countWish(Long usersId) {
        return wishRepository.countByUsersUsersId(usersId);
    }

    /** 저장된 해당 placeCate의 wish 개수 조회 */
    @Override
    public Long countWishByPlaceCat(Long usersId, int placeCat) {
        return wishRepository.countByUsersUsersIdAndPlacePlaceCat(usersId, placeCat);
    }

    /** wish 삭제 */
    @Transactional
    @Override
    public void deleteWish(Long wishId) {
        wishRepository.deleteById(wishId);
    }

}

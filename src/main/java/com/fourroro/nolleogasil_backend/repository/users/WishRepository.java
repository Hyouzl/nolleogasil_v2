package com.fourroro.nolleogasil_backend.repository.users;

import com.fourroro.nolleogasil_backend.entity.users.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
/**
 * {@link Wish}엔티티와 관련된 데이터 작업을 처리하는 Repository interface입니다.
 * @author 박초은
 * @since 2024-01-10
 */

public interface WishRepository extends JpaRepository<Wish, Long> {
    //save()(=persist())
    //deleteById()

    //---------wishList 조회---------
    /** wish 목록 조회 */
    public List<Wish> findByUsersUsersId(Long usersId);

    /** 해당 placeCat의 wish 목록 조회 */
    public List<Wish> findByUsersUsersIdAndPlacePlaceCat(Long usersId, int placeCat);

    /** wish 목록 조회(오름차순) */
    public List<Wish> findByUsersUsersIdOrderByWishIdAsc(Long usersId);

    /** wish 목록 조회(내림차순) */
    public List<Wish> findByUsersUsersIdOrderByWishIdDesc(Long usersId);

    /** 해당 placeCat의 wish 목록 정렬(오름차순) */
    public List<Wish> findByUsersUsersIdAndPlacePlaceCatOrderByWishIdAsc(Long usersId, int placeCat);

    /** 해당 placeCat의 wish 목록 정렬(내림차순) */
    public List<Wish> findByUsersUsersIdAndPlacePlaceCatOrderByWishIdDesc(Long usersId, int placeCat);
    //------------------------------

    /** 1개의 wish 정보 조회 */
    public Wish findByUsersUsersIdAndPlacePlaceId(Long usersId, Integer placeId);

    /** wish 유무 확인 */
    public Boolean existsByUsersUsersIdAndPlacePlaceId(Long usersId, Integer placeId);

    /** 저장된 총 wish 개수 조회 */
    public Long countByUsersUsersId(Long usersId);

    /** 저장된 해당 placeCat의 wish 개수 조회 */
    public Long countByUsersUsersIdAndPlacePlaceCat(Long usersId, int placeCat);

}
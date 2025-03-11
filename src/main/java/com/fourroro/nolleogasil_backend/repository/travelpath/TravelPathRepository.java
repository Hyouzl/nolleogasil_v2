package com.fourroro.nolleogasil_backend.repository.travelpath;

import com.fourroro.nolleogasil_backend.entity.travelpath.Keyword;
import com.fourroro.nolleogasil_backend.entity.travelpath.TravelPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * {@link TravelPath} 엔티티와 관련된 데이터 작업을 처리하는 리포지토리 인터페이스입니다.
 * @author 전선민
 * @since 2024-01-10
 */
@Repository
public interface TravelPathRepository extends JpaRepository<TravelPath, Long> {
    //save(), findById(), count(), delete() 등은 자동 상속

    /** TravelPath 목록 내림차순 조회 (keyword와 조인) */
    @Query("SELECT tp FROM TravelPath tp JOIN FETCH tp.keyword k WHERE tp.users.usersId = :usersId ORDER BY tp.travelpathId DESC")
    public List<TravelPath> findTravelPathsWithKeywordByUsersUsersIdOrderByTravelPathIdDesc(@Param("usersId") Long usersId);

    /** TravelPath 목록 오름차순 조회 (keyword와 조인) */
    @Query("SELECT tp FROM TravelPath tp JOIN FETCH tp.keyword k WHERE tp.users.usersId = :usersId ORDER BY tp.travelpathId ASC")
    public List<TravelPath> findTravelPathsWithKeywordByUsersUsersIdOrderByTravelPathIdAsc(@Param("usersId") Long usersId);

    /** TravelPath 목록 지역순 조회 (keyword와 조인) */
    @Query("SELECT tp FROM TravelPath tp JOIN FETCH tp.keyword k WHERE tp.users.usersId = :usersId ORDER BY tp.arrival ASC")
    public List<TravelPath> findTravelPathsWithKeywordByUsersUsersIdOrderByArrivalAsc(@Param("usersId") Long usersId);

    /** travelpathId로 TravelPath, Keyword, Recommendation 조회 */
    @Query("SELECT tp FROM TravelPath tp JOIN FETCH tp.keyword k JOIN FETCH tp.recommendation r WHERE tp.travelpathId = :travelpathId")
    public TravelPath findTravelPathWithKeywordAndRecommendationById(@Param("travelpathId")Long travelpathId);

    /** user가 저장한 TravelPath의 총 개수 계산 */
    public Long countByUsersUsersId(Long usersId);
}

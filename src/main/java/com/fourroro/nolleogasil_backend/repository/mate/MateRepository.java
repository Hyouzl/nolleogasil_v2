package com.fourroro.nolleogasil_backend.repository.mate;

import com.fourroro.nolleogasil_backend.entity.mate.Mate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
/**
 * {@link Mate}엔티티와 관련된 데이터 작업을 처리하는 Repository interface입니다.
 * @author 박초은, 홍유리
 * @since 2024-01-10
 */

public interface MateRepository extends JpaRepository<Mate, Long> {
    //save()(=persist())
    //findById()
    //deleteById()

    /** 모든 mate 공고 글 조회(활성화된 글만) -> 마감기한이 가까운 순으로 */
    @Query("select m from Mate m JOIN m.place p " +
            "where m.display = :display order by m.eatDate ASC, m.eatTime ASC")
    public List<Mate> findByDisplay(@Param("display")int display);

    /** 해당 placeCat에 관한 mate 골고 글 조회(활성화된 글만) -> 마감기한이 가까운 순으로 */
    @Query("select m from Mate m JOIN m.place p " +
            "where m.display = :display AND p.placeCat = :placeCat order by m.eatDate ASC, m.eatTime ASC")
    public List<Mate> findByDisplayAndPlaceCat(@Param("display")int display, @Param("placeCat")Integer placeCat);

    /** 해당 place에 관한 mate 공고 글 조회(활성화된 글만) -> 마감기한이 가까운 순으로 */
    @Query("select m from Mate m JOIN m.place p " +
            "where m.display = :display AND p.placeId = :placeId order by m.eatDate ASC, m.eatTime ASC")
    public List<Mate> findByDisplayAndPlaceId(@Param("display")int display, @Param("placeId")Integer placeId);

    /** 사용자가 개설한 Mate 공고 글 조회(최신작성순) */
    public List<Mate> findByUsersUsersIdOrderByMateIdDesc(Long usersId);

    public Mate findByChatRoomChatroomId(Long chatRoomId);

}
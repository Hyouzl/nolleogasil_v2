package com.fourroro.nolleogasil_backend.repository.mate;

import com.fourroro.nolleogasil_backend.entity.mate.MateMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
/**
 * {@link MateMember}엔티티와 관련된 데이터 작업을 처리하는 Repository interface입니다.
 * @author 박초은, 홍유리
 * @since 2024-01-10
 */

public interface MateMemberRepository extends JpaRepository<MateMember, Long> {
    //save()(=persist())
    //findById()
    //deleteById()

    public MateMember findByChatRoomChatroomIdAndUsersUsersId(Long chatroomId, Long usersId);

    public List<MateMember> findAllByUsersUsersId(Long usersId);

    public List<MateMember> findAllByUsers_UsersIdOrderByMate_EatDateDesc(Long usersId);

    public List<MateMember> findAllByUsers_UsersIdOrderByMate_EatDate(Long usersId);

    /** 해당 mate의 mateMember 수 */
    public Long countByMateMateId(Long mateId);

    /** 해당 mate의 member 목록 조회 */
    public List<MateMember> findByMateMateIdOrderByMatememberId(Long mateId);

    /** 사용자 본인을 제외한 mate의 member 목록 조회 */
    @Query("select mem from MateMember mem JOIN mem.mate m where m.mateId = :mateId AND mem.users.usersId <> :usersId order by mem.matememberId ASC")
    public List<MateMember> findByMateMemberExcludingMe(@Param("mateId")Long mateId, @Param("usersId")Long usersId);

    /** usersId, mateId로 1명의 MateMember 조회 */
    public Optional<MateMember> findByUsersUsersIdAndMateMateId(Long usersId, Long mateId);

    /** mate 이력 조회(날짜순 -> 현재날짜와 가까운 순) */
    @Query("select mem from MateMember mem JOIN mem.mate m where mem.users.usersId = :usersId order by m.eatDate DESC, m.eatTime DESC")
    public List<MateMember> findMateHistoryByUsersId(@Param("usersId")Long usersId);

}
package com.fourroro.nolleogasil_backend.repository.users;

import com.fourroro.nolleogasil_backend.entity.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 이 인터페이스는 회원 정보 관리를 위한 Repository입니다.
 * 회원 정보를 DB에 저장, 수정, 삭제, 조회하기 위해 존재합니다.
 * @author 장민정
 * @author 박초은 - setMateTemp()
 * @since 2024-01-05
 */
@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    @Modifying
    @Query(value = "update Users u " +
            "set u.nickname = :nickname " +
            "where u.usersId = :usersId")
    public void updateUsers(@Param("nickname")String nickname, @Param("usersId")Long usersId);

    @Modifying
    @Query(value = "update Users u " +
            "set u.phone = :phone " +
            "where u.usersId = :usersId")
    public void updatePhone(@Param("phone")String phone, @Param("usersId")Long usersId);

    public Users findByNickname(String nickname);

    @Query(value = "SELECT u FROM Users u WHERE u.usersId = :usersId")
    public Optional<Users> findByUsersId(@Param("usersId") Long usersId);


    public Optional<Users> findByEmail(String email);

    @Query(value = "SELECT u.nickname FROM Users u WHERE u.email = :email")
    public String findNickname(@Param("email")String email);



    @Query(value = "SELECT u.nickname FROM Users u WHERE u.usersId = :usersId")
    public String findNicknameByUsersId(@Param("usersId") Long usersId);

    public Users findUsersByEmail(String email);

    public Optional<Users> findByPhone(String phone);
    public Optional<Users> findByName(String name);

    /** 사용자의 mateTemp 값 변경 */
    @Modifying
    @Query("update Users u set u.matetemp = :mateTemp where u.usersId = :usersId")
    public void setMateTemp(@Param("usersId")Long usersId, @Param("mateTemp")float mateTemp);

    Optional<Users> findByEmailAndProvider(String email, String provider);
}
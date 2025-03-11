package com.fourroro.nolleogasil_backend.service.users;

import com.fourroro.nolleogasil_backend.dto.users.UsersDto;
import com.fourroro.nolleogasil_backend.entity.users.Users;

import java.util.List;

/**
 * 이 인터페이스는 회원 정보 관리를 위한 Service Interface입니다.
 * @author 장민정
 * @author 박초은 - setMateTemp()
 * @since 2024-01-05
 */
public interface UsersService {

    public void insertUsers(UsersDto users);
    //public Users findByusersId(Long usersId);

    public Users findByUsersId(Long usersId);

    public Users findUsers(Long usersId);

    public List<Users> findAll();

    public String findNickname(String email);


    public Users findByEmail(String email);

    public Users findByPhone(String phone);
    
    //회원저장

    //회원정보 수정
    public void updateUsers(String nickname, Long usersId);

    public void deleteUsers(Long usersId);

    Users findUsersByEmail(String email);

    /** 사용자의 mateTemp 값 변경 */
    public void setMateTemp(Long usersId, Float mateTemp);
}

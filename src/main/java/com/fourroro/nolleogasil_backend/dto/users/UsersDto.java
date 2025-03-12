package com.fourroro.nolleogasil_backend.dto.users;

import com.fourroro.nolleogasil_backend.entity.users.Users;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.io.Serializable;

/**
 * 이 클래스는 회원 정보 관리를 위한 DTO입니다.
 * @author 장민정
 * @since 2024-01-05
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    private Long usersId;
    @NotNull
    private String loginId;
    @NotNull
    private String password;

    @NotNull
    private String name;
    @NotNull
    private String email;
    @NotNull
    private String nickname;
    @NotNull
    private String phone;
    @NotNull
    private String gender;
    private float mateTemp;

    @Builder
    public UsersDto(Long usersId, String name, String email, String nickname, String phone, String gender, float mateTemp){
        this.usersId = usersId;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.phone = phone;
        this.gender = gender;
        this.mateTemp = mateTemp;
    }

    public void setMateTemp(float mateTemp) {
        this.mateTemp = mateTemp;
    }

    public static UsersDto changeToDto(Users entity) {
        return UsersDto.builder()
                .usersId(entity.getUsersId())
                .name(entity.getName())
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .phone(entity.getPhone())
                .gender(entity.getGender())
                .mateTemp(entity.getMatetemp())
                .build();
    }

    public static UsersDto changeToUsersDTO (Users users) {
        return UsersDto.builder()
                .usersId(users.getUsersId())
                .loginId(users.getLoginId())
                .password(users.getPassword())
                .name(users.getName())
                .email(users.getEmail())
                .nickname(users.getNickname())
                .phone(users.getPhone())
                .mateTemp(users.getMatetemp())
                .build();
    }

}
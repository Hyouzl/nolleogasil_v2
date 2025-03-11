package com.fourroro.nolleogasil_backend.dto.users;

import com.fourroro.nolleogasil_backend.entity.users.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.io.Serializable;

/**
 * 이 클래스는 회원 정보 관리를 위한 DTO입니다.
 * @author 장민정
 * @since 2024-01-05
 */
@Getter
@Setter
@NoArgsConstructor
public class UsersDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    private Long usersId;
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
}
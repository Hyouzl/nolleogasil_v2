package com.fourroro.nolleogasil_backend.entity.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fourroro.nolleogasil_backend.dto.users.UsersDto;
import com.fourroro.nolleogasil_backend.entity.chat.Chat;
import com.fourroro.nolleogasil_backend.entity.chat.ChatRoom;
import com.fourroro.nolleogasil_backend.entity.mate.Apply;
import com.fourroro.nolleogasil_backend.entity.mate.Mate;
import com.fourroro.nolleogasil_backend.entity.mate.MateMember;
import com.fourroro.nolleogasil_backend.entity.travelpath.TravelPath;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 이 클래스는 회원 정보 관리를 위한 Entity입니다.
 * @author 장민정
 * @since 2024-01-05
 */
@Getter
@Entity
@Table(name = "Users")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usersId;

    private String name;

    @JoinColumn(name = "email", unique=true)
    private String email;

    private String password;

    @JoinColumn(name = "nickname", unique=true)
    private String nickname;

    private String phone;

    private String gender;

    private float matetemp;

    private String provider;
    private Role role;

    //회원가입시 matetemp값을 지정해주지 않아 0으로 data 삽입
    //0이라면 defult값인 36.5로 설정하여 해당 문제 방지
    @PrePersist
    public void defaultMateTemp() {
        this.matetemp = this.matetemp == 0? 36.5f: this.matetemp;
    }

    @OneToMany(mappedBy = "users", cascade = CascadeType.REMOVE)
    private List<Chat> chatList;

    @OneToMany(mappedBy = "users", cascade = CascadeType.REMOVE)
    private List<ChatRoom> chatRoomList;

    @OneToMany(mappedBy = "users", cascade = CascadeType.REMOVE)
    private List<Apply> applyList;

    @OneToMany(mappedBy = "users", cascade = CascadeType.REMOVE)
    private List<Mate> mateList;

    @OneToMany(mappedBy = "users", cascade = CascadeType.REMOVE)
    private List<MateMember> mateMemberList;

    @OneToMany(mappedBy = "users", cascade = CascadeType.REMOVE)
    private List<TravelPath> travelPathList;

    @OneToMany(mappedBy = "users", cascade = CascadeType.REMOVE)
    private List<Wish> wishList;

    @Override
    public String toString() {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this);
        }catch(Exception e){
            e.printStackTrace();
            return "{}";
        }
    }

    public Users update(String name, String email) {
        this.name = name;
        this.email = email;

        return this;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public static Users changeToEntity(UsersDto dto) {
        return Users.builder()
                .usersId(dto.getUsersId())
                .name(dto.getName())
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .phone(dto.getPhone())
                .gender(dto.getGender())
                .matetemp(dto.getMateTemp())
                .build();
    }

}

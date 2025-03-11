/**
 * Mate Table에 매칭되는 Entity입니다.
 * @author 박초은
 * @since 2024-01-05
 */
package com.fourroro.nolleogasil_backend.entity.mate;

import com.fourroro.nolleogasil_backend.dto.mate.MateDto;
import com.fourroro.nolleogasil_backend.dto.mate.MateFormDto;
import com.fourroro.nolleogasil_backend.entity.chat.ChatRoom;
import com.fourroro.nolleogasil_backend.entity.place.Place;
import com.fourroro.nolleogasil_backend.entity.users.Users;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Mate")
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mateId;

    @ManyToOne
    @JoinColumn(name = "usersId")
    private Users users;
    private String title;       //글 제목
    private LocalDate eatDate;  //날짜
    private String eatTime;     //시간
    private int count;          //모집할 인원수
    private String gender;      //선호하는 성별
    private String comments;    //추가로 올리고 싶은 comments
    private int display;        //공고 글의 활성화 여부(1: 활성화, 0: 비활성화) -> default값: 1

    //Mate:Place = 1:1 단방향 연관관계
    @OneToOne
    @JoinColumn(name = "placeId")
    private Place place;

    //Mate:ChatRoom = 1:1 양방향 연관관계
    @OneToOne(mappedBy = "mate", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ChatRoom chatRoom;

    //Mate:MateMember = 1:N 양방향 연관관계
    @OneToMany(mappedBy = "mate", fetch = FetchType.LAZY)
    private List<MateMember> mateMembers;

    public static Mate toEntity(MateFormDto requestMateDto, Users users, Place place) {
        return Mate.builder()
                .users(users)
                .title(requestMateDto.getTitle())
                .eatDate(requestMateDto.getEatDate())
                .eatTime(requestMateDto.getEatTime())
                .place(place)
                .comments(requestMateDto.getComments())
                .gender(requestMateDto.getGender())
                .count(requestMateDto.getCount())
                .display(1)
                .mateMembers(new ArrayList<>())
                .build();
    }

    //dto -> entity
    public static Mate changeToEntity(MateDto dto, Users users) {
        return Mate.builder()
                .mateId(dto.getMateId())
                .users(users)
                .title(dto.getTitle())
                .eatDate(dto.getEatDate())
                .eatTime(dto.getEatTime())
                .gender(dto.getGender())
                .count(dto.getCount())
                .comments(dto.getComments())
                .display(dto.getDisplay())
                .place(Place.changeToEntity(dto.getPlace()))
                .build();
    }

}
/**
 * 개설된 맛집메이트의 공고 글 및 장소 정보를 담을 수 있는 Dto클래스입니다.
 * @author 박초은
 * @since 2024-01-05
 */
package com.fourroro.nolleogasil_backend.dto.mate;

import com.fourroro.nolleogasil_backend.dto.place.PlaceDto;
import com.fourroro.nolleogasil_backend.entity.mate.Mate;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MateDto {

    private Long mateId;        //PK //mate 공고 글 id
    private Long usersId;       //mate 공고 글 올린 사용자
    private String title;       //글 제목
    private LocalDate eatDate;  //날짜
    private String eatTime;     //시간
    private int count;          //모집할 인원수
    private String gender;      //선호하는 성별
    private String comments;    //추가로 올리고 싶은 comments
    private int display;        //공고 글의 활성화 여부(1: 활성화, 0: 비활성화) -> default값: 1
    private PlaceDto place;     //해당 장소

    //entity -> dto
    public static MateDto changeToDto(Mate entity) {
        return MateDto.builder()
                .mateId(entity.getMateId())
                .usersId(entity.getUsers().getUsersId())
                .title(entity.getTitle())
                .eatDate(entity.getEatDate())
                .eatTime(entity.getEatTime())
                .gender(entity.getGender())
                .count(entity.getCount())
                .comments(entity.getComments())
                .display(entity.getDisplay())
                .place(PlaceDto.changeToDto(entity.getPlace()))
                .build();
    }

}
/**
 * 맛집메이트의 채팅방과 장소를 위한 Dto 객체를 구성, 관리하는 클래스입니다.
 * @author 홍유리
 * @since 2024-01-05
 */
package com.fourroro.nolleogasil_backend.dto.chat;

import com.fourroro.nolleogasil_backend.dto.mate.MateDto;
import com.fourroro.nolleogasil_backend.dto.place.PlaceDto;
import com.fourroro.nolleogasil_backend.entity.chat.ChatRoom;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;


@Builder
@Getter
public class ChatRoomAndPlaceDto {

    private Long chatroomId;
    private String roomName;
    private Long usersId; //개설자
    private String nickname;
    private int maxNum;
    private int memberCnt;
    private LocalDate eatDate;  //날짜
    private String eatTime;     //시간
    private PlaceDto eatPlace; // 맛집

    public static ChatRoomAndPlaceDto ChangeToDto(ChatRoom chatRoom, PlaceDto placeDto, MateDto mateDto, int memberCnt) {
        return ChatRoomAndPlaceDto.builder()
                .chatroomId(chatRoom.getChatroomId())
                .roomName(chatRoom.getRoomName())
                .usersId(chatRoom.getUsers().getUsersId())
                .maxNum(chatRoom.getMaxNum())
                .memberCnt(memberCnt)
                .eatDate(mateDto.getEatDate())
                .eatTime(mateDto.getEatTime())
                .eatPlace(placeDto)
                .build();
    }
    public static ChatRoomAndPlaceDto createDto(ChatRoomDto chatRoomDto, PlaceDto placeDto, MateDto mateDto) {
        return ChatRoomAndPlaceDto.builder()
                .chatroomId(chatRoomDto.getChatroomId())
                .roomName(chatRoomDto.getRoomName())
                .usersId(chatRoomDto.getUsersId())
                .maxNum(chatRoomDto.getMaxNum())
                .memberCnt(chatRoomDto.getMemberCnt())
                .eatDate(mateDto.getEatDate())
                .eatTime(mateDto.getEatTime())
                .eatPlace(placeDto)
                .build();
    }


}

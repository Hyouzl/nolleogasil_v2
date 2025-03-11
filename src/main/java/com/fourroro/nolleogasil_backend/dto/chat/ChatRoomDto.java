/**
 * 맛집메이트의 채팅방을 위한 Dto 객체를 구성, 관리하는 클래스입니다.
 * @author 홍유리
 * @since 2024-01-05
 */
package com.fourroro.nolleogasil_backend.dto.chat;

import com.fourroro.nolleogasil_backend.entity.chat.ChatRoom;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatRoomDto {

    private Long chatroomId;
    private String roomName;
    private Long usersId; //개설자
    private int maxNum;
    private Long mateId; // 맛집
    private int memberCnt;

    public static ChatRoomDto ChangeToDto(ChatRoom chatRoom,int memberCnt) {
        return ChatRoomDto.builder()
                .chatroomId(chatRoom.getChatroomId())
                .roomName(chatRoom.getRoomName())
                .usersId(chatRoom.getUsers().getUsersId())
                .maxNum(chatRoom.getMaxNum())
                .mateId(chatRoom.getMate().getMateId())
                .memberCnt(memberCnt)
                .build();

    }


}

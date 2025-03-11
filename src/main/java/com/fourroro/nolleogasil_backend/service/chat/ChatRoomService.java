/**
 * 맛집메이트의 채팅방 관리를 위한 Service Interface입니다.
 * @author 홍유리
 * @since 2024-01-05
 */
package com.fourroro.nolleogasil_backend.service.chat;

import com.fourroro.nolleogasil_backend.dto.chat.ChatRoomAndPlaceDto;
import com.fourroro.nolleogasil_backend.dto.chat.ChatRoomDto;
import com.fourroro.nolleogasil_backend.dto.mate.MateDto;
import com.fourroro.nolleogasil_backend.entity.chat.ChatRoom;

import java.util.List;

public interface ChatRoomService {

    public ChatRoom getChatRoom(Long chatroomId);

    public ChatRoomAndPlaceDto getChatRoomAndPlace(Long chatroomId);
    public List<ChatRoomAndPlaceDto> getChatRoomListByMaster(Long usersId);
    public List<ChatRoomAndPlaceDto> getMyRoomsBySorted(Long usersId, String sortedBy);

    //채팅방 생성
    public ChatRoomDto createRoom(MateDto mateDto, Long usersId);

    //메이트 신청 후 수락 시 메이트 멤버를 채팅방에 넣기 위한...
    public Long getChatRoomIdByMateId(Long mateId);
    //메이트 신청할 때 메이트 멤버가 꽉 차 있으면 거절


}

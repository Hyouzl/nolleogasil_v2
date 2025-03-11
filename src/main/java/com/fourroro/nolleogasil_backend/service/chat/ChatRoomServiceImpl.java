/**
 * 맛집메이트의 채팅방 관리를 위한 Service 입니다.
 * @author 홍유리
 * @since 2024-01-05
 */
package com.fourroro.nolleogasil_backend.service.chat;


import com.fourroro.nolleogasil_backend.dto.chat.ChatRoomAndPlaceDto;
import com.fourroro.nolleogasil_backend.dto.chat.ChatRoomDto;
import com.fourroro.nolleogasil_backend.dto.mate.MateDto;
import com.fourroro.nolleogasil_backend.dto.place.PlaceDto;
import com.fourroro.nolleogasil_backend.entity.chat.ChatRoom;
import com.fourroro.nolleogasil_backend.entity.mate.Mate;
import com.fourroro.nolleogasil_backend.entity.place.Place;
import com.fourroro.nolleogasil_backend.entity.users.Users;
import com.fourroro.nolleogasil_backend.global.exception.GlobalException;
import com.fourroro.nolleogasil_backend.global.exception.ResultCode;
import com.fourroro.nolleogasil_backend.repository.chat.ChatRoomRepository;
import com.fourroro.nolleogasil_backend.service.mate.MateService;
import com.fourroro.nolleogasil_backend.service.users.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService{

    private final ChatRoomRepository chatRoomRepository;
    private final UsersService usersService;
    private final MateService mateService;

    @Override
    public ChatRoom getChatRoom(Long chatroomId) {

       ChatRoom chatRoom = chatRoomRepository.findById(chatroomId)
                .orElseThrow(() -> new GlobalException(ResultCode.NOT_FOUND_CHATROOM));

      return chatRoom;
    }

    public ChatRoomAndPlaceDto getChatRoomAndPlaceDto(ChatRoom chatRoom) {

        Mate mate = chatRoom.getMate();
        MateDto mateDto = MateDto.changeToDto(mate);
        Place place = mate.getPlace();
        PlaceDto placeDto = PlaceDto.changeToDto(place);
        int memberCnt = chatRoom.getMateMembers().size();
        System.out.println(memberCnt);
        ChatRoomAndPlaceDto chatRoomAndPlaceDto = ChatRoomAndPlaceDto.ChangeToDto(chatRoom,placeDto,mateDto,memberCnt);

        return chatRoomAndPlaceDto;

    }

    @Override
    public ChatRoomAndPlaceDto getChatRoomAndPlace(Long chatroomId) {

        ChatRoom chatRoom = this.getChatRoom(chatroomId);
        PlaceDto placeDto = mateService.getPlaceDto(chatroomId);
        MateDto mateDto = mateService.getMateBychatroom(chatroomId);
        int memberCnt = chatRoom.getMateMembers().size();
        ChatRoomDto chatRoomDto = ChatRoomDto.ChangeToDto(chatRoom,memberCnt);

        ChatRoomAndPlaceDto chatRoomAndPlaceDto = ChatRoomAndPlaceDto.createDto(chatRoomDto,placeDto,mateDto);
        return chatRoomAndPlaceDto;
    }

    @Override
    public List<ChatRoomAndPlaceDto> getChatRoomListByMaster(Long usersId) {
        System.out.println(usersId);

        List<ChatRoom> chatRoomList = chatRoomRepository.findAllByUsersUsersId(usersId);

        List<ChatRoomAndPlaceDto> chatRoomAndPlaceDtos = new ArrayList<>();

        for(ChatRoom chatRoom: chatRoomList) {
            ChatRoomAndPlaceDto chatRoomAndPlaceDto = this.getChatRoomAndPlaceDto(chatRoom);
            chatRoomAndPlaceDtos.add(chatRoomAndPlaceDto);
        }

        return chatRoomAndPlaceDtos;
    }

    @Override
    public List<ChatRoomAndPlaceDto> getMyRoomsBySorted(Long usersId, String sortedBy) {

        List<ChatRoomAndPlaceDto> chatRoomAndPlaceDtos = new ArrayList<>();
        List <ChatRoom> chatRooms = null;

        if(sortedBy.equals("최신순")) {

            chatRooms = chatRoomRepository.findAllByUsersUsersIdOrderByMateEatDateDesc(usersId);
            for(ChatRoom chatRoom: chatRooms) {
                ChatRoomAndPlaceDto chatRoomAndPlaceDto = this.getChatRoomAndPlaceDto(chatRoom);
                chatRoomAndPlaceDtos.add(chatRoomAndPlaceDto);
            }

        } else {
            chatRooms = chatRoomRepository.findAllByUsersUsersIdOrderByMate_EatDate(usersId);
            for(ChatRoom chatRoom: chatRooms) {
                ChatRoomAndPlaceDto chatRoomAndPlaceDto = this.getChatRoomAndPlaceDto(chatRoom);
                chatRoomAndPlaceDtos.add(chatRoomAndPlaceDto);
            }

        }

        return chatRoomAndPlaceDtos;
    }


    @Override
    public ChatRoomDto createRoom(MateDto mateDto, Long usersId) {

        Users users = usersService.findByUsersId(usersId);
        Mate savedMate = Mate.changeToEntity(mateDto,users);

        ChatRoom chatRoom = ChatRoom.creatChatroom(savedMate,users);
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        int memberCnt = savedChatRoom.getMateMembers().size();
        ChatRoomDto chatRoomDto = ChatRoomDto.ChangeToDto(savedChatRoom,memberCnt);

        return chatRoomDto;
    }

    @Override
    public Long getChatRoomIdByMateId(Long mateId) {

        ChatRoom chatRoom = chatRoomRepository.findByMateMateId(mateId);
        Long chatroomId = chatRoom.getChatroomId();

        return chatroomId;
    }



}

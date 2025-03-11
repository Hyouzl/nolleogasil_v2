package com.fourroro.nolleogasil_backend.controller.chat;


import com.fourroro.nolleogasil_backend.auth.jwt.util.TokenProvider;
import com.fourroro.nolleogasil_backend.dto.chat.ChatRoomAndPlaceDto;
import com.fourroro.nolleogasil_backend.service.chat.ChatRoomService;
import com.fourroro.nolleogasil_backend.service.mate.MateMemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/chatRoom")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final MateMemberServiceImpl mateMemberService;
    private final TokenProvider tokenProvider;



    @GetMapping("/{chatroomId}")
    public ChatRoomAndPlaceDto getChatRoom(@PathVariable("chatroomId") String chatroomId) {
        Long chatRoomId = Long.parseLong(chatroomId);

        ChatRoomAndPlaceDto chatRoomAndPlaceDto = chatRoomService.getChatRoomAndPlace(chatRoomId);

        return chatRoomAndPlaceDto;

    }

    @GetMapping("/myRooms")
    public List<ChatRoomAndPlaceDto> getMyRooms(@RequestParam String sortedBy, @RequestHeader("Authorization") String authorizationHeader) {
        List<ChatRoomAndPlaceDto> chatRoomAndPlaceDtoList = null;
        // 1. JWT 토큰 추출
        String token = authorizationHeader.replace("Bearer ", "");

        // 2. 토큰에서 userId 추출
        Long userId = Long.valueOf(tokenProvider.getClaims(token).getSubject());
        System.out.println(userId);

        if(sortedBy.equals("기본순")) {
            chatRoomAndPlaceDtoList = chatRoomService.getChatRoomListByMaster(userId);
        } else {
            chatRoomAndPlaceDtoList = chatRoomService.getMyRoomsBySorted(userId,sortedBy);
        }

        return chatRoomAndPlaceDtoList;
    }

    @GetMapping("/joinedRooms")
    public List<ChatRoomAndPlaceDto> getJoinedRooms(@RequestParam String sortedBy, @RequestHeader("Authorization") String authorizationHeader) {

        // 1. JWT 토큰 추출
        String token = authorizationHeader.replace("Bearer ", "");

        // 2. 토큰에서 userId 추출
        Long userId = Long.valueOf(tokenProvider.getClaims(token).getSubject());

        List<ChatRoomAndPlaceDto> chatRoomAndPlaceDtoList = null;
        if(sortedBy.equals("기본순")) {
            chatRoomAndPlaceDtoList = mateMemberService.getChatRoomListByMate(userId);
        } else {
            chatRoomAndPlaceDtoList = mateMemberService.getJoinedRoomsBySorted(userId,sortedBy);
        }

        return chatRoomAndPlaceDtoList;
    }


}
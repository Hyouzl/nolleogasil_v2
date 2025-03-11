/**
 * 이 클래스는 맛집메이트의 채팅 메세지를 DB에 저장 및 DB에서 조회, 삭제하기 위한 Repository입니다.
 * @author 홍유리
 * @since 2024-01-05
 */
package com.fourroro.nolleogasil_backend.repository.chat;

import com.fourroro.nolleogasil_backend.entity.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findChatsByChatRoomChatroomId(Long chatroomId);

}
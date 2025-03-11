/**
 * 맛집메이트 멤버의 정보를 담을 수 있는 Dto클래스입니다.
 * @author 박초은
 * @since 2024-01-05
 */
package com.fourroro.nolleogasil_backend.dto.mate;

import com.fourroro.nolleogasil_backend.entity.mate.MateMember;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MateMemberDto {

    private Long matememberId;      //PK //member의 id
    private Long usersId;           //mate 참가자의 usersId
    private MateDto mate;           //해당 mate 공고 글
    private Long chatroomID;        //해당 mate의 채팅방 id
    private int isFirst;            //채팅방에 처음 입장인지(처음입장이라면 1, 아니면 0)
    private int isGiven;            //동일 mate의 member들에게 온도를 부여했는지(default: 1, 부여했다면 0으로 변경)

    //entity -> dto
    public static MateMemberDto changeToDto(MateMember entity) {
        return MateMemberDto.builder()
                .matememberId(entity.getMatememberId())
                .usersId(entity.getUsers().getUsersId())
                .mate(MateDto.changeToDto(entity.getMate()))
                .chatroomID(entity.getChatRoom().getChatroomId())
                .isFirst(entity.getIsFirst())
                .isGiven(entity.getIsGiven())
                .build();
    }

}

/**
 * 위시(내 장소)에 저장될 정보를 담을 수 있는 Dto클래스입니다.
 * @author 박초은
 * @since 2024-01-05
 */
package com.fourroro.nolleogasil_backend.dto.users;

import com.fourroro.nolleogasil_backend.dto.place.PlaceDto;
import com.fourroro.nolleogasil_backend.entity.users.Wish;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WishDto {

    private Long wishId;        //PK
    private Long usersId;       //사용자 id
    private PlaceDto place;     //장소 id

    //entity -> dto
    public static WishDto changeToDto(Wish entity) {
        return WishDto.builder()
                .wishId(entity.getWishId())
                .usersId(entity.getUsers().getUsersId())
                .place(PlaceDto.changeToDto(entity.getPlace()))
                .build();
    }

}

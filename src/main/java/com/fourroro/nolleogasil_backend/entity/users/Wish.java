/**
 * Wish Table에 매칭되는 Entity입니다.
 * @author 박초은
 * @since 2024-01-05
 */
package com.fourroro.nolleogasil_backend.entity.users;

import com.fourroro.nolleogasil_backend.entity.place.Place;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Wish")
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishId;

    //Wish:Users = N:1 단방향 연관관계
    @ManyToOne
    @JoinColumn(name = "usersId")
    private Users users;

    //Wish:Place = 1:1 단방향 연관관계
    @OneToOne
    @JoinColumn(name = "placeId")
    private Place place;

    //dto -> entity
    public static Wish changeToEntity(Users users, Place place) {
        return Wish.builder()
                .users(users)
                .place(place)
                .build();
    }

}

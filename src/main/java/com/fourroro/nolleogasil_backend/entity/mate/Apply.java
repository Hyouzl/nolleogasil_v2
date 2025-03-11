/**
 * Apply Table에 매칭되는 Entity클래스입니다.
 * @author 박초은
 * @since 2024-01-05
 */
package com.fourroro.nolleogasil_backend.entity.mate;

import com.fourroro.nolleogasil_backend.dto.mate.ApplyDto;
import com.fourroro.nolleogasil_backend.dto.mate.ApplyStatus;
import com.fourroro.nolleogasil_backend.entity.users.Users;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Apply")
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Apply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applyId;

    //Mate:Apply = 1:N 단방향 연관관계
    @ManyToOne
    @JoinColumn(name = "mateId")
    private Mate mate;

    //Users:Apply = 1:N 단방향 연관관계
    @ManyToOne
    @JoinColumn(name = "applicantId")
    private Users users;

    @Enumerated(EnumType.STRING)
    private ApplyStatus isApply;    //신청 상태("대기", "수락", "거절" 中 1)

    //dto -> entity
    public static Apply changeToEntity(ApplyDto dto, Mate mate, Users users) {
        return Apply.builder()
                .mate(mate)
                .users(users)
                .isApply(dto.getIsApply())
                .build();
    }

}

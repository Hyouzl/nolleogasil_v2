package com.fourroro.nolleogasil_backend.entity.travelpath;

import com.fourroro.nolleogasil_backend.dto.travelpath.TravelDateDto;
import jakarta.persistence.*;
import lombok.*;
/**
 * Traveldate Table에 매칭되는 Entity입니다.
 * @author 전선민
 * @since 2024-01-10
 */
@Entity
@Table(name="Traveldate")
@Builder
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //기본 생성자
@AllArgsConstructor //전체 변수 생성하는 생성자
public class TravelDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long traveldateId;              //PK

    private String dates;                   //여행 일자

    @ManyToOne
    @JoinColumn(name="recommendationId")
    private Recommendation recommendation;  //Recommendation

    public static TravelDate changeToEntity(TravelDateDto dto, Recommendation recommendation) {
        return TravelDate.builder()
                .dates(dto.getDates())
                .recommendation(recommendation)
                .build();
    }


}

package com.fourroro.nolleogasil_backend.entity.travelpath;


import com.fourroro.nolleogasil_backend.dto.travelpath.TravelInfoDto;
import jakarta.persistence.*;
import lombok.*;
/**
 * TravelInfo Table에 매칭되는 Entity입니다.
 * @author 전선민
 * @since 2024-01-10
 */
@Entity
@Table(name="Travelinfo")
@Builder
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //기본 생성자
@AllArgsConstructor //전체 변수 생성하는 생성자
public class TravelInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long travelinfoId;              //PK

    private String infos;                   //여행 정보

    @ManyToOne
    @JoinColumn(name="recommendationId")
    private Recommendation recommendation;  //Recommendation

    public static TravelInfo changeToEntity(TravelInfoDto dto, Recommendation recommendation) {
        if (dto.getTravelinfoId() != null) { // 이미 travelInfoId가 존재하는 경우
            return TravelInfo.builder()
                    .travelinfoId(dto.getTravelinfoId()) // 기존의 travelInfoId 사용
                    .infos(dto.getInfos())
                    .recommendation(recommendation)
                    .build();
        } else {                            // travelInfoId가 존재하지 않는 경우
            return TravelInfo.builder()
                    .infos(dto.getInfos())
                    .recommendation(recommendation)
                    .build();
        }
    }
}

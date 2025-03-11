package com.fourroro.nolleogasil_backend.entity.travelpath;

import com.fourroro.nolleogasil_backend.dto.travelpath.KeywordDto;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
/**
 * Keyword Table에 매칭되는 Entity입니다.
 * @author 전선민
 * @since 2024-01-10
 */
@Entity
@Table(name="Keyword")
@Builder
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //기본 생성자
@AllArgsConstructor //전체 변수 생성하는 생성자
public class Keyword implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long keywordId;            //PK

    private String party;              //일행
    private String place;              //장소
    private String concept;            //컨셉
    private String food;               //음식

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travelpathId")
    private TravelPath travelPath;     //TravelPath

    public static Keyword changeToEntity(KeywordDto dto, TravelPath travelPath) {
        return Keyword.builder()
                .party(dto.getParty())
                .place(dto.getPlace())
                .concept(dto.getConcept())
                .food(dto.getFood())
                .travelPath(travelPath)
                .build();
    }

}

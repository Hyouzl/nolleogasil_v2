package com.fourroro.nolleogasil_backend.entity.travelpath;
import com.fourroro.nolleogasil_backend.dto.travelpath.TravelPathDto;
import com.fourroro.nolleogasil_backend.entity.users.Users;
import jakarta.persistence.*;
import lombok.*;
/**
 * Travelpath Table에 매칭되는 Entity입니다.
 * @author 전선민
 * @since 2024-01-10
 */

@Entity
@Table(name="Travelpath")
@Builder
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC) //기본 생성자
@AllArgsConstructor //전체 변수 생성하는 생성자
public class TravelPath {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long travelpathId;            //PK
    private String arrival;               //도착지
    private String startDate;             //출발 날짜
    private String endDate;		          //오는 날짜

    @ManyToOne
    @JoinColumn(name = "usersId")
    private Users users;    		      //사용자 entity

    @OneToOne(mappedBy = "travelPath",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Keyword keyword;              // TravelPath와의 OneToOne 관계

    @OneToOne(mappedBy = "travelPath",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Recommendation recommendation; // TravelPath와의 OneToOne 관계

    public static TravelPath changeToEntity(TravelPathDto dto, Users users) {
        return TravelPath.builder()
                .arrival(dto.getArrival())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .users(users)
                .build();

    }

}

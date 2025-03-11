package com.fourroro.nolleogasil_backend.repository.travelpath;

import com.fourroro.nolleogasil_backend.entity.travelpath.Keyword;
import com.fourroro.nolleogasil_backend.entity.travelpath.TravelDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * {@link TravelDate} 엔티티와 관련된 데이터 작업을 처리하는 리포지토리 인터페이스입니다.
 * @author 전선민
 * @since 2024-01-10
 */
@Repository
public interface TravelDateRepository extends JpaRepository<TravelDate, Long> {


}

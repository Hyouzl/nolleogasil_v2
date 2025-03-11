package com.fourroro.nolleogasil_backend.repository.place;

import com.fourroro.nolleogasil_backend.entity.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * {@link Place}엔티티와 관련된 데이터 작업을 처리하는 Repository interface입니다.
 * @author 박초은
 * @since 2024-01-10
 */

public interface PlaceRepository extends JpaRepository<Place, Long> {
    //save()(=persist())
    //existsById()
}

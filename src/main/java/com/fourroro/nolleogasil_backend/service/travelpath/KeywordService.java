package com.fourroro.nolleogasil_backend.service.travelpath;

import com.fourroro.nolleogasil_backend.dto.travelpath.KeywordDto;
import com.fourroro.nolleogasil_backend.entity.travelpath.TravelPath;
import org.springframework.stereotype.Service;
import com.fourroro.nolleogasil_backend.entity.travelpath.Keyword;
/**
 * {@link Keyword} 엔티티를 관리하기 위한 서비스 인터페이스입니다.
 * @author 전선민
 * @since 2024-01-10
 */
@Service
public interface KeywordService {

    /** Keyword 추가 */
    public void insertKeyword (TravelPath travelPath, KeywordDto keywordDto);
}

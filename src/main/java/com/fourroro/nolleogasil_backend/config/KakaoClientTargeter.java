package com.fourroro.nolleogasil_backend.config;

import feign.Feign;
import feign.Target;
import org.springframework.cloud.openfeign.FeignClientFactoryBean;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.cloud.openfeign.Targeter;

/**
 * 이 클래스는 Feign Client를 위한 클래스입니다.
 * @author 장민정
 * @since 2024-01-05
 */
public class KakaoClientTargeter implements Targeter {
    public <T> T target(Target<T> target, Feign.Builder feign) {
        return feign.target(target);
    }

    @Override
    public <T> T target(FeignClientFactoryBean factory, Feign.Builder feign, FeignContext context, Target.HardCodedTarget<T> target) {
        return feign.target(target);
    }
}
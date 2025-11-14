package jm.diamond.config.session;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession(redisNamespace = "spring:session:diamond") // springSessionRepositoryFilter 라는 Filter 빈
//@EnableRedisIndexedHttpSession  // 다양한 기준으로 세션을 검색해야 하는 시스템에 적합.
public class RedisConfig {

}

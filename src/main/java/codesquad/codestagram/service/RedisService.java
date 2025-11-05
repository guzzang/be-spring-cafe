package codesquad.codestagram.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisService {

    private final Long requestExpireDurationSec = 1800L;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean checkFirstRequest(String clientIp, Long articleId) {
        String key = generateKey(clientIp, articleId);
        if(redisTemplate.hasKey(key)) {
            return false;
        }
        return true;
    }

    public void writeClientRequest(String clientIp, Long articleId) {
        String key = generateKey(clientIp, articleId);
        redisTemplate.opsForValue().set(key, true, Duration.ofSeconds(requestExpireDurationSec));
    }

    private String generateKey(String clientIp, Long articleId) {
        return clientIp + ":" + articleId;
    }


}

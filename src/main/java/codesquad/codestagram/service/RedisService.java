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

    public boolean checkFirstRequest(String userId, Long articleId) {
        String key = generateKey(userId, articleId);
        if(redisTemplate.hasKey(key)) {
            return false;
        }
        return true;
    }

    public void writeClientRequest(String userId, Long articleId) {
        String key = generateKey(userId, articleId);
        redisTemplate.opsForValue().set(key, true, Duration.ofSeconds(requestExpireDurationSec));
    }

    private String generateKey(String userId, Long articleId) {
        return userId + ":" + articleId;
    }


}

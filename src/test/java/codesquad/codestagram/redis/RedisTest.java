package codesquad.codestagram.redis;

import codesquad.codestagram.config.EmbeddedRedisConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Import(EmbeddedRedisConfig.class)
@ActiveProfiles("local")
public class RedisTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void redisConnectionTest(){
        String key = "a";
        String value = "1";

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);

        String expectValue = valueOperations.get(key);
        Assertions.assertThat(expectValue).isEqualTo(value);
    }

}

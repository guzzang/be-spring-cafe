package codesquad.codestagram.redis;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
public class RedisTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void redisConnectionTest(){
        String key = "b";
        String value = "2";

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);

        String expectValue = valueOperations.get(key);
        Assertions.assertThat(expectValue).isEqualTo(value);
    }

}

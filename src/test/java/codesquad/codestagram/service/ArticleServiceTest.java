package codesquad.codestagram.service;

import codesquad.codestagram.config.EmbeddedRedisConfig;
import codesquad.codestagram.repository.ArticleRecommendRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@Import(EmbeddedRedisConfig.class)
@ActiveProfiles("local")
public class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRecommendRepository articleRecommendRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    void clearRedis() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    @DisplayName("게시글에 대한 첫번째 조회 요청 후에는 조회 기록이 남아야 한다.")
    void SearchArticleFirstTime() {
        //given
        //when
        articleService.findSingleArticle(1L, "111.111.111.111");
        //then
        Assertions.assertThat(redisTemplate.opsForValue().get("111.111.111.111:1")).isEqualTo("true");
        Assertions.assertThat(redisTemplate.hasKey("111.111.111.111:1")).isEqualTo(true);
    }

    @Test
    @DisplayName("게시글에 대한 동일 ip의 중복된 조회 요청 후에는 하나의 조회 기록만 남아야 한다.")
    void SearchSameArticle() {
        //given
        //when
        articleService.findSingleArticle(1L, "111.111.111.111");
        articleService.findSingleArticle(1L, "111.111.111.111");
        //then
        Assertions.assertThat(articleService.findById(1L).getReadCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("게시글에 대한 다른 ip의 조회 요청 후에는 각각의 조회 기록이 남아야 한다.")
    void SearchArticleByDifferentIp() {
        //given
        //when
        articleService.findSingleArticle(1L, "000.000.000.000");
        articleService.findSingleArticle(1L, "111.111.111.111");
        //then
        Assertions.assertThat(redisTemplate.opsForValue().get("000.000.000.000:1")).isEqualTo("true");
        Assertions.assertThat(redisTemplate.opsForValue().get("111.111.111.111:1")).isEqualTo("true");
        Assertions.assertThat(redisTemplate.hasKey("000.000.000.000:1")).isEqualTo(true);
        Assertions.assertThat(redisTemplate.hasKey("111.111.111.111:1")).isEqualTo(true);
        Assertions.assertThat(articleService.findById(1L).getReadCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("게시글에 대한 첫 추천은 추천 기록이 남아야 한다.")
    void recommendArticleFirstTime() {
        //given
        //when
        articleService.recommendArticle(1L, 1L);
        //then
        Assertions.assertThat(articleRecommendRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("게시글에 대해 추천한 유저가 동일 게시글을 다시 추천하면 에러가 발생해야 한다.")
    void sameUserRecommendSameArticle() {
        //given
        //when & then
        articleService.recommendArticle(1L, 1L);
        assertThrows(IllegalStateException.class, () -> articleService.recommendArticle(1L, 1L));
    }



}

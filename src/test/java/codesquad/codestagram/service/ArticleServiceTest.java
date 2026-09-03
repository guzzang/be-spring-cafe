package codesquad.codestagram.service;

import codesquad.codestagram.entity.Article;
import codesquad.codestagram.entity.User;
import codesquad.codestagram.repository.ArticleRecommendRepository;
import codesquad.codestagram.repository.ArticleRepository;
import codesquad.codestagram.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("local")
public class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRecommendRepository articleRecommendRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private User testUser1;
    private User testUser2;
    private Article article;

    @BeforeEach
    void setUp(){
        //given
        testUser1 = userRepository.save(new User("testId", "testUser", "1234", "test@test.com"));
        testUser2 = userRepository.save(new User("testId2", "testUser2", "12345", "test2@test.com"));
        article = articleRepository.save(new Article(testUser1.getName(), "testTitle", "testContents", testUser1));
    }

    @BeforeEach
    void clearRedis() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    @DisplayName("로그인하지 않은 유저의 게시글 조회는 조회수가 증가하지 않아야 한다.")
    void SearchArticleByNotLoginUser(){
        //when
        articleService.findSingleArticle(article.getId(), null);
        //then
        Assertions.assertThat(article.getReadCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("게시글에 대한 첫번째 조회 요청 후에는 조회 기록이 남아야 하며 조회수가 하나 증가해야 한다.")
    void SearchArticleFirstTime() {
        //when
        articleService.findSingleArticle(article.getId(), testUser1.getUserId());
        //then
        Assertions.assertThat(redisTemplate.hasKey(testUser1.getUserId() + ":" + article.getId())).isEqualTo(true);
        Assertions.assertThat(articleService.findById(article.getId()).getReadCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("동일 게시글에 대한 동일 사용자의 중복된 조회 요청 후에는 하나의 조회 기록만 남아야 하며 조회수가 하나만 증가해야 한다.")
    void SearchSameArticle() {
        //when
        articleService.findSingleArticle(article.getId(), testUser1.getUserId());
        articleService.findSingleArticle(article.getId(), testUser1.getUserId());
        //then
        Assertions.assertThat(articleService.findById(article.getId()).getReadCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("동일 게시글에 대한 다른 사용자의 조회 요청 후에는 각각의 조회 기록이 남아야 한다.")
    void SearchArticleByDifferentIp() {
        //when
        articleService.findSingleArticle(article.getId(), testUser1.getUserId());
        articleService.findSingleArticle(article.getId(), testUser2.getUserId());
        //then
        Assertions.assertThat(redisTemplate.hasKey(testUser1.getUserId() + ":" + article.getId())).isEqualTo(true);
        Assertions.assertThat(redisTemplate.hasKey(testUser2.getUserId() + ":" + article.getId())).isEqualTo(true);
        Assertions.assertThat(articleService.findById(article.getId()).getReadCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("게시글에 대한 첫 추천은 추천 기록이 남아야 한다.")
    void recommendArticleFirstTime() {
        //when
        articleService.recommendArticle(testUser1.getId(), article.getId());
        //then
        Assertions.assertThat(articleRecommendRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("게시글에 대해 추천한 유저가 동일 게시글을 다시 추천하면 에러가 발생해야 한다.")
    void sameUserRecommendSameArticle() {
        //when & then
        articleService.recommendArticle(testUser1.getId(), article.getId());
        assertThrows(IllegalStateException.class, () -> articleService.recommendArticle(testUser1.getId(), article.getId()));
    }



}

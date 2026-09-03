package codesquad.codestagram.service;

import codesquad.codestagram.dto.RequestArticleDto;
import codesquad.codestagram.dto.ResponseArticleDto;
import codesquad.codestagram.entity.Article;
import codesquad.codestagram.entity.ArticleRecommend;
import codesquad.codestagram.entity.User;
import codesquad.codestagram.repository.ArticleRecommendRepository;
import codesquad.codestagram.repository.ArticleRepository;
import codesquad.codestagram.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleRecommendRepository articleRecommendRepository;
    private final RedisService redisService;

    public ArticleService(RedisService redisService, ArticleRepository articleRepository, UserRepository userRepository, ArticleRecommendRepository articleRecommendRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.articleRecommendRepository = articleRecommendRepository;
        this.redisService = redisService;
    }

    public void save(RequestArticleDto requestArticleDto) {
        User user = userRepository.findById(requestArticleDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을 수 없습니다."));
        Article article = requestArticleDto.toArticle(user);
        articleRepository.save(article);
    }

    public void edit(Long articleId, RequestArticleDto requestArticleDto) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글을 찾을 수 없습니다."));
        article.setTitle(requestArticleDto.getTitle());
        article.setContents(requestArticleDto.getContents());
    }

    public ResponseArticleDto findSingleArticle(Long articleId, String userId){
        Article searchedArticle = articleRepository.findById(articleId).orElseThrow(() -> new EntityNotFoundException("해당 질문을 찾을 수 없습니다."));
        Long recommendCount = articleRecommendRepository.countByArticleId(searchedArticle.getId());

        if(userId != null && redisService.checkFirstRequest(userId, searchedArticle.getId())){
            searchedArticle.increaseReadCount();
            redisService.writeClientRequest(userId, searchedArticle.getId());
        }

        return ResponseArticleDto.of(searchedArticle, recommendCount);
    }

    public Page<Article> findAll(int page) {
        Pageable pageable = PageRequest.of(page, 15, Sort.by("createdAt").descending());

        return articleRepository.findAll(pageable);
    }

    public Article findById(Long articleId){
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("해당 질문을 찾을 수 없습니다."));
    }

    public void delete(Article article) {
        articleRepository.delete(article);
    }

    public void recommendArticle(Long userId, Long articleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("등록되어있지 않은 유저입니다."));
        Article article = findById(articleId);
        articleRecommendRepository.findByUserIdAndArticleId(user.getId(), article.getId())
                .ifPresent(i -> {throw new IllegalStateException("이미 추천한 게시글입니다.");});

        ArticleRecommend articleRecommend = new ArticleRecommend(user, article);
        articleRecommendRepository.save(articleRecommend);
    }
}

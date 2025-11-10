package codesquad.codestagram.repository;

import codesquad.codestagram.entity.ArticleRecommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRecommendRepository extends JpaRepository<ArticleRecommend, Long> {

    @Query("select count(ar.id) from ArticleRecommend ar where ar.article.id = :articleId")
    Long countByArticleId(@Param("articleId") Long articleId);

    @Query("select ar from ArticleRecommend ar where ar.user.id=:userId and ar.article.id=:articleId")
    Optional<ArticleRecommend> findByUserIdAndArticleId(@Param("userId") Long userId, @Param("articleId") Long articleId);

}

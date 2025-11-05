package codesquad.codestagram.dto;

import codesquad.codestagram.entity.Article;

public class ResponseArticleDto {

    private Article article;

    private Long recommendCount;

    public ResponseArticleDto(Article article, Long recommendCount) {
        this.article = article;
        this.recommendCount = recommendCount;
    }

    public Article getArticle() {
        return article;
    }

    public Long getRecommendCount() {
        return recommendCount;
    }

    public static ResponseArticleDto of(Article article, Long recommendCount) {
        return new ResponseArticleDto(article, recommendCount);
    }


}

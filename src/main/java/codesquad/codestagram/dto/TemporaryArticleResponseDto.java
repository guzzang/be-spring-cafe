package codesquad.codestagram.dto;

import codesquad.codestagram.entity.TemporaryArticle;

import java.util.List;

public class TemporaryArticleResponseDto {

    private List<TemporaryArticle> temporaryArticles;

    public TemporaryArticleResponseDto(List<TemporaryArticle> temporaryArticles) {
        this.temporaryArticles = temporaryArticles;
    }

    public List<TemporaryArticle> getTemporaryArticles() {
        return temporaryArticles;
    }

    public static TemporaryArticleResponseDto of(List<TemporaryArticle> temporaryArticles) {
        return new TemporaryArticleResponseDto(temporaryArticles);
    }


}

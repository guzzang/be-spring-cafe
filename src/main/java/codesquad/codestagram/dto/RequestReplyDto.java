package codesquad.codestagram.dto;

import codesquad.codestagram.controller.Article;
import codesquad.codestagram.controller.Reply;
import codesquad.codestagram.controller.User;

public class RequestReplyDto {

    private String contents;

    private Long userId;

    private Long articleId;

    public RequestReplyDto(String contents, Long userId, Long articleId) {
        this.contents = contents;
        this.userId = userId;
        this.articleId = articleId;
    }

    public String getContents() {
        return contents;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public Reply toReply(User user, Article article){
        return new Reply(article, user, contents);
    }
}

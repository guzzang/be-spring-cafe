package codesquad.codestagram.entity;

import codesquad.codestagram.config.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
public class ArticleRecommend extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ARTICLE_ID")
    private Article article;

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Article getArticle() {
        return article;
    }

    public ArticleRecommend(User user, Article article) {
        this.user = user;
        this.article = article;
    }

    protected ArticleRecommend() {}

}

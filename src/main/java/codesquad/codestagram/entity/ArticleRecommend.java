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


}

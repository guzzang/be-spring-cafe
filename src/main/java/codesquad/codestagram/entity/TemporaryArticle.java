package codesquad.codestagram.entity;

import codesquad.codestagram.config.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
public class TemporaryArticle extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

}

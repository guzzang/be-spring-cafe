package codesquad.codestagram.entity;

import codesquad.codestagram.config.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
public class TemporaryArticle extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    public TemporaryArticle(String writer, String title, String contents, User user) {
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.user = user;
    }

    protected TemporaryArticle() {

    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public void edit(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}

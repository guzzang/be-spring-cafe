package codesquad.codestagram.entity;


import codesquad.codestagram.config.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
public class Article extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long readCount;

    public Article(String writer, String title, String contents, User user) {
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.user = user;
        this.readCount = 0L;
    }

    protected Article() {}

    public Long getId() {return id;}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {this.user = user;}

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Long getReadCount() {return readCount;}

    public void setReadCount(Long readCount) {this.readCount = readCount;}

    public void increaseReadCount() {
        readCount++;
    }
}

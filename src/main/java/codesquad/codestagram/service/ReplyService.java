package codesquad.codestagram.service;

import codesquad.codestagram.entity.Article;
import codesquad.codestagram.entity.Reply;
import codesquad.codestagram.entity.User;
import codesquad.codestagram.dto.RequestReplyDto;
import codesquad.codestagram.repository.ArticleRepository;
import codesquad.codestagram.repository.ReplyRepository;
import codesquad.codestagram.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;


    public ReplyService(ReplyRepository replyRepository, UserRepository userRepository, ArticleRepository articleRepository) {
        this.replyRepository = replyRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    public void save(RequestReplyDto requestReplyDto) {
        User user = userRepository.findById(requestReplyDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을 수 없습니다."));
        Article article = articleRepository.findById(requestReplyDto.getArticleId())
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글을 찾을 수 없습니다."));
        Reply reply = requestReplyDto.toReply(user, article);
        replyRepository.save(reply);
    }

    public Reply findByReplyId(Long replyId) {
        return replyRepository.findById(replyId)
                .orElseThrow(() -> new EntityNotFoundException("해당 댓글을 찾을 수 없습니다."));
    }

    public void delete(Reply reply){
        replyRepository.delete(reply);
    }

    public Page<Reply> findByArticleId(Long articleId, int page) {
        PageRequest pageable = PageRequest.of(page, 5, Sort.by("createdAt").descending());
        return replyRepository.findByArticleId(articleId, pageable);
    }
}

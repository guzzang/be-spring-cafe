package codesquad.codestagram.service;

import codesquad.codestagram.controller.Article;
import codesquad.codestagram.controller.Reply;
import codesquad.codestagram.controller.User;
import codesquad.codestagram.dto.RequestReplyDto;
import codesquad.codestagram.repository.ArticleRepository;
import codesquad.codestagram.repository.ReplyRepository;
import codesquad.codestagram.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new EntityNotFoundException("해당 댓글을 찾을 수 없습니다."));
        return reply;
    }

    public void delete(Reply reply){
        replyRepository.delete(reply);
    }

    public List<Reply> findByArticleId(Long articleId) {
        return replyRepository.findByArticleId(articleId);
    }
}

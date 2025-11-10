package codesquad.codestagram.service;

import codesquad.codestagram.dto.RequestArticleDto;
import codesquad.codestagram.dto.TemporaryArticleResponseDto;
import codesquad.codestagram.entity.TemporaryArticle;
import codesquad.codestagram.entity.User;
import codesquad.codestagram.repository.TemporaryArticleRepository;
import codesquad.codestagram.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TemporaryArticleService {

    private final TemporaryArticleRepository temporaryArticleRepository;
    private final UserRepository userRepository;

    public TemporaryArticleService(TemporaryArticleRepository temporaryArticleRepository, UserRepository userRepository) {
        this.temporaryArticleRepository = temporaryArticleRepository;
        this.userRepository = userRepository;
    }

    public void save(RequestArticleDto requestArticleDto) {
        User user = userRepository.findById(requestArticleDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을 수 없습니다."));
        TemporaryArticle temporaryArticle = requestArticleDto.toTemporaryArticle(user);
        temporaryArticleRepository.save(temporaryArticle);
    }

    @Transactional(readOnly = true)
    public TemporaryArticleResponseDto findTemporaryArticles(Long userId) {
        List<TemporaryArticle> temporaryArticles = temporaryArticleRepository.findAllByUserId(userId);
        return TemporaryArticleResponseDto.of(temporaryArticles);
    }

    @Transactional(readOnly = true)
    public TemporaryArticle findById(Long id) {
        return temporaryArticleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 저장글을 찾을 수 없습니다."));
    }

    public void edit(Long temporaryArticleId, RequestArticleDto editArticleInfo) {
        TemporaryArticle temporaryArticle = findById(temporaryArticleId);
        temporaryArticle.edit(editArticleInfo.getTitle(), editArticleInfo.getContents());
    }

    public void delete(TemporaryArticle temporaryArticle) {
        temporaryArticleRepository.delete(temporaryArticle);
    }




}

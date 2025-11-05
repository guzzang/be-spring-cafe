package codesquad.codestagram.repository;

import codesquad.codestagram.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    Optional<Reply> findById(Long id);

    Page<Reply> findByArticleId(Long articleId, Pageable pageable);


}

package codesquad.codestagram.repository;

import codesquad.codestagram.controller.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    Optional<Reply> findById(Long id);

    List<Reply> findByArticleId(Long id);


}

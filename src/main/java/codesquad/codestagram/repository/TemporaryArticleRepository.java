package codesquad.codestagram.repository;

import codesquad.codestagram.entity.TemporaryArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemporaryArticleRepository extends JpaRepository<TemporaryArticle, Long> {

}

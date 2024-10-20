package is.hi.hbv501g.vibe.Persistance.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import is.hi.hbv501g.vibe.Persistance.Entities.Comment;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    Optional<Comment> findById(Long id);
    
}

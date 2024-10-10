package is.hi.hbv501g.vibe.Persistance.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import is.hi.hbv501g.vibe.Persistance.Entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    
}

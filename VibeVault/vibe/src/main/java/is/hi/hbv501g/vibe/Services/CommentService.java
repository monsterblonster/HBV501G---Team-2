package is.hi.hbv501g.vibe.Services;

import java.util.List;
import java.util.Optional;

import is.hi.hbv501g.vibe.Persistance.Entities.Comment;

public interface CommentService {
    Comment save(Comment comment);
    void delete(Comment comment);
    List<Comment> findAll();
    List<Comment> findByEventId(Long eventId);
}

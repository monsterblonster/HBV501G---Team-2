package is.hi.hbv501g.vibe.Services.Implementation;

import is.hi.hbv501g.vibe.Persistance.Entities.Comment;
import org.hibernate.annotations.DialectOverride.OverridesAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import is.hi.hbv501g.vibe.Persistance.Repositories.CommentRepository;
import is.hi.hbv501g.vibe.Services.CommentService;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImplementation implements CommentService {
    private CommentRepository commentRepository;

    @Autowired
    public CommentServiceImplementation(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public Optional<Comment> findByEventId(Long id) {
        return commentRepository.findById(id);
    }
    
}

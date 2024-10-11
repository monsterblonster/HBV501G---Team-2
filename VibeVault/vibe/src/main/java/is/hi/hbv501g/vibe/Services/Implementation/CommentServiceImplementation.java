package is.hi.hbv501g.vibe.Services.Implementation;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import is.hi.hbv501g.vibe.Persistance.Repositories.CommentRepository;
import is.hi.hbv501g.vibe.Services.CommentService;

@Service
public class CommentServiceImplementation implements CommentService {
    private CommentRepository commentRepository;

    // @Autowired
    public CommentServiceImplementation(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    
}

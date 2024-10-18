package is.hi.hbv501g.vibe.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import is.hi.hbv501g.vibe.Persistance.Entities.Comment;
import is.hi.hbv501g.vibe.Services.CommentService;
import is.hi.hbv501g.vibe.Services.GroupService;
import is.hi.hbv501g.vibe.Services.UserService;

@Controller
@RequestMapping("/comments")
public class ChatController {

    private final CommentService commentService;


    @Autowired
    public ChatController(CommentService commentService) {
        this.commentService = commentService;
    }
    
}

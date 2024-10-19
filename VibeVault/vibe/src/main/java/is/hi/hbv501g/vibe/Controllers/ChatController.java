package is.hi.hbv501g.vibe.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import is.hi.hbv501g.vibe.Persistance.Entities.Comment;
import is.hi.hbv501g.vibe.Services.CommentService;
import org.springframework.ui.Model;
import is.hi.hbv501g.vibe.Services.GroupService;
import is.hi.hbv501g.vibe.Services.UserService;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/comments")
public class ChatController {

    private final CommentService commentService;


    @Autowired
    public ChatController(CommentService commentService) {
        this.commentService = commentService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/comment")
    public String commentViewPost(@ModelAttribute("comment") Comment comment, Model model){

        commentService.save(comment);

        model.addAttribute("comment", new Comment());

        model.addAttribute("comment", commentService.findAll());

        return "comments";
    }


}

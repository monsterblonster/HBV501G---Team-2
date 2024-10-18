package is.hi.hbv501g.vibe.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import is.hi.hbv501g.vibe.Persistance.Entities.Comment;
import is.hi.hbv501g.vibe.Services.CommentService;

@Controller
@RequestMapping("/chats")
public class ChatController {

    @RequestMapping("/")
    public String chatPage(){
        return "chat";
    }
}

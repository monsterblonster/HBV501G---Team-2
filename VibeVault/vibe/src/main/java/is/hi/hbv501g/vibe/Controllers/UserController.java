package is.hi.hbv501g.vibe.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {
    
    @RequestMapping("/User")
    public String userPage() {
        return "user";
    }
}

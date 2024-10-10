package is.hi.hbv501g.vibe.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GroupController {

    @RequestMapping("/Group")
    public String groupPage() {
        return "group";
    }
}

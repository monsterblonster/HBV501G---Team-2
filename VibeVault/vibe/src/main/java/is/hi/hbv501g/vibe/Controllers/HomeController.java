package is.hi.hbv501g.vibe.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;


@Controller
@RequestMapping("/")
public class HomeController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showHomePage() {
        return "home"; 
    }
}

package is.hi.hbv501g.vibe.Controllers;

//import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @RequestMapping("/")
    public String homePage(){
        return "home";
    }
}

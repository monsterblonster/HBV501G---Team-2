package is.hi.hbv501g.vibe.Controllers;

//import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import is.hi.hbv501g.vibe.Persistance.Entities.*;
import is.hi.hbv501g.vibe.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {

    private final UserService userService;
    private final GroupService groupService;
    private final InvitationService invitationService;
    private final EventService eventService;

    @Autowired
    public HomeController(UserService userService, 
			InvitationService invitationService, 
			EventService eventService, 
			GroupService groupService
			) {
        this.userService = userService;
        this.invitationService = invitationService;
				this.eventService = eventService;
				this.groupService = groupService;
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage(){
        return "login";
				
    }


}

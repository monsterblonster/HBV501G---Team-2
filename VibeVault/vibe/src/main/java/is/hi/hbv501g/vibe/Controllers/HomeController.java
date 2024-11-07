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

import is.hi.hbv501g.vibe.Persistance.Entities.Invitation;
import is.hi.hbv501g.vibe.Persistance.Entities.User;
import is.hi.hbv501g.vibe.Persistance.Entities.Group;
import is.hi.hbv501g.vibe.Services.InvitationService;
import is.hi.hbv501g.vibe.Services.UserService;
import is.hi.hbv501g.vibe.Services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


@RestController
public class HomeController {
	// ekki spurja ég hef ekki hugmynd, sleppa home page kóði
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<Void> homePage() {
    // Explicitly redirect to /login
    return ResponseEntity.status(HttpStatus.FOUND)
                         .header("Location", "/calendar") // breyta default landing page hér
                         .build();
} 
    @RequestMapping(value = "/calendar", method = RequestMethod.GET)
    public String showCalendarPage() {
        return "calendar";  // The name of your HTML template (calendar.html)
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public String handleFormSubmission(@RequestParam("datetime") String datetime) {
        // You can process the selected datetime here
        System.out.println("Selected Date and Time: " + datetime);
        return "redirect:/calendar";  // Redirect back to the calendar page (or a different page)
    }


}

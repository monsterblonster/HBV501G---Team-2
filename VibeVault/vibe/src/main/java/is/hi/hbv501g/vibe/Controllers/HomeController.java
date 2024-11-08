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
    return ResponseEntity.status(HttpStatus.FOUND)
                         .header("Location", "/login") // breyta default landing page hér
                         .build();
} 

}

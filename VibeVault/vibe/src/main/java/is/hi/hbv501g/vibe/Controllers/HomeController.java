package is.hi.hbv501g.vibe.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

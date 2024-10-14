package is.hi.hbv501g.vibe.Controllers;

import is.hi.hbv501g.vibe.Persistance.Entities.User;
import is.hi.hbv501g.vibe.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Registration Page (GET)
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        System.out.println("Username: " + user.getUserName());
        System.out.println("Password: " + user.getUserPW());

        if (user.getUserName() == null || user.getUserName().isEmpty() ||
                user.getUserPW() == null || user.getUserPW().isEmpty()) {
            model.addAttribute("error", "Username and Password cannot be empty.");
            return "register";
        }

        userService.registerUser(user.getUserName(), user.getUserPW());
        return "redirect:/registration-confirmation?username=" + user.getUserName();
    }

    // Confirmation Page
    @RequestMapping(value = "/registration-confirmation", method = RequestMethod.GET)
    public String showRegistrationConfirmation(@RequestParam("username") String username, Model model) {
        model.addAttribute("username", username);
        return "registration-confirmation";
    }

    // Login Page
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    // Handling Login (POST)
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginUser(@ModelAttribute("user") User user, Model model) {
        User dbUser = userService.findUserByUsername(user.getUserName()).orElse(null);

        if (dbUser != null && dbUser.getUserPW().equals(user.getUserPW())) {
            model.addAttribute("message", "Login successful!");
            return "welcome"; // Redirect to a welcome page
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "login"; // Stay on login page with error
        }
    }
}
package is.hi.hbv501g.vibe.Controllers;

import is.hi.hbv501g.vibe.Persistance.Entities.User;
import is.hi.hbv501g.vibe.Persistance.Entities.Group;
import is.hi.hbv501g.vibe.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Set;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


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
        return "redirect:/profile?username=" + user.getUserName();
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginUser(@ModelAttribute("user") User user, Model model) {
        User dbUser = userService.findUserByUsername(user.getUserName()).orElse(null);

        if (dbUser != null && dbUser.getUserPW().equals(user.getUserPW())) {
            model.addAttribute("message", "Login successful!");
            return "redirect:/profile?username=" + user.getUserName();
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String showUserProfile(Model model, @RequestParam("username") String username) {
        User user = userService.findUserByUsername(username).orElse(null);
        if (user != null) {
            model.addAttribute("user", user);
            return "profile";
        } else {
            model.addAttribute("error", "User not found");
            return "profile";
        }
    }

    @RequestMapping(value = "/create-group", method = RequestMethod.GET)
    public String showCreateGroupForm(Model model, @RequestParam("username") String username) {
        User dbUser = userService.findUserByUsername(username).orElse(null);
        model.addAttribute("user", dbUser);
        model.addAttribute("group", new Group());
        return "group_create";
    }

}
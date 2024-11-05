package is.hi.hbv501g.vibe.Controllers;

import is.hi.hbv501g.vibe.Persistance.Entities.User;
import is.hi.hbv501g.vibe.Persistance.Entities.Group;
import is.hi.hbv501g.vibe.Services.UserService;
import is.hi.hbv501g.vibe.Services.GroupService;
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
    private GroupService groupService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/create-group", method = RequestMethod.POST)
    public String createGroup(@ModelAttribute("group") Group group, @RequestParam("username") String username) {
        User adminUser = userService.findUserByUsername(username).orElse(null);
        if (adminUser == null) {
            return "redirect:/error";
        }

        groupService.createGroup(group.getGroupName(), group.getDescription(), adminUser);
        return "redirect:/profile?username=" + username;
    }


    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        if (user.getUserName() == null || user.getUserName().isEmpty() ||
                user.getUserPW() == null || user.getUserPW().isEmpty() ||
                user.getFullName() == null || user.getFullName().isEmpty() ||
                user.getAddress() == null || user.getAddress().isEmpty() ||
                user.getPhoneNumber() == null || user.getPhoneNumber().isEmpty() ||
                user.getEmailAddress() == null || user.getEmailAddress().isEmpty()) {

            model.addAttribute("error", "All fields are required.");
            return "register";
        }

        if (!user.getUserPW().equals(user.getConfirmPassword())) {
            model.addAttribute("error", "Password and Confirm Password do not match.");
            return "register";
        }

        try {
            userService.registerUser(user);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
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
            Set<Group> userGroups = user.getGroups();
            model.addAttribute("userGroups", userGroups);
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

    @RequestMapping(value = "/profile/edit", method = RequestMethod.GET)
    public String showEditProfileForm(@RequestParam("username") String username, Model model) {
        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        model.addAttribute("user", user);
        return "edit_profile";
    }

    @RequestMapping(value = "/profile/edit", method = RequestMethod.POST)
    public String updateProfile(@ModelAttribute("user") User user, Model model) {
        if (!user.getUserPW().isEmpty() && !user.getUserPW().equals(user.getConfirmPassword())) {
            model.addAttribute("error", "New Password and Confirm Password do not match.");
            return "edit_profile";
        }

        User existingUser = userService.findUserById(user.getID())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + user.getID()));

        existingUser.setFullName(user.getFullName());
        existingUser.setAddress(user.getAddress());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        existingUser.setEmailAddress(user.getEmailAddress());
        existingUser.setUserName(user.getUserName());

        if (user.getUserPW() != null && !user.getUserPW().isEmpty()) {
            existingUser.setUserPW(user.getUserPW());
        }

        try {
            userService.updateUser(existingUser);
            return "redirect:/profile?username=" + existingUser.getUserName();
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "edit_profile";
        }
    }



}
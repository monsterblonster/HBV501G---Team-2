package is.hi.hbv501g.vibe.Controllers;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import is.hi.hbv501g.vibe.Persistance.Entities.Invitation;
import is.hi.hbv501g.vibe.Persistance.Entities.User;
import is.hi.hbv501g.vibe.Persistance.Entities.Group;
import is.hi.hbv501g.vibe.Services.ActivityService;
import is.hi.hbv501g.vibe.Services.EventService;
import is.hi.hbv501g.vibe.Services.FileStorageService;
import is.hi.hbv501g.vibe.Services.InvitationService;
import is.hi.hbv501g.vibe.Services.UserService;
import is.hi.hbv501g.vibe.Services.GroupService;



@Controller
public class UserController {

    private final UserService userService;
    private final InvitationService invitationService;
    private final FileStorageService fileStorageService;
    
    @Autowired
    private GroupService groupService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    public UserController(UserService userService, InvitationService invitationService, FileStorageService fileStorageService) {
        this.userService = userService;
        this.invitationService = invitationService;
        this.fileStorageService = fileStorageService;
    }

    @RequestMapping(value = "/create-group", method = RequestMethod.POST)
    public String createGroup(@ModelAttribute("group") Group group, @RequestParam("username") String username) {
        User adminUser = userService.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        group.setAdmin(adminUser);
        //group.getMembers().add(adminUser);
        groupService.createGroup(group);
        return "redirect:/profile?username=" + username;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUser(@ModelAttribute("user") User user, @RequestParam("profilePicture") MultipartFile profilePicture, Model model) {
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
            if (!profilePicture.isEmpty()) {
                String filename = user.getUserName() + "_profile.jpg";
                String filePath = fileStorageService.storeFile(profilePicture, filename, "user");
                user.setProfilePicturePath("/images/users/" + filename);
            }

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

    @RequestMapping(value = "/upload-picture", method = RequestMethod.POST)
    public String uploadProfilePicture(@RequestParam("file") MultipartFile file, @RequestParam("username") String username) {
        String filename = username + "_profile.jpg";
        String filePath = fileStorageService.storeFile(file, filename, "user");
        userService.updateUserProfilePicture(username, "/images/users/" + filename);
        return "redirect:/profile?username=" + username;
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

            List<Invitation> invitations = invitationService.findInvitationsByUser(user);
            model.addAttribute("invitations", invitations);
            model.addAttribute("profilePicturePath", user.getProfilePicturePath());
        } else {
            model.addAttribute("error", "User not found");
        }
        return "profile";
    }

    @RequestMapping(value = "/invitations/{id}/accept", method = RequestMethod.POST)
    public String acceptInvitation(@PathVariable("id") Long invitationId, @RequestParam("username") String username) {
        User invited = userService.findUserByUsername(username).orElse(null);
        activityService.acceptInvite(invitationService.findById(invitationId).orElse(null).getGroup(), invited);
        invitationService.acceptInvitation(invitationId);
        return "redirect:/profile?username=" + username;
    }

    @RequestMapping(value = "/invitations/{id}/decline", method = RequestMethod.POST)
    public String declineInvitation(@PathVariable("id") Long invitationId, @RequestParam("username") String username) {
        activityService.declineInvite(invitationService.findById(invitationId).orElse(null).getGroup(), userService.findUserByUsername(username).orElse(null));
        invitationService.declineInvitation(invitationId);
        return "redirect:/profile?username=" + username;
    }


    @RequestMapping(value = "/create-group", method = RequestMethod.GET)
    public String showCreateGroupForm(Model model, @RequestParam("username") String username) {
        User dbUser = userService.findUserByUsername(username).orElse(null);
        model.addAttribute("user", dbUser);
        model.addAttribute("group", new Group());
        return "group_create";
    }

    //allows you to leave group and removes all association
    @RequestMapping(value = "/profile/remove-group", method = RequestMethod.POST)
    public String removeGroup(@RequestParam("username") String username, @RequestParam("groupId") Long groupId, Model model) {
        // Find the user and group by the provided parameters
        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        Group group = groupService.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + groupId));

        // Remove user from the group
        if (group.getMembers().contains(user)) {
            group.getMembers().remove(user);
            groupService.saveGroup(group);
        }

        // Remove user from all events associated with the group
        group.getGroupEvents().forEach(event -> {
            if (event.getParticipants().contains(user)) {
                event.getParticipants().remove(user);
                eventService.save(event);
            }
        });

        // Redirect back to the profile page
        return "redirect:/profile?username=" + username;
    }

    @RequestMapping(value = "/profile/edit", method = RequestMethod.GET)
    public String showEditProfileForm(@RequestParam("username") String username, Model model) {
        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        model.addAttribute("user", user);
        return "edit_profile";
    }

    @RequestMapping(value = "/profile/edit", method = RequestMethod.POST)
    public String updateProfile(@ModelAttribute("user") User user, @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture, Model model) {
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

        if (profilePicture != null && !profilePicture.isEmpty()) {
            String filename = user.getUserName() + "_profile.jpg";
            String filePath = fileStorageService.storeFile(profilePicture, filename, "user");
            existingUser.setProfilePicturePath("/images/users/" + filename);
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

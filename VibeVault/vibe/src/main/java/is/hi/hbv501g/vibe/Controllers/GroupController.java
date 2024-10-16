package is.hi.hbv501g.vibe.Controllers;

import is.hi.hbv501g.vibe.Persistance.Entities.Group;
import is.hi.hbv501g.vibe.Persistance.Entities.User;
import is.hi.hbv501g.vibe.Services.GroupService;
import is.hi.hbv501g.vibe.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;
    private final UserService userService;

    @Autowired
    public GroupController(GroupService groupService, UserService userService) {
        this.groupService = groupService;
        this.userService = userService;
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String showCreateGroupForm(Model model) {
        model.addAttribute("group", new Group());
        return "group_create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createGroup(@ModelAttribute("group") Group group) {
        Optional<User> adminOptional = userService.findUserByUsername("DefaultAdmin");

        if (adminOptional.isPresent()) {
            User admin = adminOptional.get();

            group.setAdmin(admin);

            groupService.createGroup(group.getGroupName(), group.getDescription(), admin);
        } else {
            String defaultUsername = "DefaultAdmin";
            String defaultPassword = "defaultPassword";

            userService.registerUser(defaultUsername, defaultPassword);

            Optional<User> newUserOptional = userService.findUserByUsername(defaultUsername);
            if (newUserOptional.isPresent()) {
                User newUser = newUserOptional.get();

                group.setAdmin(newUser);

                groupService.createGroup(group.getGroupName(), group.getDescription(), newUser);
            }
        }

        return "redirect:/groups";
    }



    @RequestMapping(value = "", method = RequestMethod.GET)
    public String showGroups(Model model) {
        model.addAttribute("groups", groupService.findAllGroups());
        return "groups";
    }

    @RequestMapping(value = "/{id}/invite", method = RequestMethod.POST)
    public String inviteUser(@PathVariable("id") Long groupId, @RequestParam("username") String username) {
        Optional<Group> group = groupService.findById(groupId);
        Optional<User> user = userService.findUserByUsername(username);
        if (group.isPresent() && user.isPresent()) {
            groupService.addUserToGroup(user.get(), group.get());
        }
        return "redirect:/groups";
    }

}

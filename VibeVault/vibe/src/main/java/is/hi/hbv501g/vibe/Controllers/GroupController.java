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
    public String createGroup(@RequestParam("username") String username, @ModelAttribute("group") Group group) {
        User creator = userService.findUserByUsername(username).get();
        groupService.createGroup(group.getGroupName(), group.getDescription(), creator);
        /*
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
        */
        return "redirect:/profile?username=" + creator.getUserName();
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

    // Hægt að endurnefna í showActivityLog og breyta í það
    // eða bara halda þessu sem lista yfir alla eventa í group og hafa activity log sem annað
    @RequestMapping(value = "/{groupName}/events", method=RequestMethod.GET)
    public String showGroupEvents(@RequestParam("username") String username, @PathVariable("groupName") String groupName, Model model) {
        Group group = groupService.findByGroupName(groupName).orElse(null);
        model.addAttribute("user", userService.findUserByUsername(username).orElse(null));
        model.addAttribute("events", group.getGroupEvents());
        model.addAttribute("group", group);
        return "group_events";
    }
    

}

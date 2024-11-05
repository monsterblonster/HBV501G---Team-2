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
import java.util.Set;


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
        User creator = userService.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        groupService.createGroup(group.getGroupName(), group.getDescription(), creator);
        return "redirect:/profile?username=" + creator.getUserName();
    }


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String showGroups(@RequestParam("username") String username, Model model) {
        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        Set<Group> userGroups = user.getGroups();
        model.addAttribute("groups", userGroups);
        model.addAttribute("user", user);
        return "groups";
    }

    @RequestMapping(value = "/{id}/invite", method = RequestMethod.POST)
    public String inviteUser(@PathVariable("id") Long groupId, @RequestParam("username") String username, Model model) {
        Optional<Group> group = groupService.findById(groupId);
        Optional<User> user = userService.findUserByUsername(username);

        if (group.isEmpty() || user.isEmpty()) {
            model.addAttribute("error", "User or group not found");
            return "error";
        }

        groupService.addUserToGroup(user.get(), group.get());
        return "redirect:/groups";
    }

    @RequestMapping(value = "/{id}/add-tag", method = RequestMethod.POST)
    public String addTagToGroup(@PathVariable("id") Long groupId, @RequestParam("tag") String tagName, @RequestParam("username") String username) {
        groupService.addTagToGroup(groupId, tagName);
        return "redirect:/groups/" + groupId + "/details?username=" + username;
    }


    @RequestMapping(value = "/{id}/remove-tag", method = RequestMethod.POST)
    public String removeTagFromGroup(@PathVariable("id") Long groupId, @RequestParam("tag") String tagName, @RequestParam("username") String username) {
        groupService.removeTagFromGroup(groupId, tagName);
        return "redirect:/groups/" + groupId + "/details?username=" + username;
    }


    @RequestMapping(value = "/{id}/details", method = RequestMethod.GET)
    public String showGroupDetails(@PathVariable("id") Long groupId, Model model, @RequestParam("username") String username) {
        Group group = groupService.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + groupId));
        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        model.addAttribute("user", user);
        model.addAttribute("group", group);
        return "group_details";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String showEditGroupForm(@PathVariable("id") Long groupId, Model model, @RequestParam("username") String username) {
        Group group = groupService.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + groupId));

        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        model.addAttribute("group", group);
        model.addAttribute("user", user);
        return "group_edit"; // The template for editing a group
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    public String editGroup(@PathVariable("id") Long groupId,
                            @RequestParam("groupName") String groupName,
                            @RequestParam("description") String description,
                            @RequestParam("username") String username) {
        Group group = groupService.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + groupId));

        group.setGroupName(groupName);
        group.setDescription(description);

        groupService.saveGroup(group);
        return "redirect:/groups/" + groupId + "/details?username=" + username;
    }



    // Hægt að endurnefna í showActivityLog og breyta í það
    // eða bara halda þessu sem lista yfir alla eventa í group og hafa activity log sem annað
    @RequestMapping(value = "/{groupName}/events", method=RequestMethod.GET)
    public String showGroupEvents(@RequestParam("username") String username, @PathVariable("groupName") String groupName, Model model) {
        Group group = groupService.findByGroupName(groupName).orElse(null);
        model.addAttribute("user", userService.findUserByUsername(username).orElse(null));
        if (group != null) {
            model.addAttribute("events", group.getGroupEvents());
        } else model.addAttribute("error", "Group not found.");
        model.addAttribute("group", group);
        return "group_events";
    }
    

}

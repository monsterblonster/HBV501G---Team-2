package is.hi.hbv501g.vibe.Controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import is.hi.hbv501g.vibe.Persistance.Entities.Group;
import is.hi.hbv501g.vibe.Persistance.Entities.Tag;
import is.hi.hbv501g.vibe.Persistance.Entities.User;
import is.hi.hbv501g.vibe.Persistance.Entities.Activity;
import is.hi.hbv501g.vibe.Persistance.Entities.Event;
import is.hi.hbv501g.vibe.Persistance.Repositories.TagRepository;
import is.hi.hbv501g.vibe.Services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;
    private final UserService userService;
    private final InvitationService invitationService;
    private final TagRepository tagRepository;
    private final FileStorageService fileStorageService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    public GroupController(GroupService groupService, UserService userService, InvitationService invitationService, TagRepository tagRepository, FileStorageService fileStorageService) {
        this.groupService = groupService;
        this.userService = userService;
        this.invitationService = invitationService;
        this.tagRepository = tagRepository;
        this.fileStorageService = fileStorageService;
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String showCreateGroupForm(@RequestParam("username") String username, Model model) {
        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        model.addAttribute("user", user);
        model.addAttribute("group", new Group());
        return "group_create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createGroup(@RequestParam("username") String username,
                              @RequestParam(value = "tags", required = false) String tags,
                              @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
                              @ModelAttribute("group") Group group) {
        User creator = userService.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        group.setAdmin(creator);

        Set<Tag> tagSet = new HashSet<>();
        if (tags != null && !tags.isBlank()) {
            String[] tagArray = tags.split(",");
            for (String tagName : tagArray) {
                String trimmedTagName = tagName.trim();
                Tag tagEntity = tagRepository.findByName(trimmedTagName)
                        .orElseGet(() -> new Tag(trimmedTagName));
                tagSet.add(tagEntity);
            }
        }
        group.setTags(tagSet);

        if (profilePicture != null && !profilePicture.isEmpty()) {
            String filename = group.getGroupName() + "_profile.jpg";
            String filePath = fileStorageService.storeFile(profilePicture, filename, "group");
            group.setProfilePicturePath(filePath);
        }

        groupService.createGroup(group);
        activityService.createGroup(group, creator);
        return "redirect:/profile?username=" + creator.getUserName();
    }


    @RequestMapping(value = "/{id}/invite", method = RequestMethod.POST)
    public String inviteUser(@PathVariable("id") Long groupId, @RequestParam("username") String invitedUsername, @RequestParam("currentUser") String currentUsername, Model model) {
        try {
            invitationService.createInvitation(groupId, invitedUsername);
            Group group = groupService.findById(groupId).orElse(null);
            User inviter = userService.findUserByUsername(currentUsername).orElse(null);
            User invited = userService.findUserByUsername(invitedUsername).orElse(null);
            activityService.inviteUser(group, inviter, invited);
            return "redirect:/groups/" + groupId + "/details?username=" + currentUsername;
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "error_page";
        }
    }

    @RequestMapping(value = "/{id}/add-tag", method = RequestMethod.POST)
    public String addTagToGroup(@PathVariable("id") Long groupId, @RequestParam("tag") String tagName, @RequestParam("username") String username) {
        groupService.addTagToGroup(groupId, tagName);
        activityService.addTag(groupService.findById(groupId).orElse(null), tagName, userService.findUserByUsername(username).orElse(null));
        return "redirect:/groups/" + groupId + "/details?username=" + username;
    }

    @RequestMapping(value = "/{id}/remove-tag", method = RequestMethod.POST)
    public String removeTagFromGroup(@PathVariable("id") Long groupId, @RequestParam("tag") String tagName, @RequestParam("username") String username) {
        groupService.removeTagFromGroup(groupId, tagName);
        activityService.removeTag(groupService.findById(groupId).orElse(null), tagName, userService.findUserByUsername(username).orElse(null));
        return "redirect:/groups/" + groupId + "/details?username=" + username;
    }

    @RequestMapping(value = "/{id}/details", method = RequestMethod.GET)
    public String showGroupDetails(@PathVariable("id") Long groupId, Model model, @RequestParam("username") String username) {
        Group group = groupService.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + groupId));
        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        model.addAttribute("user", user);
        groupService.refreshEvents(group);
        model.addAttribute("group", group);
        List<Event> events = new ArrayList<>();
        for (Event event : group.getGroupEvents()) {
            if (event.getStatus().equalsIgnoreCase("Upcoming")) {
                events.add(event);
            }
        }
        model.addAttribute("events", events);
        List<Activity> activities = activityService.findByGroupAndPage(group, 0, 10);
        model.addAttribute("activities", activities);
        model.addAttribute("currentMemberCount", group.getCurrentMemberCount());
        model.addAttribute("maxMembers", group.getMaxMembers());
        return "group_details";
    }

    @RequestMapping(value = "/{id}/activity", method = RequestMethod.GET)
    public String showGroupActivity(@PathVariable("id") Long groupId, Model model, @RequestParam("username") String username, @RequestParam("page") int pageNumber) {
        Group group = groupService.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + groupId));
        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        int pageSize = 20;
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("user", user);
        model.addAttribute("group", group);
        List<Activity> activities = activityService.findByGroupAndPage(group, pageNumber, pageSize);
        model.addAttribute("activities", activities);
        model.addAttribute("currentMemberCount", group.getCurrentMemberCount());
        model.addAttribute("maxMembers", group.getMaxMembers());
        return "group_activity";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String showEditGroupForm(@PathVariable("id") Long groupId, Model model, @RequestParam("username") String username) {
        Group group = groupService.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + groupId));
        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        if (!group.getAdmin().equals(user)) {
            model.addAttribute("error", "Only the admin can edit the group.");
            return "error_page";
        }

        model.addAttribute("group", group);
        model.addAttribute("user", user);
        return "group_edit";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    public String editGroup(@PathVariable("id") Long groupId,
                            @RequestParam("groupName") String groupName,
                            @RequestParam("description") String description,
                            @RequestParam("maxMembers") Integer maxMembers,
                            @RequestParam("username") String username,
                            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture) {

        Group group = groupService.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + groupId));

        group.setGroupName(groupName);
        group.setDescription(description);
        group.setMaxMembers(maxMembers);

        if (profilePicture != null && !profilePicture.isEmpty()) {
            String filename = group.getGroupName() + "_profile.jpg";
            String filePath = fileStorageService.storeFile(profilePicture, filename, "group");
            group.setProfilePicturePath(filePath);
        }
        activityService.editGroup(group, group.getAdmin());
        groupService.saveGroup(group);
        return "redirect:/groups/" + groupId + "/details?username=" + username;
    }


    @RequestMapping(value = "/{id}/remove-user", method = RequestMethod.POST)
    public String removeUser(@PathVariable("id") Long groupId, @RequestParam("username") String username, @RequestParam("currentUser") String currentUser, Model model) {
        Group group = groupService.findById(groupId)
            .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + groupId));
        User userToRemove = userService.findUserByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        User adminUser = userService.findUserByUsername(currentUser)
            .orElseThrow(() -> new IllegalArgumentException("Admin user not found with username: " + currentUser));

        if (!group.getAdmin().equals(adminUser)) {
            model.addAttribute("error", "Only the admin can remove users from the group.");
            return "error_page";
        }

        if (group.getAdmin().equals(userToRemove)) {
            model.addAttribute("error", "The admin cannot remove themselves from the group.");
            return "redirect:/groups/" + groupId + "/details?username=" + currentUser;
        }

        // Remove user from the group
        group.getMembers().remove(userToRemove);
        activityService.removeUser(group, adminUser, userToRemove);
        groupService.saveGroup(group);

        // Remove user from all events associated with the group
        for (Event event : group.getGroupEvents()) {
            if (event.getParticipants().contains(userToRemove)) {
                event.getParticipants().remove(userToRemove);
                eventService.save(event); // Save updated event after removing the participant
            }
        }

        // Redirect back to the group details page
        return "redirect:/groups/" + groupId + "/details?username=" + currentUser;
    }


    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    public String deleteGroup(@PathVariable("id") Long groupId, @RequestParam("username") String currentUser, Model model) {
        Group group = groupService.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + groupId));
        User adminUser = userService.findUserByUsername(currentUser)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + currentUser));

        if (!group.getAdmin().equals(adminUser)) {
            model.addAttribute("error", "Only the admin can delete the group.");
            return "error_page";
        }

        groupService.deleteGroup(groupId, adminUser);
        return "redirect:/profile?username=" + currentUser;
    }




    // Hægt að endurnefna í showActivityLog og breyta í það
    // eða bara halda þessu sem lista yfir alla eventa í group og hafa activity log sem annað
    @RequestMapping(value = "/{id}/pastEvents", method=RequestMethod.GET)
    public String showGroupEvents(@RequestParam("username") String username, @PathVariable("id") Long groupId, Model model) {
        Group group = groupService.findById(groupId).orElse(null);
        model.addAttribute("user", userService.findUserByUsername(username).orElse(null));
        if (group != null) {
            List<Event> pastEvents = new ArrayList<>();
            for (Event event : group.getGroupEvents()) {
                if (!event.getStatus().equalsIgnoreCase("Upcoming")) {
                    pastEvents.add(event);
                }
            }
            model.addAttribute("events", pastEvents);
        } else model.addAttribute("error", "Group not found.");
        model.addAttribute("group", group);
        return "group_events";
    }


}

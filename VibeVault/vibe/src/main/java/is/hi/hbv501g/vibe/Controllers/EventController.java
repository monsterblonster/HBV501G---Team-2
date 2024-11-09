package is.hi.hbv501g.vibe.Controllers;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import is.hi.hbv501g.vibe.Persistance.Entities.Activity;
import is.hi.hbv501g.vibe.Persistance.Entities.Comment;
import is.hi.hbv501g.vibe.Persistance.Entities.Event;
import is.hi.hbv501g.vibe.Persistance.Entities.Group;
import is.hi.hbv501g.vibe.Persistance.Entities.User;
import is.hi.hbv501g.vibe.Services.EventService;
import is.hi.hbv501g.vibe.Services.GroupService;
import is.hi.hbv501g.vibe.Services.UserService;
import is.hi.hbv501g.vibe.Services.FileStorageService;
import is.hi.hbv501g.vibe.Services.ActivityService;
import is.hi.hbv501g.vibe.Services.CommentService;



@Controller
@RequestMapping("/events")
public class EventController {
    private final UserService userService;
    private final GroupService groupService;
    private final EventService eventService;
    private final FileStorageService fileStorageService;
    private final CommentService commentService;
    private final ActivityService activityService;

    // hérn verður bara unnið með staka eventa, þannig búa til event, skoða event, fara í event chat
    // hver event getur verið með hlekk yfir í group-una sem hann tilheyrir
    @Autowired
    public EventController(UserService userService, GroupService groupService, EventService eventService, FileStorageService fileStorageService, CommentService commentService, ActivityService activityService) {
        this.eventService = eventService;
        this.groupService = groupService;
        this.userService = userService;
        this.fileStorageService = fileStorageService;
        this.commentService = commentService;
        this.activityService = activityService;
    }
    @RequestMapping(value = "/create", method=RequestMethod.GET)
    public String viewCreateGroupForm(@RequestParam("username") String creatorName, @RequestParam("groupname") String groupName, Model model) {
        model.addAttribute("user", userService.findUserByUsername(creatorName).orElse(null));
        model.addAttribute("group", groupService.findByGroupName(groupName).orElse(null));
        model.addAttribute("event", new Event());
        return "event_create";
    }

    @RequestMapping(value = "/create", method=RequestMethod.POST)
    public String createEvent(
            @RequestParam("username") String username,
            @RequestParam("groupname") String groupname,
            @RequestParam("eventPhoto") MultipartFile eventPhoto,
            @ModelAttribute("event") Event event) {

        User creator = userService.findUserByUsername(username).orElse(null);
        Group group = groupService.findByGroupName(groupname).orElse(null);
        event.setGroup(group);
        event.setCreator(creator);
        event.setDate(new Date());

        if (eventPhoto != null && !eventPhoto.isEmpty()) {
            String filename = "event_" + System.currentTimeMillis() + "_" + eventPhoto.getOriginalFilename();
            String photoPath = fileStorageService.storeFile(eventPhoto, filename);
            event.setPhotoPath(photoPath);
        }

        eventService.save(event);
        Activity activity = new Activity();
        activity.setGroup(group);
        activity.setPostTime(LocalDateTime.now());
        activity.setUser(creator);
        activity.setDescriptionString("created a new event: ");
        activity.setTypeString(event.getName());
        activity.setLinkString("/events/" + event.getId().toString() + "/details?username=");
        activity.setExtraString("Click to view!");
        activityService.save(activity);
        return "redirect:/events/" + event.getId() + "/details?username=" + username;
    }



    @RequestMapping(value = "/{id}/details", method = RequestMethod.GET)
    public String eventPage(@PathVariable("id") Long eventId, @RequestParam("username") String username, Model model) {
        Event event = eventService.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));
        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        model.addAttribute("event", event);
        model.addAttribute("user", user);
        model.addAttribute("comment", new Comment());
        return "event";
    }


    @RequestMapping(value = "/{id}/details/comment", method = RequestMethod.POST)
    public String addComment(@PathVariable("id") Long eventId, @ModelAttribute("comment") Comment comment, @RequestParam("username") String username) {
        Event event = eventService.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with id: " + eventId));

        User author = userService.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        comment.setEvent(event);
        comment.setAuthor(author);

        commentService.save(comment); // Save the comment
        return "redirect:/events/" + eventId + "/details?username=" + username;
    }

}

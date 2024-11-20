package is.hi.hbv501g.vibe.Controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import is.hi.hbv501g.vibe.Persistance.Entities.Activity;
import is.hi.hbv501g.vibe.Persistance.Entities.Comment;
import is.hi.hbv501g.vibe.Persistance.Entities.Event;
import is.hi.hbv501g.vibe.Persistance.Entities.User;
import is.hi.hbv501g.vibe.Persistance.Entities.Group;
import is.hi.hbv501g.vibe.Persistance.Entities.Attendance;
import is.hi.hbv501g.vibe.Services.*;

@Controller
@RequestMapping("/events")
public class EventController {
    private final UserService userService;
    private final GroupService groupService;
    private final EventService eventService;
    private final FileStorageService fileStorageService;
    private final CommentService commentService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    public EventController(UserService userService, GroupService groupService, EventService eventService, FileStorageService fileStorageService, CommentService commentService) {
        this.eventService = eventService;
        this.groupService = groupService;
        this.userService = userService;
        this.fileStorageService = fileStorageService;
        this.commentService = commentService;
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String viewCreateGroupForm(@RequestParam("username") String creatorName, @RequestParam("groupname") String groupName, Model model) {
        model.addAttribute("user", userService.findUserByUsername(creatorName).orElse(null));
        model.addAttribute("group", groupService.findByGroupName(groupName).orElse(null));
        model.addAttribute("event", new Event());
        return "event_create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createEvent(
            @RequestParam("username") String username,
            @RequestParam("groupname") String groupname,
            @RequestParam("eventPhoto") MultipartFile eventPhoto,
            @ModelAttribute("event") Event event,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam("datetime") LocalDateTime datetime) {

        User creator = userService.findUserByUsername(username).orElse(null);
        Group group = groupService.findByGroupName(groupname).orElse(null);
        event.setGroup(group);
        event.setCreator(creator);
        event.setDate(datetime);
        event.getParticipants().add(creator);

        if (eventPhoto != null && !eventPhoto.isEmpty()) {
            String filename = "event_" + System.currentTimeMillis() + "_" + eventPhoto.getOriginalFilename();
            String photoPath = fileStorageService.storeFile(eventPhoto, filename, "event");
            event.setPhotoPath("/images/events/" + filename);
        }

        eventService.save(event);
        activityService.createEvent(group, event, creator);

        return "redirect:/events/" + event.getId() + "/details?username=" + username;
    }

    @RequestMapping(value = "/{id}/details", method = RequestMethod.GET)
    public String eventPage(@PathVariable("id") Long eventId, @RequestParam("username") String username, Model model
	) {
		        Event event = eventService.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));
        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        boolean isParticipant = event.getParticipants().contains(user);
        model.addAttribute("isParticipant", isParticipant);
        model.addAttribute("event", event);
        model.addAttribute("user", user);
        model.addAttribute("comment", new Comment());
        model.addAttribute("participants", event.getParticipants());
        return "event";
    }

    @RequestMapping(value = "/{id}/attendance", method = RequestMethod.POST)
    public String updateAttendance(
            @PathVariable("id") Long eventId,
            @RequestParam("username") String username,
            @RequestParam("attendance") Attendance attendance) {

        Event event = eventService.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));

        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        event.getParticipantStatus().put(user, attendance); // what is put doing here
        eventService.save(event);

        // Update attendance status
        event.setAttendance(attendance);
        eventService.save(event);

        return "redirect:/events/" + eventId + "/details?username=" + username;
    }

    @RequestMapping(value = "/{id}/join", method = RequestMethod.POST)
    public String joinEvent(@PathVariable("id") Long eventId, @RequestParam("username") String username, Model model) {
        Event event = eventService.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));
        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        if (event.getParticipants().contains(user)) {
            model.addAttribute("error", "You are already a participant of this event.");
            return "error_page";
        }

        event.getParticipants().add(user);
        eventService.save(event);
        activityService.joinEvent(event.getGroup(), event, user);

        return "redirect:/groups/" + event.getGroup().getId() + "/details?username=" + username;
    }

    @RequestMapping(value = "/{id}/details/comment", method = RequestMethod.POST)
    public String addComment(@PathVariable("id") Long eventId, @ModelAttribute("comment") Comment comment, @RequestParam("username") String username) {
        Event event = eventService.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with id: " + eventId));

        User author = userService.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        comment.setEvent(event);
        comment.setAuthor(author);
        comment.setDate(LocalDateTime.now());
        activityService.eventComment(event.getGroup(), event, author);
        commentService.save(comment); // Save the comment
        return "redirect:/events/" + eventId + "/details?username=" + username;
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    public String deleteEvent(@PathVariable("id") Long eventId, @RequestParam("username") String username, Model model) {
        Event event = eventService.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));
        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        // Only creator can delete event
        if (!event.getCreator().equals(user)) {
            model.addAttribute("error", "Only the event creator can delete this event.");
            return "error_page";
        }

        // Log the deletion in the activity log
        activityService.deleteEvent(event.getGroup(), event, user);

        eventService.delete(event);
        return "redirect:/groups/" + event.getGroup().getId() + "/details?username=" + username;
    }
}

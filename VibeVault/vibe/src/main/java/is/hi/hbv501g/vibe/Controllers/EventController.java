package is.hi.hbv501g.vibe.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import is.hi.hbv501g.vibe.Persistance.Entities.Event;
import is.hi.hbv501g.vibe.Persistance.Entities.Group;
import is.hi.hbv501g.vibe.Persistance.Entities.User;
import is.hi.hbv501g.vibe.Services.EventService;
import is.hi.hbv501g.vibe.Services.GroupService;
import is.hi.hbv501g.vibe.Services.UserService;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;


@Controller
@RequestMapping("/events")
public class EventController {
    private final UserService userService;
    private final GroupService groupService;
    private final EventService eventService;

    // hérn verður bara unnið með staka eventa, þannig búa til event, skoða event, fara í event chat
    // hver event getur verið með hlekk yfir í group-una sem hann tilheyrir
    @Autowired
    public EventController(UserService userService, GroupService groupService, EventService eventService) {
        this.eventService = eventService;
        this.groupService = groupService;
        this.userService = userService;
    }
    @RequestMapping(value = "/create", method=RequestMethod.GET)
    public String viewCreateGroupForm(@RequestParam("username") String creatorName, @RequestParam("groupname") String groupName, Model model) {
        model.addAttribute("user", userService.findUserByUsername(creatorName).orElse(null));
        model.addAttribute("group", groupService.findByGroupName(groupName).orElse(null));
        model.addAttribute("event", new Event());
        return "event_create";
    }

    @RequestMapping(value = "/create", method=RequestMethod.POST)
    public String createEvent(@RequestParam("username") String username, @RequestParam("groupname") String groupname, @ModelAttribute("event") Event event,  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam("datetime") LocalDateTime datetime) {
        User creator = userService.findUserByUsername(username).get();
        Group group = groupService.findByGroupName(groupname).get();
        event.setGroup(group);
        event.setCreator(creator);
        event.setDate(datetime);
        eventService.save(event);
        return "redirect:/groups/" + groupname + "/events?username=" + username;
    }
    
    

    @RequestMapping(value = "/{id}/details", method = RequestMethod.GET)
    public String eventPage() {
        return "event";
    }
}

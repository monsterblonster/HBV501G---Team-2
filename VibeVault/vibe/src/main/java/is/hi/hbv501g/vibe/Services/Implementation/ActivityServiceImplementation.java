package is.hi.hbv501g.vibe.Services.Implementation;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.cglib.core.Local;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import is.hi.hbv501g.vibe.Persistance.Entities.Activity;
import is.hi.hbv501g.vibe.Persistance.Entities.Event;
import is.hi.hbv501g.vibe.Persistance.Entities.Group;
import is.hi.hbv501g.vibe.Persistance.Entities.User;
import is.hi.hbv501g.vibe.Persistance.Repositories.ActivityRepository;
import is.hi.hbv501g.vibe.Services.ActivityService;

@Service
public class ActivityServiceImplementation implements ActivityService{
    private final ActivityRepository activityRepository;

    public ActivityServiceImplementation(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public Activity save(Activity activity) {
        return activityRepository.save(activity);
    }

    @Override
    public Activity base(Group group, User user) {
        Activity activity = new Activity();
        activity.setGroup(group);
        activity.setUser(user);
        activity.setPostTime(LocalDateTime.now());
        return activity;
    }
    @Override
    public Activity inviteUser(Group group, User inviter, User invited) {
        Activity activity = base(group, inviter);
        activity.setExtraString("invited: " + invited.getUserName());
        return this.save(activity);
    }

    @Override
    public Activity acceptInvite(Group group, User invited) {
        Activity activity = base(group, invited);
        activity.setDescriptionString("Accepted their invitation.");
        activity.setExtraString("Welcome to the group!");
        return this.save(activity);
    }
    
    @Override
    public Activity declineInvite(Group group, User invited) {
        Activity activity = base(group, invited);
        activity.setDescriptionString("Declined an invitation to the group :'(");
        return this.save(activity);
    }

    @Override 
    public Activity createEvent(Group group, Event event, User creator) {
        Activity activity = base(group, creator);
        activity.setDescriptionString("created a new event: ");
        activity.setTypeString(event.getName());
        activity.setLinkString("/events/" + event.getId().toString() + "/details?username=");
        activity.setExtraString("Click to view!");
        return this.save(activity);
    }

    @Override
    public Activity editEvent(Group group, Event event, User creator) {
        Activity activity = base(group, creator);
        activity.setDescriptionString("changed their event: ");
        activity.setTypeString(event.getName());
        activity.setLinkString("/events/" + event.getId().toString() + "/details?username=");
        return this.save(activity);
    }

    @Override
    public Activity deleteEvent(Group group, Event event, User creator) {
        Activity activity = base(group, creator);
        activity.setDescriptionString("deleted an event: ");
        activity.setTypeString(event.getName());
        activity.setLinkString("");
        activity.setExtraString("The event has been removed.");
        return this.save(activity);
    }

    @Override
    public Activity createGroup(Group group, User creator) {
        Activity activity = base(group, creator);
        activity.setDescriptionString("created the group!");
        return this.save(activity);
    }

    @Override
    public Activity editGroup(Group group, User user) {
        Activity activity = base(group, user);
        activity.setDescriptionString("edited the group details.");
        return this.save(activity);
    }

    @Override
    public Activity eventComment(Group group, Event event, User creator) {
        Activity activity = base(group, creator);
        activity.setDescriptionString("commented on: ");
        activity.setTypeString(event.getName());
        activity.setLinkString("/events/" + event.getId().toString() + "/details?username=");
        return this.save(activity);
    }

    @Override
    public Activity removeUser(Group group, User user, User removed) {
        Activity activity = base(group, user);
        activity.setDescriptionString("removed " + removed.getUserName() + " from the group.");
        return this.save(activity);
    }

    @Override
    public Activity joinEvent(Group group, Event event, User user) {
        Activity activity = base(group, user);
        activity.setDescriptionString("is attending ");
        activity.setTypeString(event.getName());
        activity.setLinkString("/events/" + event.getId().toString() + "/details?username=");
        return this.save(activity);
    }

    @Override
    public List <Activity> reversedByGroup(Group group) {
        List<Activity> activities = this.findByGroup(group);
        Collections.reverse(activities);
        return activities;
    }

    @Override
    public void delete(Activity activity) {
        activityRepository.delete(activity);
        return;
    }

    @Override
    public List<Activity> findAll() {
        return activityRepository.findAll();
    }

    @Override
    public List<Activity> findByGroupAndPage(Group group, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("postTime").descending());
        return activityRepository.findAllByGroup(group, pageable);
    }

    @Override
    public List<Activity> findByGroup(Group group) {
        return activityRepository.findByGroup(group);
    }

    @Override
    public Optional<Activity> findById(Long id) {
        return activityRepository.findById(id);
    }
}

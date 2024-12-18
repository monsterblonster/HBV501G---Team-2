package is.hi.hbv501g.vibe.Services.Implementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import is.hi.hbv501g.vibe.Persistance.Entities.Activity;
import is.hi.hbv501g.vibe.Persistance.Entities.Attendance;
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
    public Activity addTag(Group group, String tag, User user) {
        Activity activity = base(group, user);
        activity.setDescriptionString("added the tag: " + tag);
        return this.save(activity);
    }

    @Override
    public Activity removeTag(Group group, String tag, User user) {
        Activity activity = base(group, user);
        activity.setDescriptionString("removed the tag: " + tag);
        return this.save(activity);
    }

    @Override
    public Activity holdEvent(Event event) {
        Activity activity = new Activity();
        activity.setUser(event.getCreator());
        activity.setGroup(event.getGroup());
        activity.setPostTime(event.getDate());
        activity.setDescriptionString("held the event: ");
        activity.setTypeString(event.getName());
        activity.setLinkString("/events/" + event.getId().toString() + "/details?username=");
        return this.save(activity);
    }

    @Override
    public Activity cancelEvent(Event event) {
        Activity activity = base(event.getGroup(), event.getCreator());
        activity.setDescriptionString("canceled the event: ");
        activity.setTypeString(event.getName());
        activity.setLinkString("/events/" + event.getId().toString() + "/details?username=");
        return this.save(activity);
    }

    @Override
    public void deleteEvent(Event event) {
        List<Activity> temp = new ArrayList<>();
        for(Activity activity : this.reversedByGroup(event.getGroup())) {
            if (activity.getLinkString() != null) {
                if (activity.getLinkString().equalsIgnoreCase("/events/" + event.getId().toString() + "/details?username=")) temp.add(activity);
            }
        }
        for(Activity activity : temp) this.delete(activity);
    }

    @Override
    public Activity updateAttendance(User user, Attendance attendance, Event event) {
        Activity activity = base(event.getGroup(), user);
        activity.setDescriptionString("is " + attendance.toString() + " to: ");
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

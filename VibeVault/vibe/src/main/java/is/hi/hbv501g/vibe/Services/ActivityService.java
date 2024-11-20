package is.hi.hbv501g.vibe.Services;

import java.util.List;
import java.util.Optional;


import is.hi.hbv501g.vibe.Persistance.Entities.Activity;
import is.hi.hbv501g.vibe.Persistance.Entities.Attendance;
import is.hi.hbv501g.vibe.Persistance.Entities.Event;
import is.hi.hbv501g.vibe.Persistance.Entities.Group;
import is.hi.hbv501g.vibe.Persistance.Entities.User;

public interface ActivityService {
    Activity save(Activity activity);
    void delete(Activity activity);
    List<Activity> findAll();
    List<Activity> findByGroupAndPage(Group group, int pageNumber, int pageSize);
    List<Activity> findByGroup(Group group);
    Optional<Activity> findById(Long id);
    Activity base(Group group, User user);
    Activity inviteUser(Group group, User inviter, User invited);
    Activity acceptInvite(Group group, User invited);
    Activity declineInvite(Group group, User invited);
    Activity createEvent(Group group, Event event, User creator);
    Activity editEvent(Group group, Event event, User creator);
    Activity createGroup(Group group, User creator);
    Activity editGroup(Group group, User user);
    Activity eventComment(Group group, Event event, User creator);
    Activity removeUser(Group group, User user, User removed);
    Activity joinEvent(Group group, Event event, User user);
    Activity addTag(Group group, String tag, User user);
    Activity removeTag(Group group, String tag, User user);
    Activity holdEvent(Event event);
    Activity cancelEvent(Event event);
    Activity updateAttendance(User user, Attendance attendance, Event event);
    void deleteEvent(Event event);
    List<Activity> reversedByGroup(Group group);
}

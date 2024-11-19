package is.hi.hbv501g.vibe.Services;

import java.util.List;
import java.util.Optional;

import is.hi.hbv501g.vibe.Persistance.Entities.Activity;
import is.hi.hbv501g.vibe.Persistance.Entities.Event;
import is.hi.hbv501g.vibe.Persistance.Entities.Group;
import is.hi.hbv501g.vibe.Persistance.Entities.User;

public interface ActivityService {
    Activity save(Activity activity);
    void delete(Activity activity);
    List<Activity> findAll();
    List<Activity> findByGroup(Group group);
    Optional<Activity> findById(Long id);
    Activity base(Group group, User user);
    Activity inviteUser(Group group, User inviter, User invited);
    Activity acceptInvite(Group group, User invited);
    Activity declineInvite(Group group, User invited);
    Activity createEvent(Group group, Event event, User creator);
    Activity deleteEvent(Group group, Event event, User creator);
    Activity createGroup(Group group, User creator);
    Activity editGroup(Group group, User user);
    Activity eventComment(Group group, Event event, User creator);
    Activity removeUser(Group group, User user, User removed);
    Activity joinEvent(Group group, Event event, User user);
}

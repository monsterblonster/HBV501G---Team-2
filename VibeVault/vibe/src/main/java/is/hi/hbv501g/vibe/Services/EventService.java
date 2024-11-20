package is.hi.hbv501g.vibe.Services;

import java.util.List;
import java.util.Optional;
import is.hi.hbv501g.vibe.Persistance.Entities.Event;
import is.hi.hbv501g.vibe.Persistance.Entities.Group;

public interface EventService {
    Event save(Event event);
    Event editEvent(Event event);
    void delete(Event event);
    List<Event> findAll();
    List<Event> findByEventGroup(Group group);
    List<Event> findByName(String name);
    Optional<Event> findById(Long id);
    void updateEvent(Event event);
}

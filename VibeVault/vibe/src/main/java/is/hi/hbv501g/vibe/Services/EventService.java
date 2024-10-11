package is.hi.hbv501g.vibe.Services;

import java.util.List;

import is.hi.hbv501g.vibe.Persistance.Entities.Event;
import is.hi.hbv501g.vibe.Persistance.Entities.Group;

public interface EventService {
    public Event save(Event event);
    public void delete(Event event);
    public List<Event> findAll();
    public List<Event> findByEventGroup(Group group);
    public List<Event> findByName(String name);
    public Event findByID(long ID);
}

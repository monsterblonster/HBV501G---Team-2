package is.hi.hbv501g.vibe.Services.Implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import is.hi.hbv501g.vibe.Persistance.Entities.Event;
import is.hi.hbv501g.vibe.Persistance.Entities.Group;
import is.hi.hbv501g.vibe.Persistance.Repositories.EventRepository;
import is.hi.hbv501g.vibe.Services.EventService;

@Service
public class EventServiceImplementation implements EventService {
    private EventRepository eventRepository;

    public Event save(Event event) {
        return eventRepository.save(event);
    }

    public void delete(Event event) {
        eventRepository.delete(event);
        return;
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public List<Event> findByEventGroup(Group group){
        return eventRepository.findByGroup(group);
    }

    public List<Event> findByName(String name) {
        return eventRepository.findByName(name);
    }

    public Event findByID(long ID) {
        return eventRepository.findByID(ID);
    }
}

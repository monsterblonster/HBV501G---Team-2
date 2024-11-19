package is.hi.hbv501g.vibe.Services.Implementation;

import java.util.List;

import java.util.Optional;
import org.springframework.stereotype.Service;

import is.hi.hbv501g.vibe.Persistance.Entities.Event;
import is.hi.hbv501g.vibe.Persistance.Entities.Group;
import is.hi.hbv501g.vibe.Persistance.Repositories.EventRepository;
import is.hi.hbv501g.vibe.Services.EventService;

@Service
public class EventServiceImplementation implements EventService {
    private EventRepository eventRepository;

    public EventServiceImplementation(EventRepository eventRepository){
        this.eventRepository = eventRepository;
    }

    @Override
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public void delete(Event event) {
        eventRepository.delete(event);
        return;
    }

    @Override
    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    @Override
    public List<Event> findByEventGroup(Group group){
        return eventRepository.findByGroup(group);
    }

    @Override
    public List<Event> findByName(String name) {
        return eventRepository.findByName(name);
    }
    
    @Override
    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }
		
		@Override
    public void updateEvent(Event event) {
        eventRepository.save(event);
    }
}

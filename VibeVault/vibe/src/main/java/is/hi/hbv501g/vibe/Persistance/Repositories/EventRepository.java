package is.hi.hbv501g.vibe.Persistance.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import is.hi.hbv501g.vibe.Persistance.Entities.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    Event save(Event event);
    void delete(Event event);
    List<Event> findAll();
    List<Event> findByGroup(); // gæti þurft custom útfærslu
    List<Event> findByName(String name);
    Event findByID(long ID);
    
}

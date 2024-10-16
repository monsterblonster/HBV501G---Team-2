package is.hi.hbv501g.vibe.Persistance.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import is.hi.hbv501g.vibe.Persistance.Entities.Event;
import is.hi.hbv501g.vibe.Persistance.Entities.Group;
import is.hi.hbv501g.vibe.Persistance.Entities.User;

public interface EventRepository extends JpaRepository<Event, Long> {
    Event save(Event event);
    void delete(Event event);
    List<Event> findAll();
    List<Event> findByGroup(Group group); // gæti þurft custom útfærslu
    List<Event> findByName(String name);
    //Event findByCreator(User creator);
    Event findByID(long ID);
}

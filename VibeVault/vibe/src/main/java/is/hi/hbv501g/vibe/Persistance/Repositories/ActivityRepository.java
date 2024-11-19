package is.hi.hbv501g.vibe.Persistance.Repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import is.hi.hbv501g.vibe.Persistance.Entities.Activity;
import is.hi.hbv501g.vibe.Persistance.Entities.Group;
import is.hi.hbv501g.vibe.Persistance.Entities.User;

public interface ActivityRepository extends JpaRepository<Activity, Long>{
    Activity save(Activity activity);
    void delete(Activity activity);
    List<Activity> findAll();
    List<Activity> findAllByGroup(Group group, Pageable pageable);
    List<Activity> findByUser(User user);
    List<Activity> findByGroup(Group group);
}

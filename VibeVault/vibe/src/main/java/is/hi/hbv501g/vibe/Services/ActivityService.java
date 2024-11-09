package is.hi.hbv501g.vibe.Services;

import java.util.List;
import java.util.Optional;

import is.hi.hbv501g.vibe.Persistance.Entities.Activity;
import is.hi.hbv501g.vibe.Persistance.Entities.Group;

public interface ActivityService {
    public Activity save(Activity activity);
    public void delete(Activity activity);
    public List<Activity> findAll();
    public List<Activity> findByGroup(Group group);
    public Optional<Activity> findById(Long id);
}

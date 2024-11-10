package is.hi.hbv501g.vibe.Services.Implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import is.hi.hbv501g.vibe.Persistance.Entities.Activity;
import is.hi.hbv501g.vibe.Persistance.Entities.Group;
import is.hi.hbv501g.vibe.Persistance.Repositories.ActivityRepository;
import is.hi.hbv501g.vibe.Services.ActivityService;

@Service
public class ActivityServiceImplementation implements ActivityService{
    private ActivityRepository activityRepository;

    public ActivityServiceImplementation(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public Activity save(Activity activity) {
        return activityRepository.save(activity);
    }

    @Override
    public void delete(Activity activity) {
        activityRepository.delete(activity);
        return;
    }

    @Override
    public List<Activity> findAll() {
        return activityRepository.findAll();
    }

    @Override
    public List<Activity> findByGroup(Group group) {
        return activityRepository.findByGroup(group);
    }

    @Override
    public Optional<Activity> findById(Long id) {
        return activityRepository.findById(id);
    }
}

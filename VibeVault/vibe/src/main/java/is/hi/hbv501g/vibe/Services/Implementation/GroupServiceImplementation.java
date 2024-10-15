package is.hi.hbv501g.vibe.Services.Implementation;

import is.hi.hbv501g.vibe.Persistance.Entities.Group;
import is.hi.hbv501g.vibe.Persistance.Entities.User;
import is.hi.hbv501g.vibe.Persistance.Repositories.GroupRepository;
import is.hi.hbv501g.vibe.Services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImplementation implements GroupService {

    private final GroupRepository groupRepository;

    @Autowired
    public GroupServiceImplementation(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public Group createGroup(String name, String description, User admin) {
        Group group = new Group();
        group.setGroupName(name);
        group.setDescription(description);
        group.setAdmin(admin);
        group.getMembers().add(admin);

        return groupRepository.save(group);
    }

    @Override
    public Optional<Group> findByGroupName(String groupName) {
        return groupRepository.findByGroupName(groupName);
    }

    @Override
    public void addUserToGroup(User user, Group group) {
        group.addMember(user);
        groupRepository.save(group);
    }

    @Override
    public Optional<Group> findById(Long id) {
        return groupRepository.findById(id);
    }

    @Override
    public List<Group> findAllGroups() {
        return groupRepository.findAll();
    }
}

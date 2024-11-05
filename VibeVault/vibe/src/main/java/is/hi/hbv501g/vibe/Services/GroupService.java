package is.hi.hbv501g.vibe.Services;

import is.hi.hbv501g.vibe.Persistance.Entities.Group;
import is.hi.hbv501g.vibe.Persistance.Entities.User;
import java.util.List;
import java.util.Optional;

public interface GroupService {
    Group createGroup(String name, String description, User admin);
    Optional<Group> findByGroupName(String groupName);
    void addUserToGroup(User user, Group group);
    List<Group> findAllGroups();
    void removeUserFromGroup(User user, Group group);
    Group addTagToGroup(Long groupId, String tagName);
    Group removeTagFromGroup(Long groupId, String tagName);
    void saveGroup(Group group);

    Optional<Group> findById(Long id);
}

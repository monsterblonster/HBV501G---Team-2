package is.hi.hbv501g.vibe.Services;

import is.hi.hbv501g.vibe.Persistance.Entities.Group;
import is.hi.hbv501g.vibe.Persistance.Entities.User;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    Group createGroup(Group group);
    Optional<Group> findByGroupName(String groupName);
    void addUserToGroup(User user, Group group);
    List<Group> findAllGroups();
    void removeUserFromGroup(User user, Group group, User admin);
    Group addTagToGroup(Long groupId, String tagName);
    Group removeTagFromGroup(Long groupId, String tagName);
    void deleteGroup(Long id, User admin);
    void saveGroup(Group group);
    void refreshEvents(Group group);
    Optional<Group> findById(Long id);
}

package is.hi.hbv501g.vibe.Services.Implementation;

import is.hi.hbv501g.vibe.Persistance.Entities.Group;
import is.hi.hbv501g.vibe.Persistance.Entities.Tag;
import is.hi.hbv501g.vibe.Persistance.Entities.User;
import is.hi.hbv501g.vibe.Persistance.Repositories.TagRepository;
import is.hi.hbv501g.vibe.Persistance.Repositories.GroupRepository;
import is.hi.hbv501g.vibe.Services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImplementation implements GroupService {

    private final GroupRepository groupRepository;
    private final TagRepository tagRepository;

    @Autowired
    public GroupServiceImplementation(GroupRepository groupRepository, TagRepository tagRepository) {
        this.groupRepository = groupRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public void saveGroup(Group group) {
        groupRepository.save(group);
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
    public Group addTagToGroup(Long groupId, String tagName) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));
        Tag tag = tagRepository.findByName(tagName)
                .orElseGet(() -> tagRepository.save(new Tag(tagName))); // Save new tag if it doesn't exist

        group.addTag(tag);
        return groupRepository.save(group);  // Save the group with the new tag
    }

    @Override
    public Group removeTagFromGroup(Long groupId, String tagName) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));
        Tag tag = tagRepository.findByName(tagName)
                .orElseThrow(() -> new IllegalArgumentException("Tag not found"));

        group.removeTag(tag);
        return groupRepository.save(group); // Save the group without the tag
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
    public void removeUserFromGroup(User user, Group group) {
        group.removeMember(user);
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

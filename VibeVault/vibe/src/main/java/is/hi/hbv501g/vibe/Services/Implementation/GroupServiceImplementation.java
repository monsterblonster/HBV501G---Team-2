package is.hi.hbv501g.vibe.Services.Implementation;

import is.hi.hbv501g.vibe.Persistance.Entities.Event;
import is.hi.hbv501g.vibe.Persistance.Entities.Group;
import is.hi.hbv501g.vibe.Persistance.Entities.Tag;
import is.hi.hbv501g.vibe.Persistance.Entities.User;
import is.hi.hbv501g.vibe.Persistance.Repositories.TagRepository;
import is.hi.hbv501g.vibe.Persistance.Repositories.GroupRepository;
import is.hi.hbv501g.vibe.Services.ActivityService;
import is.hi.hbv501g.vibe.Services.EventService;
import is.hi.hbv501g.vibe.Services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImplementation implements GroupService {

    private final GroupRepository groupRepository;
    private final TagRepository tagRepository;

    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private EventService eventService;

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
    public Group createGroup(Group group) {
        group.getMembers().add(group.getAdmin());
        return groupRepository.save(group);
    }


    @Override
    public Group addTagToGroup(Long groupId, String tagName) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));
        Tag tag = tagRepository.findByName(tagName)
                .orElseGet(() -> tagRepository.save(new Tag(tagName)));

        group.addTag(tag);
        return groupRepository.save(group);
    }

    @Override
    public Group removeTagFromGroup(Long groupId, String tagName) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));
        Tag tag = tagRepository.findByName(tagName)
                .orElseThrow(() -> new IllegalArgumentException("Tag not found"));

        group.removeTag(tag);
        return groupRepository.save(group);
    }

    @Override
    public Optional<Group> findByGroupName(String groupName) {
        return groupRepository.findByGroupName(groupName);
    }

    @Override
    public void addUserToGroup(User user, Group group) {
        if (group.getMembers().size() < group.getMaxMembers()) {
            group.addMember(user);
            groupRepository.save(group);
        } else {
            throw new IllegalStateException("Group has reached the maximum number of members.");
        }
    }

    @Override
    public void removeUserFromGroup(User user, Group group, User admin) {
        if (!group.getAdmin().equals(admin)) {
            throw new IllegalArgumentException("Only the admin can remove members from the group.");
        }
        if (group.getAdmin().equals(user)) {
            throw new IllegalArgumentException("The admin cannot remove themselves from the group.");
        }

        group.removeMember(user);
        groupRepository.save(group);
    }


    @Override
    public void deleteGroup(Long id, User admin) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + id));

        if (!group.getAdmin().equals(admin)) {
            throw new IllegalArgumentException("Only the admin can delete the group.");
        }

        group.getMembers().forEach(member -> member.getGroups().remove(group));
        group.getTags().forEach(tag -> tag.getGroups().remove(group));

        groupRepository.delete(group);
    }

    @Override
    public void refreshEvents(Group group) {
        for (Event event : group.getGroupEvents()) {
            if (event.getDate().isBefore(LocalDateTime.now()) && event.getStatus().equalsIgnoreCase("upcoming")) {
                event.setStatus("Over");
                eventService.save(event);
                activityService.holdEvent(event);
            } //else { break; }
        }
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

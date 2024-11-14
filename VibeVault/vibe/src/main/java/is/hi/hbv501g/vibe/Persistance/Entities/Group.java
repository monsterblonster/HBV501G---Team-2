package is.hi.hbv501g.vibe.Persistance.Entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String groupName;

    @Column(length = 500)
    private String description;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

    @ManyToMany
    @JoinTable(
            name = "group_members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private final Set<User> members = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "group_tags",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Event> groupEvents = new HashSet<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Activity> groupActivities = new HashSet<>();

    private Integer maxMembers;

    private String profilePicturePath;

    public Group() {}

    public Group(String groupName, String description, User admins, Integer maxMembers) {
        this.groupName = groupName;
        this.description = description;
        this.members.add(this.admin);
        this.maxMembers = maxMembers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void addMember(User user) {
        this.members.add(user);
    }

    public void removeMember(User user) {
        this.members.remove(user);
    }

    public Set<Event> getGroupEvents() {
        return groupEvents;
    }

    public void setGroupEvents(Set<Event> groupEvents) {
        this.groupEvents = groupEvents;
    }

    public Set<Tag> getTags() { return tags; }
    public void setTags(Set<Tag> tags) { this.tags = tags; }

    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getGroups().add(this);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getGroups().remove(this);
    }

    public Integer getMaxMembers() { return maxMembers; }

    public void setMaxMembers(Integer maxMembers) { this.maxMembers = maxMembers; }

    public int getCurrentMemberCount() {
        return members.size();
    }

    public Set<Activity> getGroupActivities() {
        return groupActivities;
    }

    public void setGroupActivities(Set<Activity> groupActivities) {
        this.groupActivities = groupActivities;
    }

    public String getProfilePicturePath() { return profilePicturePath; }

    public void setProfilePicturePath(String profilePicturePath) { this.profilePicturePath = profilePicturePath; }

}


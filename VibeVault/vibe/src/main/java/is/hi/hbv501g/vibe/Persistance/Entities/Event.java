package is.hi.hbv501g.vibe.Persistance.Entities;

//import java.util.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.MapKeyJoinColumn;
import java.util.Map;
import java.util.HashMap;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Event(){}

    public Event(String name, LocalDateTime date, String description, String status, Group group, User creator){
        this.name = name;
        this.date = date;
        this.description = description;
        this.status = status;
        this.group = group;
        this.creator = creator;
        this.comments = new ArrayList<Comment>();
        this.participants = new ArrayList<User>();
        this.participants.add(this.creator);
        this.timeCreated = LocalDateTime.now();
    }
    
    private String name;
    private LocalDateTime timeCreated;
    private LocalDateTime date;
    private String description;
    private String status;
    private String photoPath;

    @Enumerated(EnumType.STRING)
    private Attendance attendance;

    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;

    @ManyToMany
    @JoinTable(
        name = "event_participants",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<User> participants = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "participant_status", joinColumns = @JoinColumn(name = "event_id"))
    @MapKeyJoinColumn(name = "user_id")
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Map<User, Attendance> participantStatus = new HashMap<>();

    public Map<User, Attendance> getParticipantStatus() {
        return participantStatus;
    }

    public void setParticipantStatus(Map<User, Attendance> participantStatus) {
        this.participantStatus = participantStatus;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Group group;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(LocalDateTime timeCreated) {
        this.timeCreated = timeCreated;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Group getGroup() {
        return group;
    }
    public void setGroup(Group group) {
        this.group = group;
    }
    public Attendance getAttendance() {
        return attendance;
    }
    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }
    public List<Comment> getComments() {
        return comments;
    }
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    public String getPhotoPath() { return photoPath; }

    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }
    
}

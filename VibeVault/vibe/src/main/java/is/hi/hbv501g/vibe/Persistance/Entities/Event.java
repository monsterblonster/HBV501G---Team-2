package is.hi.hbv501g.vibe.Persistance.Entities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;

    @Override
    public String toString() {
        return "Event [ID=" + ID + ", name=" + name + ", date=" + date + ", description=" + description + ", status="
                + status + ", group=" + group + ", creator=" + creator + ", participants=" + participants
                + ", comments=" + comments + "]";
    }

    protected Event(){}

    public Event(String name, Date date, String description, String status, Group group, User creator){
        this.name = name;
        this.date = date;
        this.description = description;
        this.status = status;
        this.group = group;
        this.creator = creator;
        List<User> particpant = new ArrayList<User>();
        particpant.add(creator);
        this.participants = particpant;
        this.comments = new ArrayList<Comment>();
    }
    
    private String name;
    private Date date;
    private String description;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;

    @ManyToMany
    private List<User> participants;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public long getID() {
        return ID;
    }
    public void setID(long iD) {
        ID = iD;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
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
    public List<Comment> getComments() {
        return comments;
    }
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    
}

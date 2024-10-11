package is.hi.hbv501g.vibe.Persistance.Entities;

import java.util.List;

import is.hi.hbv501g.vibe.Persistance.Entities.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;

    private String name;
    private String status;
    private User creator;
    private Group group;
    private List<User> participants;
}

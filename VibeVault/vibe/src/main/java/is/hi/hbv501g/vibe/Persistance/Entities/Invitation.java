package is.hi.hbv501g.vibe.Persistance.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "invitations")
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "accepted", nullable = false)
    private boolean accepted = false;

    public Invitation() {}

    public Invitation(Group group, User user) {
        this.group = group;
        this.user = user;
    }

    public Long getId() { return id; }

    public Group getGroup() { return group; }

    public void setGroup(Group group) { this.group = group; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public boolean isAccepted() { return accepted; }

    public void setAccepted(boolean accepted) { this.accepted = accepted; }
}

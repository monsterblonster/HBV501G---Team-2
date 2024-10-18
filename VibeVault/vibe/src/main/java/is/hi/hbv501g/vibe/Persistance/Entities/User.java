package is.hi.hbv501g.vibe.Persistance.Entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long ID;

    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(name = "userpw", nullable = false)
    private String userPW;

    @ManyToMany(mappedBy = "members")
    private Set<Group> groups = new HashSet<>();

    public User() {}

    public User(String userName, String userPW) {
        this.userName = userName;
        this.userPW = userPW;
    }

    @Override
    public String toString() {
        return "User [ID=" + ID + ", userName=" + userName + ", userPW=" + userPW + "]";
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) { this.userName = userName; }

    public String getUserPW() {
        return userPW;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public void setUserPW(String userPW) {
        this.userPW = userPW;
    }
    
}

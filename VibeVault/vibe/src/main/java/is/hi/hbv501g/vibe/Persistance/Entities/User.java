package is.hi.hbv501g.vibe.Persistance.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

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

    public User() {}

    public User(String userName, String userPW) {
        this.userName = userName;
        this.userPW = userPW;
    }

    public Long getID() {
        return ID;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPW() {
        return userPW;
    }
}

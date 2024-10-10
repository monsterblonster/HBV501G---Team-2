package is.hi.hbv501g.vibe.Persistance.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long ID;
    private String userName;
    private String userPW;

    protected User() {}

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

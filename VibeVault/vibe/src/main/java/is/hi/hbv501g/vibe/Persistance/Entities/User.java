package is.hi.hbv501g.vibe.Persistance.Entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long ID;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "email_address", nullable = false, unique = true)
    private String emailAddress;

    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(name = "userpw", nullable = false)
    private String userPW;

    @Transient
    private String confirmPassword;

    @ManyToMany(mappedBy = "members")
    private Set<Group> groups = new HashSet<>();
    
    @ManyToMany(mappedBy = "participants")
    private List<Event> events;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> ownedEvents;


    public User() {}

    public User(String userName, String userPW) {
        this.userName = userName;
        this.userPW = userPW;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "User [ID=" + ID + ", userName=" + userName + ", userPW=" + userPW + "]";
    }

    public String getFullName() { return fullName; }

    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmailAddress() { return emailAddress; }

    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }

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

    public String getConfirmPassword() { return confirmPassword; }

    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
    
}

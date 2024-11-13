package is.hi.hbv501g.vibe.Persistance.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String commentData;
    
    //private DateTime commentTime;

    public Comment() {}

    //should also have user id and event id.
    public Comment(String commentData, Event event) {
        this.event = event;
        this.commentData = commentData;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    public String getCommentData() {
        return commentData;
    }

    public void setCommentData(String commentData) {
        this.commentData = commentData;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getAuthor() { return author; }

    public void setAuthor(User author) { this.author = author; }


    
}

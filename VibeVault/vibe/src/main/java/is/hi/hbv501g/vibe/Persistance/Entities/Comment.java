package is.hi.hbv501g.vibe.Persistance.Entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String commentData;
    
    private LocalDateTime commentTime;

    public Comment() {}

    //should also have user id and event id.
    public Comment(String commentData, Event event, LocalDateTime commentTime) {
        this.event = event;
        this.commentData = commentData;
				this.commentTime = commentTime;
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
		
	public LocalDateTime getDate() {
		return commentTime;
	}
	
	public void setDate(LocalDateTime date) {
		this.commentTime = date;
	}

    public User getAuthor() { return author; }

    public void setAuthor(User author) { this.author = author; }


    
}

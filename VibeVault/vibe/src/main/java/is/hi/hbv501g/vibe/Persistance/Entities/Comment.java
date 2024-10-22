package is.hi.hbv501g.vibe.Persistance.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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
    public Comment(Long id, String commentData) {
        this.id = id;
        this.commentData = commentData;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommentData() {
        return commentData;
    }

    public void setCommentData(String commentData) {
        this.commentData = commentData;
    }
}

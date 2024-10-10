package is.hi.hbv501g.vibe.Persistance.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "groupstest")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
}

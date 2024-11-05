package is.hi.hbv501g.vibe.Persistance.Repositories;

import is.hi.hbv501g.vibe.Persistance.Entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
}

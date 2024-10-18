package is.hi.hbv501g.vibe.Persistance.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import is.hi.hbv501g.vibe.Persistance.Entities.Group;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long>{
    Optional<Group> findByGroupName(String groupName);
}

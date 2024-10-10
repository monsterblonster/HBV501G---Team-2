package is.hi.hbv501g.vibe.Persistance.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import is.hi.hbv501g.vibe.Persistance.Entities.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long>{
    
}

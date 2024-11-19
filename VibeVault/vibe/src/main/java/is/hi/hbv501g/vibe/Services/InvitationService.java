package is.hi.hbv501g.vibe.Services;

import is.hi.hbv501g.vibe.Persistance.Entities.Invitation;
import is.hi.hbv501g.vibe.Persistance.Entities.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface InvitationService {
    Invitation createInvitation(Long groupId, String username);
    void acceptInvitation(Long invitationId);

    @Transactional
    void declineInvitation(Long invitationId);

    List<Invitation> findInvitationsByUser(User user);
    Optional <Invitation> findById(Long id);
}

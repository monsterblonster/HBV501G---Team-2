package is.hi.hbv501g.vibe.Services.Implementation;

import is.hi.hbv501g.vibe.Persistance.Entities.Invitation;
import is.hi.hbv501g.vibe.Persistance.Entities.Group;
import is.hi.hbv501g.vibe.Persistance.Entities.User;
import is.hi.hbv501g.vibe.Persistance.Repositories.InvitationRepository;
import is.hi.hbv501g.vibe.Services.InvitationService;
import is.hi.hbv501g.vibe.Services.GroupService;
import is.hi.hbv501g.vibe.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InvitationServiceImplementation implements InvitationService {

    private final InvitationRepository invitationRepository;
    private final GroupService groupService;
    private final UserService userService;

    @Autowired
    public InvitationServiceImplementation(InvitationRepository invitationRepository, GroupService groupService, UserService userService) {
        this.invitationRepository = invitationRepository;
        this.groupService = groupService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public Invitation createInvitation(Long groupId, String username) {
        Group group = groupService.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + groupId));
        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        if (invitationRepository.findByUserAndGroup(user, group).isPresent()) {
            throw new IllegalArgumentException("User has already been invited to this group.");
        }

        return invitationRepository.save(new Invitation(group, user));
    }

    @Override
    @Transactional
    public void acceptInvitation(Long invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new IllegalArgumentException("Invitation not found with id: " + invitationId));

        Group group = invitation.getGroup();
        User user = invitation.getUser();

        groupService.addUserToGroup(user, group);
        invitationRepository.delete(invitation);
    }

    @Transactional
    @Override
    public void declineInvitation(Long invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new IllegalArgumentException("Invitation not found with id: " + invitationId));

        invitationRepository.delete(invitation);
    }

    @Override
    public List<Invitation> findInvitationsByUser(User user) {
        return invitationRepository.findByUser(user);
    }

    @Override
    public Optional <Invitation> findById(Long id) {
        return invitationRepository.findById(id);
    }
}

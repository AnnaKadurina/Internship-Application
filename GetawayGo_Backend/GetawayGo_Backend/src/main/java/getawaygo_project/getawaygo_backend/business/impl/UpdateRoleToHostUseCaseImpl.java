package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.UpdateRoleToHostUseCase;
import getawaygo_project.getawaygo_backend.business.exception.RoleNotFoundOrValidException;
import getawaygo_project.getawaygo_backend.business.exception.UnauthorizedDataException;
import getawaygo_project.getawaygo_backend.business.exception.UserNotFoundException;
import getawaygo_project.getawaygo_backend.domain.AccessToken;
import getawaygo_project.getawaygo_backend.persistance.AuthorityRepository;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.AuthorityEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateRoleToHostUseCaseImpl implements UpdateRoleToHostUseCase {
    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;
    private AccessToken accessToken;
    @Override
    public void updateRole(long id) {
        if (accessToken.getUserId() != id) {
            throw new UnauthorizedDataException();
        }
        Optional<UserEntity> userUpdate = userRepository.findById(id);
        if (userUpdate.isEmpty())
            throw new UserNotFoundException();
        else
        {
            AuthorityEntity hostAuthority = authorityRepository.findByRole("HOST");
            if (hostAuthority == null) {
                throw new RoleNotFoundOrValidException();
            }
            UserEntity user = userUpdate.get();
            user.setAuthority(hostAuthority);
            userRepository.save(user);
        }

    }
}

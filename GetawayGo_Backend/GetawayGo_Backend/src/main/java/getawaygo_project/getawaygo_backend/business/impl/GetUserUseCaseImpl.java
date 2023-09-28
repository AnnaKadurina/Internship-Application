package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.GetUserUseCase;
import getawaygo_project.getawaygo_backend.business.exception.UnauthorizedDataException;
import getawaygo_project.getawaygo_backend.business.exception.UserNotFoundException;
import getawaygo_project.getawaygo_backend.domain.AccessToken;
import getawaygo_project.getawaygo_backend.domain.User;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetUserUseCaseImpl implements GetUserUseCase {
    private UserRepository userRepository;
    private AccessToken accessToken;

    @Override
    public Optional<User> getUser(long id) {
        if (accessToken.getUserId() != id) {
            throw new UnauthorizedDataException();
        }

        Optional<User> findUser = userRepository.findById(id).map(UserConverter::convert);
        if (findUser.isEmpty())
            throw new UserNotFoundException();
        else
            return findUser;
    }
}

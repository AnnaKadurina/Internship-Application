package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.DeactivateUserUseCase;
import getawaygo_project.getawaygo_backend.business.exception.UserNotFoundException;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@AllArgsConstructor
public class DeactivateUserUseCaseImpl implements DeactivateUserUseCase {
    private UserRepository userRepository;

    @Override
    public void deactivateUser(long id) {
        Optional<UserEntity> userUpdate = userRepository.findById(id);
        if (userUpdate.isEmpty())
            throw new UserNotFoundException();
        else
        {
            UserEntity user = userUpdate.get();
            user.setActive(false);
            userRepository.save(user);
        }
    }
}

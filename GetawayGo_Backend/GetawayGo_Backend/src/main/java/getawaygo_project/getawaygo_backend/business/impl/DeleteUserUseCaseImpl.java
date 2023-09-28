package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.DeleteUserUseCase;
import getawaygo_project.getawaygo_backend.business.exception.UserNotFoundException;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DeleteUserUseCaseImpl implements DeleteUserUseCase {
    private UserRepository userRepository;

    @Override
    public void deleteUser(long userId) {
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isEmpty())
            throw new UserNotFoundException();

        userRepository.deleteById(userId);
    }
}

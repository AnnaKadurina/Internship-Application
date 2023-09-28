package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.UpdateUserUseCase;
import getawaygo_project.getawaygo_backend.business.exception.*;
import getawaygo_project.getawaygo_backend.domain.AccessToken;
import getawaygo_project.getawaygo_backend.domain.UpdateUserRequest;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class UpdateUserUseCaseImpl implements UpdateUserUseCase {
    private UserRepository userRepository;
    private AccessToken accessToken;

    @Override
    public void updateUser(UpdateUserRequest updateUserRequest) {
        if (accessToken.getUserId() != updateUserRequest.getId())
            throw new UnauthorizedDataException();
        Optional<UserEntity> userUpdate = userRepository.findById(updateUserRequest.getId());
        if (userUpdate.isEmpty())
            throw new UserNotFoundException();
        else {
            List<UserEntity> allUsersExceptCurrent = userRepository.findAll()
                    .stream()
                    .filter(user -> user.getUserId() != updateUserRequest.getId())
                    .toList();
            List<String> allUsernamesExceptCurrent = allUsersExceptCurrent.stream()
                    .map(UserEntity::getUsername)
                    .toList();

            List<String> allEmailsExceptCurrent = allUsersExceptCurrent.stream()
                    .map(UserEntity::getEmail)
                    .toList();

            if (allUsernamesExceptCurrent.contains(updateUserRequest.getUsername()))
                throw new UsernameAlreadyExistsException();
            if (allEmailsExceptCurrent.contains(updateUserRequest.getEmail()))
                throw new EmailAlreadyExistsException();

            String regex = "^[^@]+@[^@]+$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(updateUserRequest.getEmail());
            if (!matcher.matches()) {
                throw new InvalidEmailException();
            }

            UserEntity user = userUpdate.get();
            user.setEmail(updateUserRequest.getEmail());
            user.setUsername(updateUserRequest.getUsername());
            user.setFirstName(updateUserRequest.getFirstName());
            user.setLastName(updateUserRequest.getLastName());
            user.setPhone(updateUserRequest.getPhone());
            user.setAddress(updateUserRequest.getAddress());
            userRepository.save(user);
        }
    }
}

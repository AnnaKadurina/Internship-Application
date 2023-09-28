package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.GetUserDTOUseCase;
import getawaygo_project.getawaygo_backend.business.exception.UserNotFoundException;
import getawaygo_project.getawaygo_backend.domain.User;
import getawaygo_project.getawaygo_backend.domain.UserDTO;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetUserDTOUseCaseImpl implements GetUserDTOUseCase {
    private UserRepository userRepository;

    @Override
    public UserDTO getUserDto(long id) {
        Optional<User> findUser = userRepository.findById(id).map(UserConverter::convert);
        if (findUser.isEmpty())
            throw new UserNotFoundException();
        else
            return UserDTO.builder()
                    .username(findUser.get().getUsername())
                    .photo(findUser.get().getPhoto())
                    .build();
    }
}

package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.GetAllUsersUseCase;
import getawaygo_project.getawaygo_backend.business.exception.NoUsersFoundException;
import getawaygo_project.getawaygo_backend.domain.GetAllUsersResponse;
import getawaygo_project.getawaygo_backend.domain.User;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class GetAllUsersUseCaseImpl implements GetAllUsersUseCase {
    private UserRepository userRepository;
    @Override
    public GetAllUsersResponse getUsers() {
        List<UserEntity> users = userRepository.findAll();
        final GetAllUsersResponse response =new GetAllUsersResponse();
        List<User> results = users.stream()
                .map(UserConverter::convert)
                .toList();
        if(results.isEmpty())
            throw new NoUsersFoundException();
        else {
            response.setAllUsers(results);
            return response;
        }
    }
}

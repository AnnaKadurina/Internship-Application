package getawaygo_project.getawaygo_backend.business;

import getawaygo_project.getawaygo_backend.domain.CreateUserRequest;
import getawaygo_project.getawaygo_backend.domain.CreateUserResponse;

public interface CreateUserUseCase {
    CreateUserResponse createUser(CreateUserRequest userRequest);
}

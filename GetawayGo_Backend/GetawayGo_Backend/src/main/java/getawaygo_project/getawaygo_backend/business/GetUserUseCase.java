package getawaygo_project.getawaygo_backend.business;

import getawaygo_project.getawaygo_backend.domain.User;

import java.util.Optional;

public interface GetUserUseCase {
Optional<User> getUser(long id);
}

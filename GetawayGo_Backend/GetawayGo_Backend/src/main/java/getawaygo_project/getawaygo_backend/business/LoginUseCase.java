package getawaygo_project.getawaygo_backend.business;

import getawaygo_project.getawaygo_backend.domain.LoginRequest;
import getawaygo_project.getawaygo_backend.domain.LoginResponse;

public interface LoginUseCase {
    LoginResponse login(LoginRequest loginRequest);
}

package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.AccessTokenEncoder;
import getawaygo_project.getawaygo_backend.business.LoginUseCase;
import getawaygo_project.getawaygo_backend.business.exception.InvalidCredentialsException;
import getawaygo_project.getawaygo_backend.business.exception.ProfileDeactivatedException;
import getawaygo_project.getawaygo_backend.domain.AccessToken;
import getawaygo_project.getawaygo_backend.domain.LoginRequest;
import getawaygo_project.getawaygo_backend.domain.LoginResponse;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginUseCaseImpl implements LoginUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenEncoder accessTokenEncoder;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        UserEntity user = userRepository.findByUsername(loginRequest.getUsername());
        if (user == null)
            throw new InvalidCredentialsException();

        if (!user.getActive())
            throw new ProfileDeactivatedException();

        if (!matchesPassword(loginRequest.getPassword(), user.getPassword()))
            throw new InvalidCredentialsException();

        String accessToken = generateAccessToken(user);
        return LoginResponse.builder().accessToken(accessToken).build();
    }

    private boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private String generateAccessToken(UserEntity user) {
        Long userId = user != null ? user.getUserId() : null;
        String role = user != null && user.getAuthority() != null ? user.getAuthority().getRole() : null;

        return accessTokenEncoder.encode(
                AccessToken.builder()
                        .subject(user.getUsername())
                        .role(role)
                        .userId(userId)
                        .build());
    }
}

package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.AccessTokenEncoder;
import getawaygo_project.getawaygo_backend.business.exception.InvalidCredentialsException;
import getawaygo_project.getawaygo_backend.business.exception.ProfileDeactivatedException;
import getawaygo_project.getawaygo_backend.business.exception.UserNotFoundException;
import getawaygo_project.getawaygo_backend.domain.AccessToken;
import getawaygo_project.getawaygo_backend.domain.LoginRequest;
import getawaygo_project.getawaygo_backend.domain.LoginResponse;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.AuthorityEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AccessTokenEncoder accessTokenEncoder;
    @InjectMocks
    private LoginUseCaseImpl loginUseCase;

    @Test
    void login() {
        AuthorityEntity authority = new AuthorityEntity();
        authority.setRole("ADMIN");

        UserEntity user = new UserEntity();
        user.setUserId(1);
        user.setUsername("anikadurinaa");
        user.setPassword("encodedPassword");
        user.setActive(true);
        user.setAuthority(authority);

        LoginRequest loginRequest = new LoginRequest(user.getUsername(), user.getPassword());

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(accessTokenEncoder.encode(Mockito.any(AccessToken.class))).thenReturn("accessToken");

        LoginResponse loginResponse = loginUseCase.login(loginRequest);

        assertEquals("accessToken", loginResponse.getAccessToken());

        verify(userRepository).findByUsername(user.getUsername());
        verify(passwordEncoder).matches(loginRequest.getPassword(), user.getPassword());
        verify(accessTokenEncoder).encode(Mockito.any(AccessToken.class));
    }
    @Test
    void loginDeactivatedProfile() {
        AuthorityEntity authority = new AuthorityEntity();
        authority.setRole("ADMIN");

        UserEntity user = new UserEntity();
        user.setUserId(1);
        user.setUsername("anikadurinaa");
        user.setPassword("encodedPassword");
        user.setActive(false);
        user.setAuthority(authority);

        LoginRequest loginRequest = new LoginRequest(user.getUsername(), user.getPassword());

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        assertThrows(ProfileDeactivatedException.class, () -> loginUseCase.login(loginRequest));

        verify(userRepository).findByUsername(user.getUsername());
    }

    @Test
    void loginInvalidUsername() {
        String username = "test";
        String password = "password";

        LoginRequest loginRequest = new LoginRequest(username, password);

        when(userRepository.findByUsername(username)).thenReturn(null);

        assertThrows(InvalidCredentialsException.class, () -> loginUseCase.login(loginRequest));

        verify(userRepository).findByUsername(username);
    }
    @Test
    void loginInvalidPassword() {
        AuthorityEntity authority = new AuthorityEntity();
        authority.setRole("ADMIN");

        UserEntity user = new UserEntity();
        user.setUserId(1);
        user.setUsername("anikadurinaa");
        user.setPassword("encodedPassword");
        user.setActive(true);
        user.setAuthority(authority);

        LoginRequest loginRequest = new LoginRequest(user.getUsername(), "fail");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> loginUseCase.login(loginRequest));

        verify(userRepository).findByUsername(user.getUsername());
        verify(passwordEncoder).matches(loginRequest.getPassword(), user.getPassword());
    }

}
package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.exception.*;
import getawaygo_project.getawaygo_backend.domain.CreateUserRequest;
import getawaygo_project.getawaygo_backend.domain.CreateUserResponse;
import getawaygo_project.getawaygo_backend.persistance.AuthorityRepository;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.AuthorityEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthorityRepository authorityRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateUserUseCaseImpl createUserUseCase;

    @Test
    void createUser() {
        CreateUserRequest request = CreateUserRequest.builder()
                .username("anikadurinaa")
                .email("a.kadurina@gmail.com")
                .firstName("Anna")
                .lastName("Kadurina")
                .address("Blg")
                .phone("02589632")
                .photo("photolink")
                .password("Test1234()")
                .build();

        when(userRepository.findByUsername(request.getUsername())).thenReturn(null);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("Test");
        when(authorityRepository.findByRole("GUEST")).thenReturn(new AuthorityEntity());

        CreateUserResponse createUserResponse = createUserUseCase.createUser(request);

        assertNotNull(createUserResponse);
        assertNotNull(createUserResponse.getUserResponse());
        assertEquals(request.getUsername(), createUserResponse.getUserResponse().getUsername());

        verify(userRepository).findByUsername(request.getUsername());
        verify(userRepository).findByEmail(request.getEmail());
        verify(authorityRepository).findByRole("GUEST");
        verify(passwordEncoder).encode(request.getPassword());
    }

    @Test
    void createUserEmailInvalid() {
        CreateUserRequest request = CreateUserRequest.builder()
                .username("anikadurinaa")
                .email("a.kadurina")
                .firstName("Anna")
                .lastName("Kadurina")
                .address("Blg")
                .phone("02589632")
                .photo("photolink")
                .password("Test1234()")
                .build();

        when(userRepository.findByUsername(request.getUsername())).thenReturn(null);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(null);
        when(authorityRepository.findByRole("GUEST")).thenReturn(new AuthorityEntity());

        assertThrows(InvalidEmailException.class, () -> createUserUseCase.createUser(request));

        verify(userRepository).findByUsername(request.getUsername());
        verify(userRepository).findByEmail(request.getEmail());
        verify(authorityRepository).findByRole("GUEST");
    }
    @Test
    void createUserPasswordInvalid() {
        CreateUserRequest request = CreateUserRequest.builder()
                .username("anikadurinaa")
                .email("a.kadurina@gmail.com")
                .firstName("Anna")
                .lastName("Kadurina")
                .address("Blg")
                .phone("02589632")
                .photo("photolink")
                .password("test")
                .build();

        when(userRepository.findByUsername(request.getUsername())).thenReturn(null);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(null);
        when(authorityRepository.findByRole("GUEST")).thenReturn(new AuthorityEntity());

        assertThrows(InvalidPasswordException.class, () -> createUserUseCase.createUser(request));

        verify(userRepository).findByUsername(request.getUsername());
        verify(userRepository).findByEmail(request.getEmail());
        verify(authorityRepository).findByRole("GUEST");
    }

    @Test
    void testCreateUserUsernameAlreadyExists() {
        CreateUserRequest request = CreateUserRequest.builder()
                .username("anikadurinaa")
                .email("a.kadurina@gmail.com")
                .firstName("Anna")
                .lastName("Kadurina")
                .address("Blg")
                .phone("02589632")
                .photo("photolink")
                .password("Test1234()")
                .build();

        when(userRepository.findByUsername(request.getUsername())).thenReturn(new UserEntity());

        assertThrows(UsernameAlreadyExistsException.class, () -> createUserUseCase.createUser(request));
        verify(userRepository).findByUsername(request.getUsername());

    }

    @Test
    void testCreateUserEmailAlreadyExists() {
        CreateUserRequest request = CreateUserRequest.builder()
                .username("anikadurinaa")
                .email("a.kadurina@gmail.com")
                .firstName("Anna")
                .lastName("Kadurina")
                .address("Blg")
                .phone("02589632")
                .photo("photolink")
                .build();

        when(userRepository.findByUsername(request.getUsername())).thenReturn(null);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(new UserEntity());

        assertThrows(EmailAlreadyExistsException.class, () -> createUserUseCase.createUser(request));

        verify(userRepository).findByUsername(request.getUsername());
        verify(userRepository).findByEmail(request.getEmail());
    }

    @Test
    void testCreateUserRoleNotValid() {
        CreateUserRequest request = CreateUserRequest.builder()
                .username("anikadurinaa")
                .email("a.kadurina@gmail.com")
                .firstName("Anna")
                .lastName("Kadurina")
                .address("Blg")
                .phone("02589632")
                .photo("photolink")
                .build();

        when(userRepository.findByUsername(request.getUsername())).thenReturn(null);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(null);
        when(authorityRepository.findByRole("GUEST")).thenReturn(null);

        assertThrows(RoleNotFoundOrValidException.class, () -> createUserUseCase.createUser(request));

        verify(userRepository).findByUsername(request.getUsername());
        verify(userRepository).findByEmail(request.getEmail());
        verify(authorityRepository).findByRole("GUEST");
    }

}
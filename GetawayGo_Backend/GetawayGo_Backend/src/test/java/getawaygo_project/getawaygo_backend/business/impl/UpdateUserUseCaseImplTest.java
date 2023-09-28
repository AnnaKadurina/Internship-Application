package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.exception.*;
import getawaygo_project.getawaygo_backend.domain.AccessToken;
import getawaygo_project.getawaygo_backend.domain.UpdateUserRequest;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AccessToken accessToken;
    @InjectMocks
    private UpdateUserUseCaseImpl updateUserUseCase;

    @Test
    void updateUser() {
        UserEntity updateUser = new UserEntity();
        updateUser.setUserId(1L);
        updateUser.setUsername("anikadurinaa");
        updateUser.setFirstName("Anna");
        updateUser.setLastName("Kadurina");
        updateUser.setAddress("Blg");
        updateUser.setEmail("a.kadurina@gmail.com");
        updateUser.setPhoto("photolink");
        updateUser.setPhone("025896300");

        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(1)
                .username("anikadurinaa")
                .email("a.kadurina@gmail.com")
                .firstName("Penka")
                .lastName("Kadurina")
                .address("Blg")
                .phone("02589632")
                .build();

        when(accessToken.getUserId()).thenReturn(updateUser.getUserId());
        when(userRepository.findById(request.getId())).thenReturn(Optional.of(updateUser));

        updateUserUseCase.updateUser(request);

        assertEquals("Penka", updateUser.getFirstName());
        verify(userRepository).findById(anyLong());
        verify(accessToken).getUserId();
    }
    @Test
    void updateUserEmailInvalid() {
        UserEntity updateUser = new UserEntity();
        updateUser.setUserId(1L);
        updateUser.setUsername("anikadurinaa");
        updateUser.setFirstName("Anna");
        updateUser.setLastName("Kadurina");
        updateUser.setAddress("Blg");
        updateUser.setEmail("a.kadurina@gmail.com");
        updateUser.setPhoto("photolink");
        updateUser.setPhone("025896300");

        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(1)
                .username("anikadurinaa")
                .email("a.kadurina")
                .firstName("Penka")
                .lastName("Kadurina")
                .address("Blg")
                .phone("02589632")
                .build();

        when(accessToken.getUserId()).thenReturn(updateUser.getUserId());
        when(userRepository.findById(request.getId())).thenReturn(Optional.of(updateUser));

        assertThrows(InvalidEmailException.class, () -> updateUserUseCase.updateUser(request));

        verify(userRepository).findById(anyLong());
        verify(accessToken).getUserId();
    }

    @Test
    void updateUserNotFound() {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(1L)
                .username("anikadurinaa")
                .email("a.kadurina@gmail.com")
                .firstName("Penka")
                .lastName("Kadurina")
                .address("Blg")
                .phone("02589632")
                .build();

        when(accessToken.getUserId()).thenReturn(1L);
        when(userRepository.findById(request.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> updateUserUseCase.updateUser(request));
        verify(userRepository).findById(anyLong());
        verify(accessToken).getUserId();
    }

    @Test
    void updateUserUsernameAlreadyExists() {
        List<UserEntity> users = new ArrayList<>();

        UserEntity updateUser = new UserEntity();
        updateUser.setUserId(1);
        updateUser.setUsername("anikadurinaa");
        updateUser.setEmail("a.kadurina@gmail.com");
        updateUser.setFirstName("Anna");
        updateUser.setLastName("Kadurina");
        updateUser.setPhone("558598");
        updateUser.setPhoto("url");
        updateUser.setAddress("Blg");

        UserEntity updateUser2 = new UserEntity();
        updateUser2.setUserId(2);
        updateUser2.setUsername("anikadurinaa2");
        updateUser2.setEmail("a.kadurina2@gmail.com");
        updateUser2.setFirstName("Anna2");
        updateUser2.setLastName("Kadurina2");
        updateUser2.setPhone("5585982");
        updateUser2.setPhoto("url");
        updateUser2.setAddress("Blg");

        users.add(updateUser);
        users.add(updateUser2);

        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(updateUser.getUserId())
                .username(updateUser2.getUsername())
                .build();

        when(accessToken.getUserId()).thenReturn(updateUser.getUserId());
        when(userRepository.findById(updateUser.getUserId())).thenReturn(Optional.of(updateUser));
        when(userRepository.findAll()).thenReturn(users);

        assertThrows(UsernameAlreadyExistsException.class, () -> updateUserUseCase.updateUser(request));

        verify(userRepository).findById(anyLong());
        verify(userRepository).findAll();
        verify(accessToken).getUserId();
    }

    @Test
    void updateUserEmailAlreadyExists() {
        List<UserEntity> users = new ArrayList<>();

        UserEntity updateUser = new UserEntity();
        updateUser.setUserId(1);
        updateUser.setUsername("anikadurinaa");
        updateUser.setEmail("a.kadurina@gmail.com");
        updateUser.setFirstName("Anna");
        updateUser.setLastName("Kadurina");
        updateUser.setPhone("558598");
        updateUser.setPhoto("url");
        updateUser.setAddress("Blg");

        UserEntity updateUser2 = new UserEntity();
        updateUser2.setUserId(2);
        updateUser2.setUsername("anikadurinaa2");
        updateUser2.setEmail("a.kadurina2@gmail.com");
        updateUser2.setFirstName("Anna2");
        updateUser2.setLastName("Kadurina2");
        updateUser2.setPhone("5585982");
        updateUser2.setPhoto("url");
        updateUser2.setAddress("Blg");

        users.add(updateUser);
        users.add(updateUser2);

        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(updateUser.getUserId())
                .email(updateUser2.getEmail())
                .build();

        when(accessToken.getUserId()).thenReturn(updateUser.getUserId());
        when(userRepository.findById(updateUser.getUserId())).thenReturn(Optional.of(updateUser));
        when(userRepository.findAll()).thenReturn(users);

        assertThrows(EmailAlreadyExistsException.class, () -> updateUserUseCase.updateUser(request));

        verify(userRepository).findById(anyLong());
        verify(userRepository).findAll();
        verify(accessToken).getUserId();

    }

    @Test
    void updateUserNotAuthorized() {
        Long id = 1L;
        Long wrongId = 2L;

        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(id)
                .username("test")
                .build();

        when(accessToken.getUserId()).thenReturn(wrongId);

        assertThrows(UnauthorizedDataException.class, () -> updateUserUseCase.updateUser(request));

        verify(accessToken).getUserId();
    }
}
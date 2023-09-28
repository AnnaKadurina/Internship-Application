package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.exception.UnauthorizedDataException;
import getawaygo_project.getawaygo_backend.business.exception.UserNotFoundException;
import getawaygo_project.getawaygo_backend.domain.AccessToken;
import getawaygo_project.getawaygo_backend.domain.User;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.AuthorityEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserUseCaseImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private AccessToken accessToken;
    @InjectMocks
    private GetUserUseCaseImpl getUserUseCase;
    @Test
    void getUser() {
        UserEntity getUser = new UserEntity();
        getUser.setUserId(1);
        getUser.setUsername("anikadurinaa");
        getUser.setFirstName("Anna");
        getUser.setLastName("Kadurina");
        getUser.setAddress("Blg");
        getUser.setEmail("a.kadurina@gmail.com");
        getUser.setPhoto("photolink");
        getUser.setPhone("025896300");
        getUser.setAuthority(new AuthorityEntity());

        when(userRepository.findById(getUser.getUserId())).thenReturn(Optional.of(getUser));
        when(accessToken.getUserId()).thenReturn(getUser.getUserId());

        Optional<User> user = getUserUseCase.getUser(getUser.getUserId());

        assertEquals(getUser.getUsername(), user.get().getUsername());
        verify(userRepository).findById(getUser.getUserId());
        verify(accessToken).getUserId();
    }
    @Test
    void getUserNotFound() {
        Long id = 1L;

        when(accessToken.getUserId()).thenReturn(id);
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> getUserUseCase.getUser(id));
        verify(userRepository).findById(id);
        verify(accessToken).getUserId();
    }

    @Test
    void getUserNotAuthorized() {
        Long id = 1L;
        Long wrongId = 2L;

        when(accessToken.getUserId()).thenReturn(wrongId);

        assertThrows(UnauthorizedDataException.class, () -> getUserUseCase.getUser(id));

        verify(accessToken).getUserId();
    }
}
package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.exception.UserNotFoundException;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActivateUserUseCaseImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ActivateUserUseCaseImpl activateUserUseCase;

    @Test
    void activateUser() {
        UserEntity updateUser = new UserEntity();
        updateUser.setUsername("anikadurinaa");
        updateUser.setUserId(1);
        updateUser.setActive(false);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(updateUser));

        activateUserUseCase.activateUser(1);

        assertEquals(true, updateUser.getActive());
        verify(userRepository).findById(anyLong());
    }
    @Test
    void activateUserNotFound() {
        UserEntity updateUser = new UserEntity();
        updateUser.setUsername("anikadurinaa");
        updateUser.setUserId(1);
        updateUser.setActive(false);

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> activateUserUseCase.activateUser(1));
        assertEquals(false, updateUser.getActive());
        verify(userRepository).findById(anyLong());
    }
}
package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.exception.UserNotFoundException;
import getawaygo_project.getawaygo_backend.persistance.*;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteUserUseCaseImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private PropertyRepository propertyRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private DeleteUserUseCaseImpl deleteUserUseCase;

    @Test
    void deleteUser() {
        UserEntity deleteUser = new UserEntity();
        deleteUser.setUserId(1);
        deleteUser.setUsername("anikadurinaa");
        deleteUser.setFirstName("Anna");
        deleteUser.setLastName("Kadurina");
        deleteUser.setAddress("Blg");
        deleteUser.setEmail("a.kadurina@gmail.com");
        deleteUser.setPhoto("photolink");
        deleteUser.setPhone("025896300");

        when(userRepository.findById(deleteUser.getUserId())).thenReturn(Optional.of(deleteUser));

        deleteUserUseCase.deleteUser(deleteUser.getUserId());

        assertNull(userRepository.findByUsername(deleteUser.getUsername()));
        verify(userRepository).deleteById(deleteUser.getUserId());

    }

    @Test
    void deleteUserNotFound() {
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> deleteUserUseCase.deleteUser(id));
        verify(userRepository).findById(id);

    }
}
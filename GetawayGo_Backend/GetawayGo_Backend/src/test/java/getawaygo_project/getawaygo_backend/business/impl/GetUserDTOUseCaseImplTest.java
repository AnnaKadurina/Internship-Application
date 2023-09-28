package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.exception.UserNotFoundException;
import getawaygo_project.getawaygo_backend.domain.UserDTO;
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
class GetUserDTOUseCaseImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private GetUserDTOUseCaseImpl getUserDTOUseCase;

    @Test
    void getUserDto() {
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

        UserDTO user = getUserDTOUseCase.getUserDto(getUser.getUserId());

        assertEquals(getUser.getUsername(), user.getUsername());
        verify(userRepository).findById(getUser.getUserId());
    }

    @Test
    void getUserDtoNotFound() {
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> getUserDTOUseCase.getUserDto(id));
        verify(userRepository).findById(id);
    }
}
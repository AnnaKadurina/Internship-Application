package getawaygo_project.getawaygo_backend.persistance;

import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @Test
    void testFindByUsername() {
        UserEntity user = new UserEntity();
        user.setUsername("ani");
        user.setEmail("ani@gmail.com");

        when(userRepository.findByUsername("ani")).thenReturn(user);

        UserEntity foundUser = userRepository.findByUsername("ani");

        assertNotNull(foundUser);
        assertEquals("ani", foundUser.getUsername());

        verify(userRepository, times(1)).findByUsername("ani");
    }

    @Test
    void testFindByEmail() {
        UserEntity user = new UserEntity();
        user.setUsername("ani");
        user.setEmail("ani@gmail.com");

        when(userRepository.findByEmail("ani@gmail.com")).thenReturn(user);

        UserEntity foundUser = userRepository.findByEmail("ani@gmail.com");

        assertNotNull(foundUser);
        assertEquals("ani@gmail.com", foundUser.getEmail());

        verify(userRepository, times(1)).findByEmail("ani@gmail.com");
    }
}
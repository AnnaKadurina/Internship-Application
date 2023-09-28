package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.exception.NoUsersFoundException;
import getawaygo_project.getawaygo_backend.domain.GetAllUsersResponse;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.AuthorityEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllUsersUseCaseImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private GetAllUsersUseCaseImpl getAllUsersUseCase;

    @Test
    void getUsers() {
        UserEntity getUser1 = new UserEntity();
        getUser1.setUserId(1);
        getUser1.setUsername("anikadurinaa");
        getUser1.setAuthority(new AuthorityEntity());

        UserEntity getUser2 = new UserEntity();
        getUser2.setUserId(2);
        getUser2.setUsername("anikadurinaa");
        getUser2.setAuthority(new AuthorityEntity());

        List<UserEntity> users = new ArrayList<>();
        users.add(getUser1);
        users.add(getUser2);

        when(userRepository.findAll()).thenReturn(users);

        GetAllUsersResponse response = getAllUsersUseCase.getUsers();

        assertEquals(response.getAllUsers().size(), users.size());
        assertEquals(1, response.getAllUsers().stream().findFirst().get().getUserId());

        verify(userRepository).findAll();
    }

    @Test
    void getUsersNoFound() {
        List<UserEntity> users = new ArrayList<>();

        when(userRepository.findAll()).thenReturn(users);

        assertThrows(NoUsersFoundException.class, () -> getAllUsersUseCase.getUsers());

        verify(userRepository).findAll();
    }
}
package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.domain.UserStatisticsDTO;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.AuthorityEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRoleStatisticsUseCaseImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserRoleStatisticsUseCaseImpl userRoleStatisticsUseCase;

    @Test
    void getRoleStatistics() {
        AuthorityEntity authorityGuest = new AuthorityEntity();
        authorityGuest.setRole("GUEST");
        AuthorityEntity authorityHost = new AuthorityEntity();
        authorityHost.setRole("HOST");

        UserEntity guest = new UserEntity();
        guest.setUserId(1);
        guest.setUsername("guest");
        guest.setAuthority(authorityGuest);

        UserEntity host = new UserEntity();
        host.setUserId(2);
        host.setUsername("host");
        host.setAuthority(authorityHost);

        when(userRepository.findAll()).thenReturn(List.of(guest, host));

        UserStatisticsDTO statisticsDTO = userRoleStatisticsUseCase.getRoleStatistics();

        assertEquals(1, statisticsDTO.getGuestCount());
        assertEquals(1, statisticsDTO.getHostCount());

        verify(userRepository, times(2)).findAll();
    }
}
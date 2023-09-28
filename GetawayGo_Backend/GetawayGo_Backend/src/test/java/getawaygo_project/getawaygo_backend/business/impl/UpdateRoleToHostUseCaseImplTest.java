package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.exception.RoleNotFoundOrValidException;
import getawaygo_project.getawaygo_backend.business.exception.UnauthorizedDataException;
import getawaygo_project.getawaygo_backend.business.exception.UserNotFoundException;
import getawaygo_project.getawaygo_backend.domain.AccessToken;
import getawaygo_project.getawaygo_backend.persistance.AuthorityRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateRoleToHostUseCaseImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthorityRepository authorityRepository;
    @Mock
    private AccessToken accessToken;
    @InjectMocks
    private UpdateRoleToHostUseCaseImpl updateRoleToHostUseCase;

    @Test
    void updateRole() {
        AuthorityEntity authority = new AuthorityEntity();
        authority.setRole("GUEST");

        AuthorityEntity authorityNew = new AuthorityEntity();
        authority.setRole("HOST");

        UserEntity updateUser = new UserEntity();
        updateUser.setUsername("anikadurinaa");
        updateUser.setUserId(1);
        updateUser.setAuthority(authority);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(updateUser));
        when(authorityRepository.findByRole(anyString())).thenReturn(authorityNew);
        when(accessToken.getUserId()).thenReturn(updateUser.getUserId());


        updateRoleToHostUseCase.updateRole(1);

        assertEquals(authorityNew, updateUser.getAuthority());
        verify(userRepository).findById(anyLong());
        verify(authorityRepository).findByRole(anyString());
        verify(accessToken).getUserId();
    }
    @Test
    void updateRoleUserNotFound() {
        AuthorityEntity authority = new AuthorityEntity();
        authority.setRole("GUEST");

        UserEntity updateUser = new UserEntity();
        updateUser.setUsername("anikadurinaa");
        updateUser.setUserId(1);
        updateUser.setAuthority(authority);

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(accessToken.getUserId()).thenReturn(updateUser.getUserId());

        assertThrows(UserNotFoundException.class, () -> updateRoleToHostUseCase.updateRole(1));
        verify(userRepository).findById(anyLong());
        verify(accessToken).getUserId();
    }
    @Test
    void updateRoleUnauthorizedException() {
        AuthorityEntity authority = new AuthorityEntity();
        authority.setRole("GUEST");

        UserEntity updateUser = new UserEntity();
        updateUser.setUsername("anikadurinaa");
        updateUser.setUserId(1L);
        updateUser.setAuthority(authority);

        when(accessToken.getUserId()).thenReturn(2L);

        assertThrows(UnauthorizedDataException.class, () -> updateRoleToHostUseCase.updateRole(1L));

        verify(accessToken).getUserId();

    }
    @Test
    void updateRoleAuthorityException() {
        AuthorityEntity authority = new AuthorityEntity();
        authority.setRole("GUEST");

        UserEntity updateUser = new UserEntity();
        updateUser.setUsername("anikadurinaa");
        updateUser.setUserId(1);
        updateUser.setAuthority(authority);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(updateUser));
        when(accessToken.getUserId()).thenReturn(updateUser.getUserId());
        when(authorityRepository.findByRole(anyString())).thenReturn(null);

        assertThrows(RoleNotFoundOrValidException.class, () -> updateRoleToHostUseCase.updateRole(1));

        verify(userRepository).findById(anyLong());
        verify(authorityRepository).findByRole(anyString());
        verify(accessToken).getUserId();

    }
}
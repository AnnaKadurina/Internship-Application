package getawaygo_project.getawaygo_backend.controller;

import getawaygo_project.getawaygo_backend.business.*;
import getawaygo_project.getawaygo_backend.business.exception.*;
import getawaygo_project.getawaygo_backend.domain.*;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private GetAllUsersUseCase getAllUsersUseCase;
    @Mock
    private GetUserUseCase getUserUseCase;
    @Mock
    private CreateUserUseCase createUserUseCase;
    @Mock
    private UpdateUserUseCase updateUserUseCase;
    @Mock
    private DeleteUserUseCase deleteUserUseCase;
    @Mock
    private DeactivateUserUseCase deactivateUserUseCase;
    @Mock
    private ActivateUserUseCase activateUserUseCase;
    @Mock
    private UploadPictureUseCase uploadPictureUseCase;
    @Mock
    private UpdateRoleToHostUseCase updateRoleToHostUseCase;
    @Mock
    private LoginUseCase loginUseCase;
    @Mock
    private GetUserDTOUseCase getUserDTOUseCase;
    @InjectMocks
    private UserController userController;

    @Test
    void login() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("anikadurinaa");
        loginRequest.setPassword("test");

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken("token");

        when(loginUseCase.login(loginRequest)).thenReturn(loginResponse);

        ResponseEntity<LoginResponse> login = userController.login(loginRequest);

        assertEquals(login.getBody(), loginResponse);
        assertEquals(HttpStatus.OK, login.getStatusCode());
        verify(loginUseCase).login(loginRequest);

    }

    @Test
    void getAllUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());
        GetAllUsersResponse getResponse = new GetAllUsersResponse(userList);

        when(getAllUsersUseCase.getUsers()).thenReturn(getResponse);

        ResponseEntity<GetAllUsersResponse> users = userController.getAllUsers();

        assertEquals(getResponse, users.getBody());
        assertEquals(HttpStatus.OK, users.getStatusCode());
        verify(getAllUsersUseCase).getUsers();

    }

    @Test
    void getAllUsersNoUsersFound() {
        when(getAllUsersUseCase.getUsers()).thenThrow(new NoUsersFoundException());

        assertThrows(NoUsersFoundException.class, () -> userController.getAllUsers());

        verify(getAllUsersUseCase).getUsers();

    }

    @Test
    void getUser() {
        User user = new User();
        user.setUsername("test");
        user.setUserId(1);

        when(getUserUseCase.getUser(user.getUserId())).thenReturn(Optional.of(user));

        ResponseEntity<User> getUser = userController.getUser(user.getUserId());

        assertEquals(getUser.getBody(), user);
        assertEquals(getUser.getBody().getUsername(), user.getUsername());
        assertEquals(HttpStatus.OK, getUser.getStatusCode());
        verify(getUserUseCase).getUser(user.getUserId());

    }
    @Test
    void getUserDto() {
        Long id = 1L;
        UserDTO user = new UserDTO();
        user.setUsername("test");
        user.setPhoto("url");

        when(getUserDTOUseCase.getUserDto(id)).thenReturn(user);

        ResponseEntity<UserDTO> getUser = userController.getUserDTO(id);

        assertEquals(user, getUser.getBody());
        assertEquals(user.getUsername(), getUser.getBody().getUsername());
        assertEquals(HttpStatus.OK, getUser.getStatusCode());

        verify(getUserDTOUseCase).getUserDto(id);

    }

    @Test
    void getUserNoUserFound() {

        Long id = 1L;

        when(getUserUseCase.getUser(id)).thenThrow(new UserNotFoundException());

        assertThrows(UserNotFoundException.class, () -> userController.getUser(id));
        verify(getUserUseCase).getUser(id);

    }

    @Test
    void updateRole() {
        User user = new User();
        user.setUsername("test");
        user.setUserId(1);

        doNothing().when(updateRoleToHostUseCase).updateRole(user.getUserId());
        ResponseEntity<Void> update = userController.updateRole(user.getUserId());

        assertEquals(HttpStatus.NO_CONTENT, update.getStatusCode());
        verify(updateRoleToHostUseCase).updateRole(user.getUserId());
    }

    @Test
    void updateRoleUserNotFound() {
        Long id = 1L;

        doThrow(new UserNotFoundException()).when(updateRoleToHostUseCase).updateRole(id);

        assertThrows(UserNotFoundException.class, () -> userController.updateRole(id));

        verify(updateRoleToHostUseCase).updateRole(id);
    }

    @Test
    void updateRoleException() {
        Long id = 1L;

        doThrow(new RoleNotFoundOrValidException()).when(updateRoleToHostUseCase).updateRole(id);

        assertThrows(RoleNotFoundOrValidException.class, () -> userController.updateRole(id));

        verify(updateRoleToHostUseCase).updateRole(id);
    }

    @Test
    void deactivateUser() {
        User user = new User();
        user.setUsername("test");
        user.setUserId(1);

        doNothing().when(deactivateUserUseCase).deactivateUser(user.getUserId());
        ResponseEntity<Void> deactivate = userController.deactivateUser(user.getUserId());

        assertEquals(HttpStatus.NO_CONTENT, deactivate.getStatusCode());
        verify(deactivateUserUseCase).deactivateUser(user.getUserId());
    }

    @Test
    void deactivateUserNotFound() {
        Long id = 1L;

        doThrow(new UserNotFoundException()).when(deactivateUserUseCase).deactivateUser(id);

        assertThrows(UserNotFoundException.class, () -> userController.deactivateUser(id));

        verify(deactivateUserUseCase).deactivateUser(id);
    }

    @Test
    void activateUser() {
        User user = new User();
        user.setUsername("test");
        user.setUserId(1);

        doNothing().when(activateUserUseCase).activateUser(user.getUserId());
        ResponseEntity<Void> activate = userController.activateUser(user.getUserId());

        assertEquals(HttpStatus.NO_CONTENT, activate.getStatusCode());
        verify(activateUserUseCase).activateUser(user.getUserId());
    }

    @Test
    void activateUserNotFound() {
        Long id = 1L;

        doThrow(new UserNotFoundException()).when(activateUserUseCase).activateUser(id);

        assertThrows(UserNotFoundException.class, () -> userController.activateUser(id));

        verify(activateUserUseCase).activateUser(id);
    }

    @Test
    void createUser() {
        CreateUserRequest request = CreateUserRequest.builder()
                .username("anikadurinaa")
                .build();
        User user = User.builder()
                .username(request.getUsername())
                .build();
        CreateUserResponse response = CreateUserResponse.builder()
                .userResponse(user)
                .build();

        when(createUserUseCase.createUser(request)).thenReturn(response);

        ResponseEntity<CreateUserResponse> createUserResponse = userController.createUser(request);

        assertEquals(HttpStatus.CREATED, createUserResponse.getStatusCode());
        assertEquals(request.getUsername(), createUserResponse.getBody().getUserResponse().getUsername());
        verify(createUserUseCase).createUser(request);
    }

    @Test
    void createUserUsernameAlreadyExists() {
        CreateUserRequest request = CreateUserRequest.builder()
                .username("anikadurinaa")
                .build();

        when(createUserUseCase.createUser(request)).thenThrow(new UsernameAlreadyExistsException());

        assertThrows(UsernameAlreadyExistsException.class, () -> userController.createUser(request));
        verify(createUserUseCase).createUser(request);
    }

    @Test
    void updateUser() {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .username("anikadurinaa")
                .address("Blg")
                .build();

        doNothing().when(updateUserUseCase).updateUser(request);
        ResponseEntity<Void> update = userController.updateUser(1, request);

        assertEquals(HttpStatus.NO_CONTENT, update.getStatusCode());
        verify(updateUserUseCase).updateUser(request);
    }

    @Test
    void updateUserUsernameAlreadyExists() {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .username("anikadurinaa")
                .address("Blg")
                .build();

        doThrow(new UsernameAlreadyExistsException()).when(updateUserUseCase).updateUser(request);

        assertThrows(UsernameAlreadyExistsException.class, () -> userController.updateUser(1, request));
        verify(updateUserUseCase).updateUser(request);
    }

    @Test
    void deleteUser() {
        User delete = User.builder()
                .userId(1)
                .build();

        doNothing().when(deleteUserUseCase).deleteUser(delete.getUserId());
        ResponseEntity<Void> deleteResult = userController.deleteUser(delete.getUserId());

        assertEquals(HttpStatus.NO_CONTENT, deleteResult.getStatusCode());
        verify(deleteUserUseCase).deleteUser(delete.getUserId());
    }

    @Test
    void deleteUserNotFound() {
        Long id = 1L;

        doThrow(new UserNotFoundException()).when(deleteUserUseCase).deleteUser(id);

        assertThrows(UserNotFoundException.class, () -> userController.deleteUser(id));

        verify(deleteUserUseCase).deleteUser(id);
    }

    @Test
    void uploadPhoto() {
        UserEntity updateUser = new UserEntity();
        updateUser.setUsername("anikadurinaa");
        updateUser.setUserId(1);

        String fileName = "test.jpg";
        String content = "test content";
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        MockMultipartFile photo = new MockMultipartFile(fileName, fileName, "image/jpg", bytes);

        doNothing().when(uploadPictureUseCase).uploadPicture(photo, updateUser.getUserId());
        ResponseEntity<Void> uploadPicture = userController.uploadPhoto(photo, updateUser.getUserId());

        assertEquals(HttpStatus.NO_CONTENT, uploadPicture.getStatusCode());
        verify(uploadPictureUseCase).uploadPicture(photo, updateUser.getUserId());
    }

    @Test
    void uploadPhotoFailed() {
        Long id = 1L;

        String fileName = "test.jpg";
        String content = "test content";
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        MockMultipartFile photo = new MockMultipartFile(fileName, fileName, "image/jpg", bytes);

        doThrow(new FileException()).when(uploadPictureUseCase).uploadPicture(photo, id);

        assertThrows(FileException.class, () -> userController.uploadPhoto(photo, id));

        verify(uploadPictureUseCase).uploadPicture(photo, id);
    }
}
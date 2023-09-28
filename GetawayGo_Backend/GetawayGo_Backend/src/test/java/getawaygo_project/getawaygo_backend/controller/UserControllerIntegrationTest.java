package getawaygo_project.getawaygo_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import getawaygo_project.getawaygo_backend.business.*;
import getawaygo_project.getawaygo_backend.business.exception.InvalidCredentialsException;
import getawaygo_project.getawaygo_backend.business.exception.NoUsersFoundException;
import getawaygo_project.getawaygo_backend.business.exception.UserNotFoundException;
import getawaygo_project.getawaygo_backend.domain.*;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GetAllUsersUseCase getAllUsersUseCase;
    @MockBean
    private GetUserUseCase getUserUseCase;
    @MockBean
    private CreateUserUseCase createUserUseCase;
    @MockBean
    private UpdateUserUseCase updateUserUseCase;
    @MockBean
    private DeleteUserUseCase deleteUserUseCase;
    @MockBean
    private DeactivateUserUseCase deactivateUserUseCase;
    @MockBean
    private ActivateUserUseCase activateUserUseCase;
    @MockBean
    private UpdateRoleToHostUseCase updateRoleToHostUseCase;
    @MockBean
    private LoginUseCase loginUseCase;
    @MockBean
    private UploadPictureUseCase uploadPictureUseCase;
    @MockBean
    private GetUserDTOUseCase getUserDTOUseCase;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllUsersReturns200() throws Exception {
        User getUser = User.builder()
                .userId(1)
                .username("Ani")
                .email("a.kadurina@gmail.com")
                .firstName("Anna")
                .lastName("Kadurina")
                .address("Blg")
                .phone("313131313")
                .photo("url")
                .role("GUEST")
                .active(true)
                .build();
        GetAllUsersResponse response = GetAllUsersResponse.builder().allUsers(List.of(getUser)).build();

        when(getAllUsersUseCase.getUsers()).thenReturn(response);


        mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json(""" 
                            {"allUsers": [
                                {
                                    "userId": 1,
                                    "username": "Ani",
                                    "email": "a.kadurina@gmail.com",
                                    "firstName": "Anna",
                                    "lastName": "Kadurina",
                                    "address": "Blg",
                                    "phone": "313131313",
                                    "photo": "url",
                                    "role": "GUEST",
                                    "active": true
                                }
                            ]
                        }"""));
        verify(getAllUsersUseCase, times(1)).getUsers();

    }

    @Test
    @WithMockUser(username = "guest", roles = {"GUEST"})
    void getAllUsersReturns403() throws Exception {
        mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isForbidden());

    }

    @Test
    void getAllUsersReturns401() throws Exception {
        mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllUsersReturns404() throws Exception {
        doThrow(new NoUsersFoundException()).when(getAllUsersUseCase).getUsers();

        mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isNotFound());

        verify(getAllUsersUseCase, times(1)).getUsers();
    }


    @Test
    @WithMockUser(username = "guest", roles = {"GUEST"})
    void getUserReturns200() throws Exception {
        User getUser = User.builder()
                .userId(1)
                .username("Ani")
                .firstName("Anna")
                .lastName("Kadurina")
                .email("a.kadurina@gmail.com")
                .phone("313131313")
                .photo("url")
                .active(true)
                .address("Blg")
                .role("GUEST")
                .build();

        when(getUserUseCase.getUser(getUser.getUserId())).thenReturn(Optional.of(getUser));

        mockMvc.perform(get("/users/{id}", getUser.getUserId()).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json(""" 
                                {
                                    "userId": 1,
                                    "username": "Ani",
                                    "email": "a.kadurina@gmail.com",
                                    "firstName": "Anna",
                                    "lastName": "Kadurina",
                                    "address": "Blg",
                                    "phone": "313131313",
                                    "photo": "url",
                                    "role": "GUEST",
                                    "active": true
                                }
                        """));

        verify(getUserUseCase, times(1)).getUser(getUser.getUserId());
    }

    @Test
    @WithMockUser(username = "guest", roles = {"GUEST"})
    void getUserReturns404() throws Exception {
        Long id = 1L;

        when(getUserUseCase.getUser(id)).thenThrow(new UserNotFoundException());

        mockMvc.perform(get("/users/{id}", id).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isNotFound());

        verify(getUserUseCase, times(1)).getUser(id);
    }

    @Test
    void getUserReturns401() throws Exception {
        Long id = 1L;

        mockMvc.perform(get("/users/{id}", id).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser(username = "guest", roles = {"GUEST"})
    void getUserDtoReturns200() throws Exception {
        Long id = 1L;
        UserDTO getUser = UserDTO.builder()
                .username("Ani")
                .photo("url")
                .build();

        when(getUserDTOUseCase.getUserDto(id)).thenReturn(getUser);

        mockMvc.perform(get("/users/details/{id}", id).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json(""" 
                                {
                                    "username": "Ani",
                                    "photo": "url"
                                }
                        """));

        verify(getUserDTOUseCase, times(1)).getUserDto(id);
    }

    @Test
    @WithMockUser(username = "guest", roles = {"GUEST"})
    void getUserDtoReturns404() throws Exception {
        Long id = 1L;

        when(getUserDTOUseCase.getUserDto(id)).thenThrow(new UserNotFoundException());

        mockMvc.perform(get("/users/details/{id}", id).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isNotFound());

        verify(getUserDTOUseCase, times(1)).getUserDto(id);
    }

    @Test
    void getUserDtoReturns401() throws Exception {
        Long id = 1L;

        mockMvc.perform(get("/users/details/{id}", id).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isUnauthorized());

    }

    @Test
    void createUserReturns201() throws Exception {
        User createUser = User.builder()
                .userId(1)
                .username("Ani")
                .firstName("Anna")
                .lastName("Kadurina")
                .email("a.kadurina@gmail.com")
                .phone("313131313")
                .photo("url")
                .active(true)
                .address("Blg")
                .role("GUEST")
                .build();

        CreateUserRequest request = CreateUserRequest.builder()
                .username("Ani")
                .firstName("Anna")
                .lastName("Kadurina")
                .email("a.kadurina@gmail.com")
                .phone("313131313")
                .photo("url")
                .address("Blg")
                .password("1234")
                .build();

        CreateUserResponse response = CreateUserResponse.builder().userResponse(createUser).build();

        when(createUserUseCase.createUser(request)).thenReturn(response);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJSON = ow.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE));

        verify(createUserUseCase, times(1)).createUser(request);

    }

    @Test
    void createUserReturns400() throws Exception {
        CreateUserRequest request = CreateUserRequest.builder()
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJSON = ow.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(username = "guest", roles = {"GUEST"})
    void updateUserReturns204() throws Exception {
        User user = User.builder()
                .userId(1)
                .username("Ani")
                .firstName("Anna")
                .lastName("Kadurina")
                .email("a.kadurina@gmail.com")
                .phone("313131313")
                .photo("url")
                .active(true)
                .address("Blg")
                .role("GUEST")
                .build();

        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(1)
                .username("Ani")
                .firstName("Anna")
                .lastName("Kadurina2")
                .email("a.kadurina@gmail.com")
                .phone("313131313")
                .address("Blg")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJSON = ow.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/{userId}", user.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON))
                .andExpect(status().isNoContent());

        verify(updateUserUseCase, times(1)).updateUser(request);

    }

    @Test
    @WithMockUser(username = "guest", roles = {"GUEST"})
    void updateUserReturns404() throws Exception {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(2)
                .username("Ani")
                .firstName("Anna")
                .lastName("Kadurina2")
                .email("a.kadurina@gmail.com")
                .phone("313131313")
                .address("Blg")
                .build();

        doThrow(new UserNotFoundException()).when(updateUserUseCase).updateUser(request);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJSON = ow.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/{userId}", request.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON))
                .andExpect(status().isNotFound());

        verify(updateUserUseCase, times(1)).updateUser(request);

    }

    @Test
    void updateUserReturns401() throws Exception {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(2)
                .username("Ani")
                .firstName("Anna")
                .lastName("Kadurina2")
                .email("a.kadurina@gmail.com")
                .phone("313131313")
                .address("Blg")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJSON = ow.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/{userId}", request.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteUserReturns204() throws Exception {
        User user = User.builder()
                .userId(1)
                .username("Ani")
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/users/{userId}", user.getUserId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(deleteUserUseCase, times(1)).deleteUser(user.getUserId());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteUserReturns404() throws Exception {
        User user = User.builder()
                .userId(1)
                .username("Ani")
                .build();

        doThrow(new UserNotFoundException()).when(deleteUserUseCase).deleteUser(user.getUserId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/users/{userId}", user.getUserId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(deleteUserUseCase, times(1)).deleteUser(user.getUserId());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"GUEST"})
    void deleteUserReturns403() throws Exception {
        User user = User.builder()
                .userId(1)
                .username("Ani")
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/users/{userId}", user.getUserId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @Test
    void deleteUserReturns401() throws Exception {
        User user = User.builder()
                .userId(1)
                .username("Ani")
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/users/{userId}", user.getUserId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    void loginReturns200() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("username");
        request.setPassword("password");

        LoginResponse response = new LoginResponse();
        response.setAccessToken("token");

        when(loginUseCase.login(request)).thenReturn(response);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJSON = ow.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON))
                .andExpect(status().isOk());
    }

    @Test
    void loginReturns400() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("username");
        request.setPassword("password");

        doThrow(new InvalidCredentialsException()).when(loginUseCase).login(request);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJSON = ow.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "guest", roles = {"GUEST"})
    void uploadPhotoReturns204() throws Exception {
        UserEntity updateUser = new UserEntity();
        updateUser.setUsername("anikadurinaa");
        updateUser.setUserId(1);

        String fileName = "test.jpg";
        String content = "test content";
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        MockMultipartFile photo = new MockMultipartFile(fileName, fileName, "image/jpg", bytes);

        doNothing().when(uploadPictureUseCase).uploadPicture(photo, updateUser.getUserId());

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.put("/users/photo/{id}", updateUser.getUserId())
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .content(bytes)
                        .param("id", String.valueOf(updateUser.getUserId()))
                        .param("photo", fileName)
                        .content(bytes);

        mockMvc.perform(builder).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "guest", roles = {"GUEST"})
    void updateRoleReturns204() throws Exception {
        User user = User.builder()
                .userId(1)
                .username("Ani")
                .firstName("Anna")
                .lastName("Kadurina")
                .email("a.kadurina@gmail.com")
                .phone("313131313")
                .photo("url")
                .active(true)
                .address("Blg")
                .role("GUEST")
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/host/{id}", user.getUserId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(updateRoleToHostUseCase, times(1)).updateRole(user.getUserId());

    }

    @Test
    void updateRoleReturns401() throws Exception {
        User user = User.builder()
                .userId(1)
                .username("Ani")
                .firstName("Anna")
                .lastName("Kadurina")
                .email("a.kadurina@gmail.com")
                .phone("313131313")
                .photo("url")
                .active(true)
                .address("Blg")
                .role("GUEST")
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/host/{id}", user.getUserId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser(username = "guest", roles = {"GUEST"})
    void updateRoleReturns404() throws Exception {
        User user = User.builder()
                .userId(1)
                .username("Ani")
                .firstName("Anna")
                .lastName("Kadurina")
                .email("a.kadurina@gmail.com")
                .phone("313131313")
                .photo("url")
                .active(true)
                .address("Blg")
                .role("GUEST")
                .build();

        doThrow(new UserNotFoundException()).when(updateRoleToHostUseCase).updateRole(user.getUserId());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/host/{id}", user.getUserId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(updateRoleToHostUseCase, times(1)).updateRole(user.getUserId());

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deactivateUserReturns204() throws Exception {
        User user = User.builder()
                .userId(1)
                .username("Ani")
                .firstName("Anna")
                .lastName("Kadurina")
                .email("a.kadurina@gmail.com")
                .phone("313131313")
                .photo("url")
                .active(true)
                .address("Blg")
                .role("GUEST")
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/deactivate/{id}", user.getUserId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(deactivateUserUseCase, times(1)).deactivateUser(user.getUserId());

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deactivateUserReturns404() throws Exception {
        User user = User.builder()
                .userId(1)
                .username("Ani")
                .firstName("Anna")
                .lastName("Kadurina")
                .email("a.kadurina@gmail.com")
                .phone("313131313")
                .photo("url")
                .active(true)
                .address("Blg")
                .role("GUEST")
                .build();

        doThrow(new UserNotFoundException()).when(deactivateUserUseCase).deactivateUser(user.getUserId());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/deactivate/{id}", user.getUserId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(deactivateUserUseCase, times(1)).deactivateUser(user.getUserId());

    }

    @Test
    @WithMockUser(username = "guest", roles = {"GUEST"})
    void deactivateUserReturns403() throws Exception {
        User user = User.builder()
                .userId(1)
                .username("Ani")
                .firstName("Anna")
                .lastName("Kadurina")
                .email("a.kadurina@gmail.com")
                .phone("313131313")
                .photo("url")
                .active(true)
                .address("Blg")
                .role("GUEST")
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/deactivate/{id}", user.getUserId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @Test
    void deactivateUserReturns401() throws Exception {
        User user = User.builder()
                .userId(1)
                .username("Ani")
                .firstName("Anna")
                .lastName("Kadurina")
                .email("a.kadurina@gmail.com")
                .phone("313131313")
                .photo("url")
                .active(true)
                .address("Blg")
                .role("GUEST")
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/deactivate/{id}", user.getUserId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void activateUserReturns204() throws Exception {
        User user = User.builder()
                .userId(1)
                .username("Ani")
                .firstName("Anna")
                .lastName("Kadurina")
                .email("a.kadurina@gmail.com")
                .phone("313131313")
                .photo("url")
                .active(true)
                .address("Blg")
                .role("GUEST")
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/activate/{id}", user.getUserId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(activateUserUseCase, times(1)).activateUser(user.getUserId());

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void activateUserReturns404() throws Exception {
        User user = User.builder()
                .userId(1)
                .username("Ani")
                .firstName("Anna")
                .lastName("Kadurina")
                .email("a.kadurina@gmail.com")
                .phone("313131313")
                .photo("url")
                .active(true)
                .address("Blg")
                .role("GUEST")
                .build();

        doThrow(new UserNotFoundException()).when(activateUserUseCase).activateUser(user.getUserId());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/activate/{id}", user.getUserId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(activateUserUseCase, times(1)).activateUser(user.getUserId());

    }

    @Test
    @WithMockUser(username = "guest", roles = {"GUEST"})
    void activateUserReturns403() throws Exception {
        User user = User.builder()
                .userId(1)
                .username("Ani")
                .firstName("Anna")
                .lastName("Kadurina")
                .email("a.kadurina@gmail.com")
                .phone("313131313")
                .photo("url")
                .active(true)
                .address("Blg")
                .role("GUEST")
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/activate/{id}", user.getUserId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @Test
    void activateUserReturns401() throws Exception {
        User user = User.builder()
                .userId(1)
                .username("Ani")
                .firstName("Anna")
                .lastName("Kadurina")
                .email("a.kadurina@gmail.com")
                .phone("313131313")
                .photo("url")
                .active(true)
                .address("Blg")
                .role("GUEST")
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/activate/{id}", user.getUserId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }
}
package getawaygo_project.getawaygo_backend.controller;

import getawaygo_project.getawaygo_backend.business.*;
import getawaygo_project.getawaygo_backend.configuration.isauthenticated.IsAuthenticated;
import getawaygo_project.getawaygo_backend.domain.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class UserController {
    private final GetAllUsersUseCase getAllUsersUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final GetUserUseCase getUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final UploadPictureUseCase uploadPictureUseCase;
    private final DeactivateUserUseCase deactivateUserUseCase;
    private final ActivateUserUseCase activateUserUseCase;
    private final UpdateRoleToHostUseCase updateRoleToHostUseCase;
    private final LoginUseCase loginUseCase;
    private final GetUserDTOUseCase getUserDTOUseCase;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse = loginUseCase.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @IsAuthenticated
    @RolesAllowed("ROLE_ADMIN")
    @GetMapping
    public ResponseEntity<GetAllUsersResponse> getAllUsers() {
        GetAllUsersResponse response = getAllUsersUseCase.getUsers();
        return ResponseEntity.ok(response);
    }

    @IsAuthenticated
    @GetMapping("{id}")
    public ResponseEntity<User> getUser(@PathVariable(value = "id") final long id) {
        final Optional<User> user = getUserUseCase.getUser(id);
        return ResponseEntity.ok().body(user.get());
    }
    @IsAuthenticated
    @GetMapping("/details/{id}")
    public ResponseEntity<UserDTO> getUserDTO(@PathVariable(value = "id") final long id) {
        UserDTO user = getUserDTOUseCase.getUserDto(id);
        return ResponseEntity.ok().body(user);
    }
    @IsAuthenticated
    @PutMapping("/photo/{id}")
    public ResponseEntity<Void> uploadPhoto(@ModelAttribute MultipartFile photo, @PathVariable(value = "id") final long id) {
        uploadPictureUseCase.uploadPicture(photo, id);
        return ResponseEntity.noContent().build();
    }

    @IsAuthenticated
    @RolesAllowed("ROLE_GUEST")
    @PutMapping("/host/{id}")
    public ResponseEntity<Void> updateRole(@PathVariable(value = "id") final long id) {
        updateRoleToHostUseCase.updateRole(id);
        return ResponseEntity.noContent().build();
    }

    @IsAuthenticated
    @RolesAllowed("ROLE_ADMIN")
    @PutMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivateUser(@PathVariable long id) {
        deactivateUserUseCase.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }

    @IsAuthenticated
    @RolesAllowed("ROLE_ADMIN")
    @PutMapping("/activate/{id}")
    public ResponseEntity<Void> activateUser(@PathVariable(value = "id") final long id) {
        activateUserUseCase.activateUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping()
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        CreateUserResponse response = createUserUseCase.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @IsAuthenticated
    @PutMapping("{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable("userId") long id,
                                           @RequestBody @Valid UpdateUserRequest request) {
        request.setId(id);
        updateUserUseCase.updateUser(request);
        return ResponseEntity.noContent().build();
    }

    @IsAuthenticated
    @RolesAllowed("ROLE_ADMIN")
    @DeleteMapping("{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable long userId) {
        deleteUserUseCase.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}

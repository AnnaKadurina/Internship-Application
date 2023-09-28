package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.CreateUserUseCase;
import getawaygo_project.getawaygo_backend.business.exception.*;
import getawaygo_project.getawaygo_backend.domain.CreateUserRequest;
import getawaygo_project.getawaygo_backend.domain.CreateUserResponse;
import getawaygo_project.getawaygo_backend.persistance.AuthorityRepository;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.AuthorityEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class CreateUserUseCaseImpl implements CreateUserUseCase {
    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public CreateUserResponse createUser(CreateUserRequest userRequest) {
        if (userRepository.findByUsername(userRequest.getUsername()) != null) {
            throw new UsernameAlreadyExistsException();
        }
        if (userRepository.findByEmail(userRequest.getEmail()) != null) {
            throw new EmailAlreadyExistsException();
        }
        AuthorityEntity guestAuthority = authorityRepository.findByRole("GUEST");
        if (guestAuthority == null) {
            throw new RoleNotFoundOrValidException();
        }
        String emailRegex = "^[^@]+@[^@]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(userRequest.getEmail());
        if (!matcher.matches()) {
            throw new InvalidEmailException();
        }

        String passwordRegex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";
        Pattern passwordPattern = Pattern.compile(passwordRegex);
        Matcher passwordMatcher = passwordPattern.matcher(userRequest.getPassword());
        if (!passwordMatcher.matches()) {
            throw new InvalidPasswordException();
        }

        String encodedPassword = passwordEncoder.encode(userRequest.getPassword());
        UserEntity createdUser = new UserEntity();
        createdUser.setUsername(userRequest.getUsername());
        createdUser.setFirstName(userRequest.getFirstName());
        createdUser.setLastName(userRequest.getLastName());
        createdUser.setPhone(userRequest.getPhone());
        createdUser.setPhoto(userRequest.getPhoto());
        createdUser.setAddress(userRequest.getAddress());
        createdUser.setEmail(userRequest.getEmail());
        createdUser.setPassword(encodedPassword);
        createdUser.setActive(true);
        createdUser.setAuthority(guestAuthority);

        userRepository.save(createdUser);
        return CreateUserResponse.builder()
                .userResponse(UserConverter.convert(createdUser)).build();

    }
}

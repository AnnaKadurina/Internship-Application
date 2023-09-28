package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.exception.UserNotFoundException;
import getawaygo_project.getawaygo_backend.domain.CreatePropertyRequest;
import getawaygo_project.getawaygo_backend.domain.CreatePropertyResponse;
import getawaygo_project.getawaygo_backend.persistance.PropertyRepository;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
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
class CreatePropertyUseCaseImplTest {
    @Mock
    private PropertyRepository propertyRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CreatePropertyUseCaseImpl createPropertyUseCase;

    @Test
    void CreateProperty() {
        UserEntity user = new UserEntity();
        user.setUserId(3);
        CreatePropertyRequest request = CreatePropertyRequest.builder()
                .name("Test")
                .address("Test")
                .town("Blagoev")
                .country("Bulgaria")
                .nrOfRooms(3)
                .price(50)
                .description("Test")
                .userId(user.getUserId())
                .build();

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        CreatePropertyResponse response = createPropertyUseCase.createProperty(request);

        assertEquals(request.getName(), response.getPropertyResponse().getName());

        verify(userRepository).findById(user.getUserId());

    }

    @Test
    void CreatePropertyUserNotFound() {
        UserEntity user = new UserEntity();
        user.setUserId(3);
        CreatePropertyRequest request = CreatePropertyRequest.builder()
                .name("Test")
                .address("Test")
                .town("Blagoev")
                .country("Bulgaria")
                .nrOfRooms(3)
                .price(50)
                .description("Test")
                .userId(user.getUserId())
                .build();

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> createPropertyUseCase.createProperty(request));

        verify(userRepository).findById(user.getUserId());
    }

}
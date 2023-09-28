package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.exception.PropertyIsNotFoundException;
import getawaygo_project.getawaygo_backend.domain.UpdatePropertyRequest;
import getawaygo_project.getawaygo_backend.persistance.PropertyRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
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
class UpdatePropertyUseCaseImplTest {
    @Mock
    private PropertyRepository propertyRepository;
    @InjectMocks
    private UpdatePropertyUseCaseImpl updatePropertyUseCase;

    @Test
    void updateProperty() {
        UserEntity user = new UserEntity();
        user.setUserId(3);
        PropertyEntity propertyUpdate = new PropertyEntity();
        propertyUpdate.setPropertyId(1);
        propertyUpdate.setName("Test");
        propertyUpdate.setAddress("Test");
        propertyUpdate.setUser(user);

        UpdatePropertyRequest request = UpdatePropertyRequest.builder()
                .id(1)
                .name("TestUpdate")
                .address("Test")
                .build();

        when(propertyRepository.findById(1L)).thenReturn(Optional.of(propertyUpdate));

        updatePropertyUseCase.updateProperty(request);

        assertEquals("TestUpdate", propertyUpdate.getName());
        verify(propertyRepository).findById(1L);


    }

    @Test
    void updatePropertyNotFound() {
        UserEntity user = new UserEntity();
        user.setUserId(3);
        PropertyEntity propertyUpdate = new PropertyEntity();
        propertyUpdate.setPropertyId(1);
        propertyUpdate.setName("Test");
        propertyUpdate.setAddress("Test");
        propertyUpdate.setUser(user);

        UpdatePropertyRequest request = UpdatePropertyRequest.builder()
                .id(1)
                .name("TestUpdate")
                .address("Test")
                .build();

        when(propertyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PropertyIsNotFoundException.class, () -> updatePropertyUseCase.updateProperty(request));

        verify(propertyRepository).findById(1L);
    }
}
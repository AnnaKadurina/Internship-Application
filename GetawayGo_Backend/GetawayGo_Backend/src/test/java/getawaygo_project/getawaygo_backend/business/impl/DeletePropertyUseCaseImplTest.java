package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.exception.PropertyIsNotFoundException;
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
class DeletePropertyUseCaseImplTest {
    @Mock
    private PropertyRepository propertyRepository;
    @InjectMocks
    private DeletePropertyUseCaseImpl deletePropertyUseCase;

    @Test
    void deleteProperty() {
        UserEntity user = new UserEntity();
        user.setUserId(3);
        PropertyEntity propertyDelete = new PropertyEntity();
        propertyDelete.setPropertyId(1);
        propertyDelete.setName("Test");
        propertyDelete.setAddress("Test");
        propertyDelete.setUser(user);
        propertyDelete.setActive(true);

        when(propertyRepository.findById(propertyDelete.getPropertyId())).thenReturn(Optional.of(propertyDelete));
        deletePropertyUseCase.deleteProperty(propertyDelete.getPropertyId());

        assertEquals(false, propertyDelete.getActive());

        verify(propertyRepository).findById(propertyDelete.getPropertyId());
    }

    @Test
    void deletePropertyNotFound() {
        Long propertyId = 1L;

        when(propertyRepository.findById(propertyId)).thenReturn(Optional.empty());

        assertThrows(PropertyIsNotFoundException.class, () -> deletePropertyUseCase.deleteProperty(propertyId));
        verify(propertyRepository).findById(propertyId);

    }
}
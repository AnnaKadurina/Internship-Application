package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.exception.PropertyIsNotFoundException;
import getawaygo_project.getawaygo_backend.domain.Property;
import getawaygo_project.getawaygo_backend.persistance.PropertyRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyPhotoEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPropertyUseCaseImplTest {
    @Mock
    private PropertyRepository propertyRepository;
    @InjectMocks
    private GetPropertyUseCaseImpl getPropertyUseCase;

    @Test
    void getProperty() {
        PropertyPhotoEntity photoEntity = new PropertyPhotoEntity();
        photoEntity.setUrl("url");
        photoEntity.setId(1L);

        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);
        property.setName("Test");
        property.setActive(true);
        property.setUser(new UserEntity());
        property.setPhotos(List.of(photoEntity));

        when(propertyRepository.findById(property.getPropertyId())).thenReturn(Optional.of(property));

        Optional<Property> getProperty = getPropertyUseCase.getProperty(property.getPropertyId());

        assertEquals(getProperty.get().getPropertyId(), property.getPropertyId());

        verify(propertyRepository).findById(property.getPropertyId());
    }

    @Test
    void getPropertyNotFound() {
        Long id = 1L;

        when(propertyRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PropertyIsNotFoundException.class, () -> getPropertyUseCase.getProperty(id));

        verify(propertyRepository).findById(id);
    }

    @Test
    void getPropertyInactive() {
        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);
        property.setName("Test");
        property.setActive(false);
        property.setUser(new UserEntity());

        Long id = property.getPropertyId();

        when(propertyRepository.findById(property.getPropertyId())).thenReturn(Optional.of(property));

        assertThrows(PropertyIsNotFoundException.class, () -> getPropertyUseCase.getProperty(id));

        verify(propertyRepository).findById(property.getPropertyId());

    }
}
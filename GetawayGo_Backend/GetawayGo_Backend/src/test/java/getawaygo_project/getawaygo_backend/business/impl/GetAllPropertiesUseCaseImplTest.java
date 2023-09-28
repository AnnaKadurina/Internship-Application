package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.exception.NoPropertiesFoundException;
import getawaygo_project.getawaygo_backend.domain.GetAllPropertiesResponse;
import getawaygo_project.getawaygo_backend.persistance.PropertyRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllPropertiesUseCaseImplTest {
    @Mock
    private PropertyRepository propertyRepository;
    @InjectMocks
    private GetAllPropertiesUseCaseImpl getAllPropertiesUseCase;

    @Test
    void getProperties() {
        PropertyEntity property1 = new PropertyEntity();
        property1.setPropertyId(1);
        property1.setName("Test");
        property1.setActive(true);
        property1.setUser(new UserEntity());

        PropertyEntity property2 = new PropertyEntity();
        property2.setPropertyId(2);
        property2.setName("Test2");
        property2.setActive(false);
        property2.setUser(new UserEntity());

        List<PropertyEntity> properties = new ArrayList<>();
        properties.add(property1);
        properties.add(property2);

        when(propertyRepository.findAll()).thenReturn(properties);

        GetAllPropertiesResponse response = getAllPropertiesUseCase.getProperties();

        assertEquals(1, response.getAllProperties().size());
        assertEquals(1, response.getAllProperties().stream().findFirst().get().getPropertyId());

        verify(propertyRepository).findAll();
    }

    @Test
    void getPropertiesNoFound() {
        List<PropertyEntity> properties = new ArrayList<>();

        when(propertyRepository.findAll()).thenReturn(properties);

        assertThrows(NoPropertiesFoundException.class, () -> getAllPropertiesUseCase.getProperties());

        verify(propertyRepository).findAll();
    }

}
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
class GetPropertiesByCountryUseCaseImplTest {
    @Mock
    private PropertyRepository propertyRepository;
    @InjectMocks
    private GetPropertiesByCountryUseCaseImpl getPropertiesByCountryUseCase;

    @Test
    void getPropertiesByCountry() {
        PropertyEntity property1 = new PropertyEntity();
        property1.setPropertyId(1);
        property1.setName("Test");
        property1.setCountry("Bulgaria");
        property1.setActive(true);
        property1.setUser(new UserEntity());

        PropertyEntity property2 = new PropertyEntity();
        property2.setPropertyId(2);
        property2.setName("Test2");
        property2.setCountry("Netherlands");
        property2.setActive(true);
        property2.setUser(new UserEntity());

        PropertyEntity property3 = new PropertyEntity();
        property3.setPropertyId(1);
        property3.setName("Test");
        property3.setCountry("Bulgaria");
        property3.setActive(false);
        property3.setUser(new UserEntity());

        List<PropertyEntity> properties = new ArrayList<>();
        properties.add(property1);
        properties.add(property2);
        properties.add(property3);

        when(propertyRepository.findAll()).thenReturn(properties);

        GetAllPropertiesResponse response = getPropertiesByCountryUseCase.getPropertiesByCountry("Bulgaria");

        assertEquals(1, response.getAllProperties().size());

        verify(propertyRepository).findAll();
    }

    @Test
    void getPropertiesByCountryNotFound() {
        PropertyEntity property1 = new PropertyEntity();
        property1.setPropertyId(1);
        property1.setName("Test");
        property1.setCountry("Bulgaria");
        property1.setActive(true);
        property1.setUser(new UserEntity());

        PropertyEntity property2 = new PropertyEntity();
        property2.setPropertyId(2);
        property2.setName("Test2");
        property2.setCountry("Netherlands");
        property2.setActive(true);
        property2.setUser(new UserEntity());

        List<PropertyEntity> properties = new ArrayList<>();
        properties.add(property1);
        properties.add(property2);

        when(propertyRepository.findAll()).thenReturn(properties);

        assertThrows(NoPropertiesFoundException.class, () -> getPropertiesByCountryUseCase.getPropertiesByCountry("Romania"));

        verify(propertyRepository).findAll();
    }
}
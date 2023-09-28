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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilterPropertiesUseCaseImplTest {
    @Mock
    private PropertyRepository propertyRepository;
    @InjectMocks
    private FilterPropertiesUseCaseImpl filterPropertiesUseCase;

    @Test
    void getFilteredProperties() {
        PropertyEntity property1 = new PropertyEntity();
        property1.setPropertyId(1);
        property1.setName("Test");
        property1.setTown("Blg");
        property1.setCountry("Bulgaria");
        property1.setPrice(90);
        property1.setActive(true);
        property1.setUser(new UserEntity());

        PropertyEntity property2 = new PropertyEntity();
        property2.setPropertyId(2);
        property2.setName("Test2");
        property1.setTown("Blg");
        property2.setCountry("Netherlands");
        property2.setPrice(90);
        property2.setActive(true);
        property2.setUser(new UserEntity());

        List<PropertyEntity> properties = new ArrayList<>();
        properties.add(property1);
        properties.add(property2);

        when(propertyRepository.findByPriceAndTown(100, "Blg")).thenReturn(properties);

        GetAllPropertiesResponse response = filterPropertiesUseCase.getFilteredProperties(100, "Blg");

        assertEquals(2, response.getAllProperties().size());

        verify(propertyRepository).findByPriceAndTown(100, "Blg");
    }

    @Test
    void getFilteredPropertiesNoFound() {
        when(propertyRepository.findByPriceAndTown(100, "Blg")).thenReturn(Collections.emptyList());

        assertThrows(NoPropertiesFoundException.class, () -> filterPropertiesUseCase.getFilteredProperties(100, "Blg"));

        verify(propertyRepository).findByPriceAndTown(100, "Blg");
    }
}
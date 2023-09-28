package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.domain.PropertyByCountryStatisticsDTO;
import getawaygo_project.getawaygo_backend.persistance.PropertyRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PropertiesByCountryStatisticsUseCaseImplTest {
    @Mock
    private PropertyRepository propertyRepository;
    @InjectMocks
    private PropertiesByCountryStatisticsUseCaseImpl propertiesByCountryStatisticsUseCase;

    @Test
    void getPropertyStatistics() {
        PropertyEntity property1 = new PropertyEntity();
        property1.setPropertyId(1);
        property1.setCountry("BG");

        PropertyEntity property2 = new PropertyEntity();
        property2.setPropertyId(2);
        property2.setCountry("BG");

        PropertyEntity property3 = new PropertyEntity();
        property3.setPropertyId(3);
        property3.setCountry("NL");

        when(propertyRepository.findAll()).thenReturn(List.of(property1, property2, property3));

        List<PropertyByCountryStatisticsDTO> top10 = propertiesByCountryStatisticsUseCase.getPropertyStatistics();

        assertEquals("BG", top10.get(0).getCountry());
        assertEquals(2, top10.get(0).getPropertyCount());

        verify(propertyRepository).findAll();
    }
}
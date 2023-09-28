package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.persistance.PropertyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetHighestPropertyPriceImplTest {
    @Mock
    private PropertyRepository propertyRepository;
    @InjectMocks
    private GetHighestPropertyPriceImpl getHighestPropertyPrice;

    @Test
    void getHighestPropertyPrice() {
        double max = 100;

        when(propertyRepository.findHighestPrice()).thenReturn(max);

        Double getMax = getHighestPropertyPrice.getHighestPropertyPrice();

        assertEquals(100, getMax);

        verify(propertyRepository).findHighestPrice();

    }

}
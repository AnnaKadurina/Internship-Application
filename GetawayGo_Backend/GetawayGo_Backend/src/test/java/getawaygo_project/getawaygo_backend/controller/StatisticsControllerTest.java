package getawaygo_project.getawaygo_backend.controller;

import getawaygo_project.getawaygo_backend.business.BookedPropertiesStatisticsUseCase;
import getawaygo_project.getawaygo_backend.business.PropertiesByCountryStatisticsUseCase;
import getawaygo_project.getawaygo_backend.business.ReviewStatisticsUseCase;
import getawaygo_project.getawaygo_backend.business.UserRoleStatisticsUseCase;
import getawaygo_project.getawaygo_backend.domain.BookedPropertyStatisticsDTO;
import getawaygo_project.getawaygo_backend.domain.PropertyByCountryStatisticsDTO;
import getawaygo_project.getawaygo_backend.domain.ReviewStatisticsDTO;
import getawaygo_project.getawaygo_backend.domain.UserStatisticsDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatisticsControllerTest {
    @Mock
    private UserRoleStatisticsUseCase userRoleStatisticsUseCase;
    @Mock
    private PropertiesByCountryStatisticsUseCase propertiesByCountryStatisticsUseCase;
    @Mock
    private ReviewStatisticsUseCase reviewStatisticsUseCase;
    @Mock
    private BookedPropertiesStatisticsUseCase bookedPropertiesStatisticsUseCase;
    @InjectMocks
    private StatisticsController statisticsController;

    @Test
    void getUserRoleStatistics() {
        UserStatisticsDTO statisticsDTO = new UserStatisticsDTO();
        statisticsDTO.setGuestCount(1);
        statisticsDTO.setHostCount(1);

        when(userRoleStatisticsUseCase.getRoleStatistics()).thenReturn(statisticsDTO);

        ResponseEntity<UserStatisticsDTO> getStatistics = statisticsController.getUserRoleStatistics();

        assertEquals(statisticsDTO, getStatistics.getBody());
        assertEquals(1, getStatistics.getBody().getGuestCount());
        assertEquals(HttpStatus.OK, getStatistics.getStatusCode());

        verify(userRoleStatisticsUseCase).getRoleStatistics();
    }

    @Test
    void getPropertiesByCountryStatistics() {
        PropertyByCountryStatisticsDTO statisticsDTO1 = new PropertyByCountryStatisticsDTO();
        statisticsDTO1.setCountry("BG");
        statisticsDTO1.setPropertyCount(1);

        PropertyByCountryStatisticsDTO statisticsDTO2 = new PropertyByCountryStatisticsDTO();
        statisticsDTO2.setCountry("NL");
        statisticsDTO2.setPropertyCount(1);

        when(propertiesByCountryStatisticsUseCase.getPropertyStatistics()).thenReturn(List.of(statisticsDTO1, statisticsDTO2));

        ResponseEntity<List<PropertyByCountryStatisticsDTO>> getStatistics = statisticsController.getPropertiesByCountryStatistics();

        assertEquals(statisticsDTO1, getStatistics.getBody().get(0));
        assertEquals(HttpStatus.OK, getStatistics.getStatusCode());

        verify(propertiesByCountryStatisticsUseCase).getPropertyStatistics();

    }

    @Test
    void getReviewStatistics() {
        ReviewStatisticsDTO statisticsDTO = new ReviewStatisticsDTO();
        statisticsDTO.setNegativeCount(1);
        statisticsDTO.setPositiveCount(0);

        when(reviewStatisticsUseCase.getReviewStatistics(1)).thenReturn(statisticsDTO);

        ResponseEntity<ReviewStatisticsDTO> getStatistics = statisticsController.getReviewStatistics(1);

        assertEquals(statisticsDTO, getStatistics.getBody());
        assertEquals(1, getStatistics.getBody().getNegativeCount());
        assertEquals(HttpStatus.OK, getStatistics.getStatusCode());

        verify(reviewStatisticsUseCase).getReviewStatistics(1);
    }

    @Test
    void getBookedPropertiesStatistics() {
        BookedPropertyStatisticsDTO statisticsDTO1 = new BookedPropertyStatisticsDTO();
        statisticsDTO1.setPropertyName("top");
        statisticsDTO1.setBookingCount(1);

        BookedPropertyStatisticsDTO statisticsDTO2 = new BookedPropertyStatisticsDTO();
        statisticsDTO2.setPropertyName("top2");
        statisticsDTO2.setBookingCount(1);

        when(bookedPropertiesStatisticsUseCase.getBookedPropertiesStatistics(1)).thenReturn(List.of(statisticsDTO1, statisticsDTO2));

        ResponseEntity<List<BookedPropertyStatisticsDTO>> getStatistics = statisticsController.getBookedPropertiesStatistics(1);

        assertEquals(statisticsDTO1, getStatistics.getBody().get(0));
        assertEquals(HttpStatus.OK, getStatistics.getStatusCode());

        verify(bookedPropertiesStatisticsUseCase).getBookedPropertiesStatistics(1);

    }
}
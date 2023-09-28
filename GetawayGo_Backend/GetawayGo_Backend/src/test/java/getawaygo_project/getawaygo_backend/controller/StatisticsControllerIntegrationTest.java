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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class StatisticsControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRoleStatisticsUseCase userRoleStatisticsUseCase;
    @MockBean
    private PropertiesByCountryStatisticsUseCase propertiesByCountryStatisticsUseCase;
    @MockBean
    private ReviewStatisticsUseCase reviewStatisticsUseCase;
    @MockBean
    private BookedPropertiesStatisticsUseCase bookedPropertiesStatisticsUseCase;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getUserRoleStatisticsReturns200() throws Exception {
        UserStatisticsDTO statisticsDTO = new UserStatisticsDTO();
        statisticsDTO.setHostCount(1);
        statisticsDTO.setGuestCount(1);

        when(userRoleStatisticsUseCase.getRoleStatistics()).thenReturn(statisticsDTO);

        mockMvc.perform(get("/statistics/users").contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json(""" 
                                {
                                    "hostCount": 1,
                                    "guestCount": 1
                                }
                        """));

        verify(userRoleStatisticsUseCase, times(1)).getRoleStatistics();

    }

    @Test
    @WithMockUser(username = "guest", roles = {"GUEST"})
    void getUserRoleStatisticsReturns403() throws Exception {
        mockMvc.perform(get("/statistics/users").contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getPropertiesByCountryStatisticsReturns200() throws Exception {
        PropertyByCountryStatisticsDTO statisticsDTO = new PropertyByCountryStatisticsDTO();
        statisticsDTO.setPropertyCount(1);
        statisticsDTO.setCountry("BG");

        when(propertiesByCountryStatisticsUseCase.getPropertyStatistics()).thenReturn(List.of(statisticsDTO));

        mockMvc.perform(get("/statistics/properties").contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE));

        verify(propertiesByCountryStatisticsUseCase, times(1)).getPropertyStatistics();

    }

    @Test
    void getPropertiesByCountryStatisticsReturns401() throws Exception {
        mockMvc.perform(get("/statistics/properties").contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser(username = "host", roles = {"HOST"})
    void getReviewStatisticsReturns200() throws Exception {
        ReviewStatisticsDTO statisticsDTO = new ReviewStatisticsDTO();
        statisticsDTO.setNegativeCount(1);
        statisticsDTO.setPositiveCount(0);

        when(reviewStatisticsUseCase.getReviewStatistics(1)).thenReturn(statisticsDTO);

        mockMvc.perform(get("/statistics/reviews/{hostId}", 1).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json(""" 
                                {
                                    "positiveCount": 0,
                                    "negativeCount": 1
                                }
                        """));

        verify(reviewStatisticsUseCase, times(1)).getReviewStatistics(1);

    }

    @Test
    @WithMockUser(username = "host", roles = {"HOST"})
    void getBookedPropertiesStatisticsReturns200() throws Exception {
        BookedPropertyStatisticsDTO statisticsDTO1 = new BookedPropertyStatisticsDTO();
        statisticsDTO1.setPropertyName("top");
        statisticsDTO1.setBookingCount(1);

        BookedPropertyStatisticsDTO statisticsDTO2 = new BookedPropertyStatisticsDTO();
        statisticsDTO2.setPropertyName("top2");
        statisticsDTO2.setBookingCount(1);

        when(bookedPropertiesStatisticsUseCase.getBookedPropertiesStatistics(1)).thenReturn(List.of(statisticsDTO1, statisticsDTO2));

        mockMvc.perform(get("/statistics/booked/properties/{hostId}", 1).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE));

        verify(bookedPropertiesStatisticsUseCase, times(1)).getBookedPropertiesStatistics(1);

    }
}
package getawaygo_project.getawaygo_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import getawaygo_project.getawaygo_backend.business.*;
import getawaygo_project.getawaygo_backend.business.exception.NoReviewsFoundException;
import getawaygo_project.getawaygo_backend.business.exception.PropertyIsNotFoundException;
import getawaygo_project.getawaygo_backend.domain.*;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ReviewControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CreateReviewUseCase createReviewUseCase;
    @MockBean
    private DeleteReviewUseCase deleteReviewUseCase;
    @MockBean
    private GetAllReviewsUseCase getAllReviewsUseCase;
    @MockBean
    private GetReviewsForPropertyUseCase getReviewsForPropertyUseCase;
    @MockBean
    private GetAverageReviewUseCase getAverageReviewUseCase;

    @Test
    @WithMockUser(username = "guest", roles = {"GUEST"})
    void createReviewReturns201() throws Exception {
        CreateReviewRequest request = CreateReviewRequest.builder()
                .propertyId(1L)
                .userId(1L)
                .text("Very nice")
                .rating(7)
                .build();

        Review review = Review.builder()
                .reviewId(1L)
                .propertyId(1L)
                .userId(1L)
                .text("Very nice")
                .rating(7)
                .date(Instant.now())
                .build();

        CreateReviewResponse response = CreateReviewResponse.builder().reviewResponse(review).build();

        when(createReviewUseCase.createReview(request)).thenReturn(response);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJSON = ow.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE));

        verify(createReviewUseCase, times(1)).createReview(request);

    }

    @Test
    @WithMockUser(username = "host", roles = {"HOST"})
    void createReviewReturns403() throws Exception {
        CreateReviewRequest request = CreateReviewRequest.builder()
                .propertyId(1L)
                .userId(1L)
                .text("Very nice")
                .rating(7)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJSON = ow.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void createReviewReturns401() throws Exception {
        CreateReviewRequest request = CreateReviewRequest.builder()
                .propertyId(1L)
                .userId(1L)
                .text("Very nice")
                .rating(7)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJSON = ow.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "guest", roles = {"GUEST"})
    void createReviewReturns400() throws Exception {
        CreateReviewRequest request = CreateReviewRequest.builder().build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJSON = ow.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllReviewsReturns200() throws Exception {
        Instant date = Instant.parse("2022-10-10T00:00:00Z");

        Review review = Review.builder()
                .reviewId(1L)
                .propertyId(1L)
                .userId(1L)
                .text("Very nice")
                .rating(7)
                .date(date)
                .build();

        GetAllReviewsResponse response = GetAllReviewsResponse.builder().allReviews(List.of(review)).build();

        when(getAllReviewsUseCase.getReviews()).thenReturn(response);

        mockMvc.perform(get("/reviews").contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json(""" 
                         {
                          "allReviews": [
                              {
                                  "reviewId": 1,
                                  "propertyId": 1,
                                  "userId": 1,
                                  "text": "Very nice",
                                  "rating": 7,
                                  "date": "2022-10-10T00:00:00Z"
                              }
                          ]
                        }"""));
        verify(getAllReviewsUseCase, times(1)).getReviews();

    }

    @Test
    void getAverageReviewReturns200() throws Exception {
        AverageReviewDTO dto = new AverageReviewDTO();
        dto.setAverageRating(10);
        dto.setTotalCount(1);

        when(getAverageReviewUseCase.getAverageReview(1L)).thenReturn(dto);

        mockMvc.perform(get("/reviews/average/{propertyId}", 1).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE));

        verify(getAverageReviewUseCase, times(1)).getAverageReview(1L);

    }

    @Test
    void getAllReviewsReturns404() throws Exception {
        doThrow(new NoReviewsFoundException()).when(getAllReviewsUseCase).getReviews();

        mockMvc.perform(get("/reviews").contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isNotFound());

        verify(getAllReviewsUseCase, times(1)).getReviews();
    }

    @Test
    void getAllReviewsForPropertyReturns200() throws Exception {
        Instant date = Instant.parse("2022-10-10T00:00:00Z");

        Review review = Review.builder()
                .reviewId(1L)
                .propertyId(1L)
                .userId(1L)
                .text("Very nice")
                .rating(7)
                .date(date)
                .build();

        GetAllReviewsResponse response = GetAllReviewsResponse.builder().allReviews(List.of(review)).build();

        when(getReviewsForPropertyUseCase.getReviews(1)).thenReturn(response);

        mockMvc.perform(get("/reviews/{propertyId}", 1).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json(""" 
                         {
                          "allReviews": [
                              {
                                  "reviewId": 1,
                                  "propertyId": 1,
                                  "userId": 1,
                                  "text": "Very nice",
                                  "rating": 7,
                                  "date": "2022-10-10T00:00:00Z"
                              }
                          ]
                        }"""));
        verify(getReviewsForPropertyUseCase, times(1)).getReviews(1);
    }

    @Test
    void getAllReviewsForPropertyReturns404() throws Exception {
        doThrow(new NoReviewsFoundException()).when(getReviewsForPropertyUseCase).getReviews(1);

        mockMvc.perform(get("/reviews/{propertyId}", 1).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isNotFound());

        verify(getReviewsForPropertyUseCase, times(1)).getReviews(1);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteReviewReturns204() throws Exception {
        Instant date = Instant.parse("2022-10-10T00:00:00Z");

        Review review = Review.builder()
                .reviewId(1L)
                .propertyId(1L)
                .userId(1L)
                .text("Very nice")
                .rating(7)
                .date(date)
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/reviews/{reviewId}", review.getReviewId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(deleteReviewUseCase, times(1)).deleteReview(review.getReviewId());

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteReviewReturns404() throws Exception {
        Instant date = Instant.parse("2022-10-10T00:00:00Z");

        Review review = Review.builder()
                .reviewId(1L)
                .propertyId(1L)
                .userId(1L)
                .text("Very nice")
                .rating(7)
                .date(date)
                .build();

        doThrow(new PropertyIsNotFoundException()).when(deleteReviewUseCase).deleteReview(review.getReviewId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/reviews/{reviewId}", review.getReviewId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(deleteReviewUseCase, times(1)).deleteReview(review.getReviewId());

    }

    @Test
    @WithMockUser(username = "host", roles = {"HOST"})
    void deleteReviewReturns403() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/reviews/{reviewId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @Test
    void deleteReviewReturns401() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/reviews/{reviewId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }
}
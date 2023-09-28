package getawaygo_project.getawaygo_backend.controller;

import getawaygo_project.getawaygo_backend.business.*;
import getawaygo_project.getawaygo_backend.business.exception.NoReviewsFoundException;
import getawaygo_project.getawaygo_backend.business.exception.PropertyIsNotFoundException;
import getawaygo_project.getawaygo_backend.business.exception.ReviewNotFoundException;
import getawaygo_project.getawaygo_backend.business.exception.UserNotFoundException;
import getawaygo_project.getawaygo_backend.domain.*;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {
    @Mock
    private CreateReviewUseCase createReviewUseCase;
    @Mock
    private DeleteReviewUseCase deleteReviewUseCase;
    @Mock
    private GetAllReviewsUseCase getAllReviewsUseCase;
    @Mock
    private GetReviewsForPropertyUseCase getReviewsForPropertyUseCase;
    @Mock
    private GetAverageReviewUseCase getAverageReviewUseCase;
    @InjectMocks
    private ReviewController reviewController;

    @Test
    void createReview() {
        UserEntity user = new UserEntity();
        user.setUserId(3);
        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);
        CreateReviewRequest request = CreateReviewRequest.builder()
                .text("Test")
                .userId(user.getUserId())
                .propertyId(property.getPropertyId())
                .build();
        Review review = Review.builder()
                .text("Test")
                .userId(user.getUserId())
                .propertyId(property.getPropertyId())
                .build();
        CreateReviewResponse response = CreateReviewResponse.builder()
                .reviewResponse(review)
                .build();

        when(createReviewUseCase.createReview(request)).thenReturn(response);

        ResponseEntity<CreateReviewResponse> createResponse = reviewController.createReview(request);

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertEquals(request.getText(), createResponse.getBody().getReviewResponse().getText());
        verify(createReviewUseCase).createReview(request);
    }

    @Test
    void createReviewPropertyNotFound() {
        UserEntity user = new UserEntity();
        user.setUserId(3);
        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);
        CreateReviewRequest request = CreateReviewRequest.builder()
                .text("Test")
                .userId(user.getUserId())
                .propertyId(property.getPropertyId())
                .build();

        when(createReviewUseCase.createReview(request)).thenThrow(new PropertyIsNotFoundException());

        assertThrows(PropertyIsNotFoundException.class, () -> reviewController.createReview(request));

        verify(createReviewUseCase).createReview(request);
    }

    @Test
    void createReviewUserNotFound() {
        UserEntity user = new UserEntity();
        user.setUserId(3);
        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);
        CreateReviewRequest request = CreateReviewRequest.builder()
                .text("Test")
                .userId(user.getUserId())
                .propertyId(property.getPropertyId())
                .build();

        when(createReviewUseCase.createReview(request)).thenThrow(new UserNotFoundException());

        assertThrows(UserNotFoundException.class, () -> reviewController.createReview(request));

        verify(createReviewUseCase).createReview(request);
    }

    @Test
    void getAllReviews() {
        List<Review> reviewList = new ArrayList<>();
        reviewList.add(new Review());
        reviewList.add(new Review());
        GetAllReviewsResponse getResponse = new GetAllReviewsResponse(reviewList);

        when(getAllReviewsUseCase.getReviews()).thenReturn(getResponse);

        ResponseEntity<GetAllReviewsResponse> reviews = reviewController.getAllReviews();

        assertEquals(getResponse, reviews.getBody());
        assertEquals(HttpStatus.OK, reviews.getStatusCode());
        verify(getAllReviewsUseCase).getReviews();
    }

    @Test
    void getAverageReview() {
        AverageReviewDTO dto = new AverageReviewDTO();

        when(getAverageReviewUseCase.getAverageReview(1L)).thenReturn(dto);

        ResponseEntity<AverageReviewDTO> response = reviewController.getAverageReview(1L);

        assertEquals(dto, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(getAverageReviewUseCase).getAverageReview(1L);
    }
    @Test
    void getAllReviewsNoFound() {
        when(getAllReviewsUseCase.getReviews()).thenThrow(new NoReviewsFoundException());

        assertThrows(NoReviewsFoundException.class, () -> reviewController.getAllReviews());
        verify(getAllReviewsUseCase).getReviews();
    }

    @Test
    void getAllReviewsForProperty() {
        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(new Review());
        reviewList.add(new Review());
        GetAllReviewsResponse getResponse = new GetAllReviewsResponse(reviewList);

        when(getReviewsForPropertyUseCase.getReviews(property.getPropertyId())).thenReturn(getResponse);

        ResponseEntity<GetAllReviewsResponse> reviews = reviewController.getAllReviewsForProperty(property.getPropertyId());

        assertEquals(getResponse, reviews.getBody());
        assertEquals(HttpStatus.OK, reviews.getStatusCode());
        verify(getReviewsForPropertyUseCase).getReviews(property.getPropertyId());
    }

    @Test
    void getAllReviewsForPropertyNoFound() {
        Long id = 1L;

        when(getReviewsForPropertyUseCase.getReviews(id)).thenThrow(new NoReviewsFoundException());

        assertThrows(NoReviewsFoundException.class, () -> reviewController.getAllReviewsForProperty(id));

        verify(getReviewsForPropertyUseCase).getReviews(id);
    }

    @Test
    void deleteReview() {
        UserEntity user = new UserEntity();
        user.setUserId(3);
        Property property = Property.builder()
                .propertyId(1)
                .name("Test")
                .userId(user.getUserId())
                .build();
        Review review = Review.builder()
                .reviewId(1)
                .text("test")
                .userId(user.getUserId())
                .propertyId(property.getPropertyId())
                .build();
        doNothing().when(deleteReviewUseCase).deleteReview(review.getReviewId());
        ResponseEntity<Void> deleteResult = reviewController.deleteReview(review.getReviewId());

        assertEquals(HttpStatus.NO_CONTENT, deleteResult.getStatusCode());
        verify(deleteReviewUseCase).deleteReview(review.getReviewId());
    }

    @Test
    void deleteReviewNotFound() {
        Long id = 1L;

        doThrow(new ReviewNotFoundException()).when(deleteReviewUseCase).deleteReview(id);

        assertThrows(ReviewNotFoundException.class, () -> reviewController.deleteReview(id));

        verify(deleteReviewUseCase).deleteReview(id);
    }
}
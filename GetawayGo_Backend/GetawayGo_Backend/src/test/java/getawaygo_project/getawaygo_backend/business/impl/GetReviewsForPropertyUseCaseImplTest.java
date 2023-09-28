package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.exception.NoReviewsFoundException;
import getawaygo_project.getawaygo_backend.domain.GetAllReviewsResponse;
import getawaygo_project.getawaygo_backend.persistance.ReviewRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.ReviewEntity;
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
class GetReviewsForPropertyUseCaseImplTest {
    @Mock
    private ReviewRepository reviewRepository;
    @InjectMocks
    private GetReviewsForPropertyUseCaseImpl getReviewsForPropertyUseCase;

    @Test
    void getReviews() {
        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);

        PropertyEntity property2 = new PropertyEntity();
        property.setPropertyId(2);

        ReviewEntity review1 = new ReviewEntity();
        review1.setReviewId(1);
        review1.setUserId(new UserEntity());
        review1.setPropertyId(property);

        ReviewEntity review2 = new ReviewEntity();
        review2.setReviewId(2);
        review2.setUserId(new UserEntity());
        review2.setPropertyId(property);

        ReviewEntity review3 = new ReviewEntity();
        review3.setReviewId(2);
        review3.setUserId(new UserEntity());
        review3.setPropertyId(property2);

        List<ReviewEntity> reviews = new ArrayList<>();
        reviews.add(review1);
        reviews.add(review2);
        reviews.add(review3);

        when(reviewRepository.findAll()).thenReturn(reviews);

        GetAllReviewsResponse response = getReviewsForPropertyUseCase.getReviews(property.getPropertyId());

        assertEquals(2, response.getAllReviews().size());

        verify(reviewRepository).findAll();
    }

    @Test
    void getReviewsNoFound() {
        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);

        ReviewEntity review1 = new ReviewEntity();
        review1.setReviewId(1);
        review1.setUserId(new UserEntity());
        review1.setPropertyId(property);

        ReviewEntity review2 = new ReviewEntity();
        review2.setReviewId(2);
        review2.setUserId(new UserEntity());
        review2.setPropertyId(property);

        List<ReviewEntity> reviews = new ArrayList<>();
        reviews.add(review1);
        reviews.add(review2);

        when(reviewRepository.findAll()).thenReturn(reviews);

        assertThrows(NoReviewsFoundException.class, () -> getReviewsForPropertyUseCase.getReviews(5));

        verify(reviewRepository).findAll();
    }
}
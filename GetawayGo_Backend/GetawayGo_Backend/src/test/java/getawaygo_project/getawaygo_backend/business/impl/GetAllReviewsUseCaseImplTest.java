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
class GetAllReviewsUseCaseImplTest {
    @Mock
    private ReviewRepository reviewRepository;
    @InjectMocks
    private GetAllReviewsUseCaseImpl getAllReviewsUseCase;

    @Test
    void getReviews() {
        ReviewEntity review1 = new ReviewEntity();
        review1.setReviewId(1);
        review1.setUserId(new UserEntity());
        review1.setPropertyId(new PropertyEntity());

        ReviewEntity review2 = new ReviewEntity();
        review2.setReviewId(2);
        review2.setUserId(new UserEntity());
        review2.setPropertyId(new PropertyEntity());

        ReviewEntity review3 = new ReviewEntity();
        review3.setReviewId(2);
        review3.setUserId(new UserEntity());
        review3.setPropertyId(new PropertyEntity());

        List<ReviewEntity> reviews = new ArrayList<>();
        reviews.add(review1);
        reviews.add(review2);
        reviews.add(review3);

        when(reviewRepository.findAll()).thenReturn(reviews);

        GetAllReviewsResponse response = getAllReviewsUseCase.getReviews();

        assertEquals(reviews.size(), response.getAllReviews().size());
        assertEquals(1, response.getAllReviews().stream().findFirst().get().getReviewId());

        verify(reviewRepository).findAll();

    }

    @Test
    void getReviewsNoFound() {
        List<ReviewEntity> reviews = new ArrayList<>();

        when(reviewRepository.findAll()).thenReturn(reviews);

        assertThrows(NoReviewsFoundException.class, () -> getAllReviewsUseCase.getReviews());

        verify(reviewRepository).findAll();

    }
}
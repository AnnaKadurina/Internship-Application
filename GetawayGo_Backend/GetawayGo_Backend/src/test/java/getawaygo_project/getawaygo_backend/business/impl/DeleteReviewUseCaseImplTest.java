package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.exception.ReviewNotFoundException;
import getawaygo_project.getawaygo_backend.persistance.ReviewRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.ReviewEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteReviewUseCaseImplTest {
    @Mock
    private ReviewRepository reviewRepository;
    @InjectMocks
    private DeleteReviewUseCaseImpl deleteReviewUseCase;

    @Test
    void deleteReview() {
        UserEntity user = new UserEntity();
        user.setUserId(3);

        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);

        ReviewEntity review = new ReviewEntity();
        review.setReviewId(1);
        review.setPropertyId(property);
        review.setUserId(user);
        review.setText("test");
        review.setDate(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        review.setRating(1);

        when(reviewRepository.existsById(review.getReviewId())).thenReturn(true);
        deleteReviewUseCase.deleteReview(review.getReviewId());

        assertEquals(Optional.empty(), reviewRepository.findById(review.getReviewId()));

        verify(reviewRepository).existsById(review.getReviewId());
        verify(reviewRepository).deleteById(review.getReviewId());


    }

    @Test
    void deleteReviewNotFound() {
        Long id = 1L;

        when(reviewRepository.existsById(id)).thenReturn(false);

        assertThrows(ReviewNotFoundException.class, () -> deleteReviewUseCase.deleteReview(id));

        verify(reviewRepository).existsById(id);

    }
}
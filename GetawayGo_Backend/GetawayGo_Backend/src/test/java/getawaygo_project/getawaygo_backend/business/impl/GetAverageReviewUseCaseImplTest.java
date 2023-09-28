package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.exception.NoReviewsFoundException;
import getawaygo_project.getawaygo_backend.business.exception.PropertyIsNotFoundException;
import getawaygo_project.getawaygo_backend.domain.AverageReviewDTO;
import getawaygo_project.getawaygo_backend.persistance.PropertyRepository;
import getawaygo_project.getawaygo_backend.persistance.ReviewRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.ReviewEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAverageReviewUseCaseImplTest {
    @Mock
    private PropertyRepository propertyRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @InjectMocks
    private GetAverageReviewUseCaseImpl getAverageReviewUseCase;

    @Test
    void getAverageReview() {
        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);

        ReviewEntity review = new ReviewEntity();
        review.setReviewId(1);
        review.setPropertyId(property);
        review.setRating(10);

        when(propertyRepository.findById(property.getPropertyId())).thenReturn(Optional.of(property));
        when(reviewRepository.findByPropertyId(property)).thenReturn(List.of(review));
        when(reviewRepository.countReviewsForProperty(property.getPropertyId())).thenReturn(1L);
        when(reviewRepository.getAverageRatingForProperty(property.getPropertyId())).thenReturn(10.0);

        AverageReviewDTO dto = getAverageReviewUseCase.getAverageReview(property.getPropertyId());

        assertEquals(10.0, dto.getAverageRating());
        assertEquals(1L, dto.getTotalCount());

        verify(propertyRepository, times(1)).findById(property.getPropertyId());
        verify(reviewRepository, times(1)).findByPropertyId(property);
    }

    @Test
    void getAverageReviewPropertyIsNotFound() {
        Long id = 1L;

        when(propertyRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PropertyIsNotFoundException.class, () -> getAverageReviewUseCase.getAverageReview(id));

        verify(propertyRepository, times(1)).findById(id);
    }

    @Test
    void getAverageReviewNoReviewsForProperty() {
        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);
        Long id = 1L;

        when(propertyRepository.findById(property.getPropertyId())).thenReturn(Optional.of(property));
        when(reviewRepository.findByPropertyId(property)).thenReturn(Collections.emptyList());

        AverageReviewDTO dto = getAverageReviewUseCase.getAverageReview(id);

        assertEquals(0.00, dto.getAverageRating());
        assertEquals(0L, dto.getTotalCount());

        verify(propertyRepository, times(1)).findById(property.getPropertyId());
        verify(reviewRepository, times(1)).findByPropertyId(property);
    }
}
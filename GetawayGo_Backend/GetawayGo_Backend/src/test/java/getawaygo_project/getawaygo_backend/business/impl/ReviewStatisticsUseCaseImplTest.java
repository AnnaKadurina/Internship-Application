package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.exception.UnauthorizedDataException;
import getawaygo_project.getawaygo_backend.business.exception.UserNotFoundException;
import getawaygo_project.getawaygo_backend.domain.AccessToken;
import getawaygo_project.getawaygo_backend.domain.ReviewStatisticsDTO;
import getawaygo_project.getawaygo_backend.persistance.PropertyRepository;
import getawaygo_project.getawaygo_backend.persistance.ReviewRepository;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.ReviewEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewStatisticsUseCaseImplTest {
    @Mock
    private PropertyRepository propertyRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AccessToken accessToken;
    @InjectMocks
    private ReviewStatisticsUseCaseImpl reviewStatisticsUseCase;

    @Test
    void getReviewStatistics() {
        UserEntity user = new UserEntity();
        user.setUserId(1);

        UserEntity user2 = new UserEntity();
        user2.setUserId(2);

        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);
        property.setUser(user);

        PropertyEntity property2 = new PropertyEntity();
        property2.setPropertyId(2);
        property2.setUser(user2);

        ReviewEntity review = new ReviewEntity();
        review.setReviewId(1);
        review.setPropertyId(property);
        review.setRating(10);

        ReviewEntity review3 = new ReviewEntity();
        review3.setReviewId(3);
        review3.setPropertyId(property);
        review3.setRating(1);

        when(propertyRepository.findByUser(user)).thenReturn(List.of(property, property2));
        when(reviewRepository.findByPropertyId(property)).thenReturn(List.of(review, review3));
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(accessToken.getUserId()).thenReturn(user.getUserId());

        ReviewStatisticsDTO statisticsDTO = reviewStatisticsUseCase.getReviewStatistics(user.getUserId());

        assertEquals(1, statisticsDTO.getPositiveCount());
        assertEquals(1, statisticsDTO.getNegativeCount());

        verify(propertyRepository, times(1)).findByUser(user);
        verify(reviewRepository, times(1)).findByPropertyId(property);
        verify(userRepository, times(1)).findById(user.getUserId());
        verify(accessToken, times(1)).getUserId();

    }

    @Test
    void getReviewStatisticsReturnsUnauthorized() {
        long id = 1L;

        when(accessToken.getUserId()).thenReturn(2L);

        assertThrows(UnauthorizedDataException.class, () -> reviewStatisticsUseCase.getReviewStatistics(id));

        verify(accessToken, times(1)).getUserId();
    }

    @Test
    void getReviewStatisticsUserNotFound() {
        Long id = 1L;

        when(accessToken.getUserId()).thenReturn(id);
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> reviewStatisticsUseCase.getReviewStatistics(id));

        verify(userRepository, times(1)).findById(id);
        verify(accessToken, times(1)).getUserId();
    }
}
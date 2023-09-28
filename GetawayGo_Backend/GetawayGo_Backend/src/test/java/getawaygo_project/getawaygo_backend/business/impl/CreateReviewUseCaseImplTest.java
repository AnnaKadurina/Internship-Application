package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.exception.PropertyIsNotFoundException;
import getawaygo_project.getawaygo_backend.business.exception.UserNotFoundException;
import getawaygo_project.getawaygo_backend.domain.CreateReviewRequest;
import getawaygo_project.getawaygo_backend.domain.CreateReviewResponse;
import getawaygo_project.getawaygo_backend.persistance.PropertyRepository;
import getawaygo_project.getawaygo_backend.persistance.ReviewRepository;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateReviewUseCaseImplTest {
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private PropertyRepository propertyRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CreateReviewUseCaseImpl createReviewUseCase;

    @Test
    void createReview() {
        UserEntity user = new UserEntity();
        user.setUserId(3);
        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);
        CreateReviewRequest reviewRequest = CreateReviewRequest.builder()
                .text("test")
                .rating(1)
                .userId(3)
                .propertyId(1)
                .build();

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(propertyRepository.findById(property.getPropertyId())).thenReturn(Optional.of(property));

        CreateReviewResponse response = createReviewUseCase.createReview(reviewRequest);

        assertEquals(reviewRequest.getText(), response.getReviewResponse().getText());

        verify(userRepository).findById(user.getUserId());
        verify(propertyRepository).findById(property.getPropertyId());

    }
    @Test
    void createReviewPropertyNotFound() {
        UserEntity user = new UserEntity();
        user.setUserId(3);
        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);
        CreateReviewRequest reviewRequest = CreateReviewRequest.builder()
                .text("test")
                .rating(1)
                .userId(3)
                .propertyId(1)
                .build();

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(propertyRepository.findById(property.getPropertyId())).thenReturn(Optional.empty());

        assertThrows(PropertyIsNotFoundException.class, () -> createReviewUseCase.createReview(reviewRequest));

        verify(userRepository).findById(user.getUserId());
        verify(propertyRepository).findById(property.getPropertyId());

    }
    @Test
    void createReviewUserNotFound() {
        UserEntity user = new UserEntity();
        user.setUserId(3);
        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);
        CreateReviewRequest reviewRequest = CreateReviewRequest.builder()
                .text("test")
                .rating(1)
                .userId(3)
                .propertyId(1)
                .build();

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> createReviewUseCase.createReview(reviewRequest));

        verify(userRepository).findById(user.getUserId());
    }
}
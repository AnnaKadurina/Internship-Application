package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.CreateReviewUseCase;
import getawaygo_project.getawaygo_backend.business.exception.PropertyIsNotFoundException;
import getawaygo_project.getawaygo_backend.business.exception.UserNotFoundException;
import getawaygo_project.getawaygo_backend.domain.CreateReviewRequest;
import getawaygo_project.getawaygo_backend.domain.CreateReviewResponse;
import getawaygo_project.getawaygo_backend.persistance.PropertyRepository;
import getawaygo_project.getawaygo_backend.persistance.ReviewRepository;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.ReviewEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CreateReviewUseCaseImpl implements CreateReviewUseCase {
    private ReviewRepository reviewRepository;
    private UserRepository userRepository;
    private PropertyRepository propertyRepository;
    @Override
    public CreateReviewResponse createReview(CreateReviewRequest reviewRequest) {
        Optional<UserEntity> user = userRepository.findById(reviewRequest.getUserId());
        if (user.isEmpty())
            throw new UserNotFoundException();
        Optional<PropertyEntity> property = propertyRepository.findById(reviewRequest.getPropertyId());
        if (property.isEmpty())
            throw new PropertyIsNotFoundException();
        ReviewEntity createdReview = new ReviewEntity();
        createdReview.setText(reviewRequest.getText());
        createdReview.setDate(Instant.now());
        createdReview.setRating(reviewRequest.getRating());
        createdReview.setUserId(user.get());
        createdReview.setPropertyId(property.get());

        reviewRepository.save(createdReview);
        return CreateReviewResponse.builder()
                .reviewResponse(ReviewConverter.convert(createdReview))
                .build();
    }
}

package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.GetAverageReviewUseCase;
import getawaygo_project.getawaygo_backend.business.exception.PropertyIsNotFoundException;
import getawaygo_project.getawaygo_backend.domain.AverageReviewDTO;
import getawaygo_project.getawaygo_backend.persistance.PropertyRepository;
import getawaygo_project.getawaygo_backend.persistance.ReviewRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.ReviewEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GetAverageReviewUseCaseImpl implements GetAverageReviewUseCase {
    private ReviewRepository reviewRepository;
    private PropertyRepository propertyRepository;

    @Override
    public AverageReviewDTO getAverageReview(long propertyId) {
        Optional<PropertyEntity> property = propertyRepository.findById(propertyId);
        if (property.isEmpty())
            throw new PropertyIsNotFoundException();

        List<ReviewEntity> reviews = reviewRepository.findByPropertyId(property.get());
        if (reviews.isEmpty()) {
            return AverageReviewDTO.builder()
                    .averageRating(0.00)
                    .totalCount(0)
                    .build();
        } else {
            Long totalCount = reviewRepository.countReviewsForProperty(propertyId);
            Double average = reviewRepository.getAverageRatingForProperty(propertyId);

            return AverageReviewDTO.builder()
                    .averageRating(average)
                    .totalCount(totalCount)
                    .build();
        }
    }
}

package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.GetReviewsForPropertyUseCase;
import getawaygo_project.getawaygo_backend.business.exception.NoReviewsFoundException;
import getawaygo_project.getawaygo_backend.domain.GetAllReviewsResponse;
import getawaygo_project.getawaygo_backend.domain.Review;
import getawaygo_project.getawaygo_backend.persistance.ReviewRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.ReviewEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetReviewsForPropertyUseCaseImpl implements GetReviewsForPropertyUseCase {
    private ReviewRepository reviewRepository;
    @Override
    public GetAllReviewsResponse getReviews(long propertyId) {
        List<ReviewEntity> reviews = reviewRepository.findAll()
                .stream()
                .filter(review -> review.getPropertyId().getPropertyId() == propertyId)
                .toList();
        final GetAllReviewsResponse response = new GetAllReviewsResponse();
        List<Review> results = reviews.stream()
                .map(ReviewConverter::convert)
                .toList();
        if (results.isEmpty())
            throw new NoReviewsFoundException();
        else {
            response.setAllReviews(results);
            return response;
        }
    }
}

package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.GetAllReviewsUseCase;
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
public class GetAllReviewsUseCaseImpl implements GetAllReviewsUseCase {
    private ReviewRepository reviewRepository;
    @Override
    public GetAllReviewsResponse getReviews() {
        List<ReviewEntity> reviews = reviewRepository.findAll();
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

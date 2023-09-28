package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.DeleteReviewUseCase;
import getawaygo_project.getawaygo_backend.business.exception.ReviewNotFoundException;
import getawaygo_project.getawaygo_backend.persistance.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteReviewUseCaseImpl implements DeleteReviewUseCase {
    private ReviewRepository reviewRepository;
    @Override
    public void deleteReview(long reviewId) {
        if (reviewRepository.existsById(reviewId))
            this.reviewRepository.deleteById(reviewId);
        else
            throw new ReviewNotFoundException();
    }
}

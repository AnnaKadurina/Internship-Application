package getawaygo_project.getawaygo_backend.business;

import getawaygo_project.getawaygo_backend.domain.AverageReviewDTO;

public interface GetAverageReviewUseCase {
    AverageReviewDTO getAverageReview(long propertyId);
}

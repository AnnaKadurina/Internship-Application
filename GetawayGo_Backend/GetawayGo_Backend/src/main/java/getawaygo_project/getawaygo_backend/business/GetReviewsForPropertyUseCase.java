package getawaygo_project.getawaygo_backend.business;

import getawaygo_project.getawaygo_backend.domain.GetAllReviewsResponse;

public interface GetReviewsForPropertyUseCase {
    GetAllReviewsResponse getReviews(long propertyId);

}

package getawaygo_project.getawaygo_backend.business;

import getawaygo_project.getawaygo_backend.domain.CreateReviewRequest;
import getawaygo_project.getawaygo_backend.domain.CreateReviewResponse;

public interface CreateReviewUseCase {
    CreateReviewResponse createReview(CreateReviewRequest reviewRequest);

}

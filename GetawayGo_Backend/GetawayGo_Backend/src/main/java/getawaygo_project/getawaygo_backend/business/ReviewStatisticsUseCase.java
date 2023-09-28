package getawaygo_project.getawaygo_backend.business;

import getawaygo_project.getawaygo_backend.domain.ReviewStatisticsDTO;

public interface ReviewStatisticsUseCase {
    ReviewStatisticsDTO getReviewStatistics(long hostId);
}

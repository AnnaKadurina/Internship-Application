package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.ReviewStatisticsUseCase;
import getawaygo_project.getawaygo_backend.business.exception.UnauthorizedDataException;
import getawaygo_project.getawaygo_backend.business.exception.UserNotFoundException;
import getawaygo_project.getawaygo_backend.domain.AccessToken;
import getawaygo_project.getawaygo_backend.domain.ReviewStatisticsDTO;
import getawaygo_project.getawaygo_backend.persistance.PropertyRepository;
import getawaygo_project.getawaygo_backend.persistance.ReviewRepository;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.ReviewEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReviewStatisticsUseCaseImpl implements ReviewStatisticsUseCase {
    private ReviewRepository reviewRepository;
    private PropertyRepository propertyRepository;
    private UserRepository userRepository;
    private AccessToken accessToken;

    @Override
    public ReviewStatisticsDTO getReviewStatistics(long hostId) {
        if (accessToken.getUserId() != hostId)
            throw new UnauthorizedDataException();

        Optional<UserEntity> host = userRepository.findById(hostId);

        if (host.isEmpty())
            throw new UserNotFoundException();

        List<PropertyEntity> propertyEntities = propertyRepository.findByUser(host.get());
        List<ReviewEntity> reviews = new ArrayList<>();

        for (PropertyEntity property : propertyEntities) {
            reviews.addAll(reviewRepository.findByPropertyId(property));
        }

        long positiveCount = reviews.stream()
                .filter(review -> review.getRating() >= 5)
                .count();

        long negativeCount = reviews.size() - positiveCount;

        return ReviewStatisticsDTO.builder()
                .positiveCount(positiveCount)
                .negativeCount(negativeCount)
                .build();
    }
}

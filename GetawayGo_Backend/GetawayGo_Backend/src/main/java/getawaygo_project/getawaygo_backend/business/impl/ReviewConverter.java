package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.domain.Review;
import getawaygo_project.getawaygo_backend.persistance.entity.ReviewEntity;

public class ReviewConverter {
    private ReviewConverter(){}
    public static Review convert(ReviewEntity review){
        return Review.builder()
                .reviewId(review.getReviewId())
                .text(review.getText())
                .date(review.getDate())
                .rating(review.getRating())
                .userId(review.getUserId().getUserId())
                .propertyId(review.getPropertyId().getPropertyId())
                .build();
    }
}

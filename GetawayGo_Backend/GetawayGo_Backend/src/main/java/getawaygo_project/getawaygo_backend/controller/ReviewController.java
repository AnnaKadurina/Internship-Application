package getawaygo_project.getawaygo_backend.controller;

import getawaygo_project.getawaygo_backend.business.*;
import getawaygo_project.getawaygo_backend.configuration.isauthenticated.IsAuthenticated;
import getawaygo_project.getawaygo_backend.domain.AverageReviewDTO;
import getawaygo_project.getawaygo_backend.domain.CreateReviewRequest;
import getawaygo_project.getawaygo_backend.domain.CreateReviewResponse;
import getawaygo_project.getawaygo_backend.domain.GetAllReviewsResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@RequestMapping("/reviews")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class ReviewController {
    private final CreateReviewUseCase createReviewUseCase;
    private final DeleteReviewUseCase deleteReviewUseCase;
    private final GetAllReviewsUseCase getAllReviewsUseCase;
    private final GetReviewsForPropertyUseCase getReviewsForPropertyUseCase;
    private final GetAverageReviewUseCase getAverageReviewUseCase;

    @IsAuthenticated
    @RolesAllowed("ROLE_GUEST")
    @PostMapping()
    public ResponseEntity<CreateReviewResponse> createReview(@RequestBody @Valid CreateReviewRequest request) {
        CreateReviewResponse response = createReviewUseCase.createReview(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<GetAllReviewsResponse> getAllReviews() {
        GetAllReviewsResponse response = getAllReviewsUseCase.getReviews();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/average/{propertyId}")
    public ResponseEntity<AverageReviewDTO> getAverageReview(@PathVariable(value = "propertyId") long propertyId) {
        AverageReviewDTO response = getAverageReviewUseCase.getAverageReview(propertyId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{propertyId}")
    public ResponseEntity<GetAllReviewsResponse> getAllReviewsForProperty(@PathVariable("propertyId") long propertyId) {
        GetAllReviewsResponse response = getReviewsForPropertyUseCase.getReviews(propertyId);
        return ResponseEntity.ok(response);
    }

    @IsAuthenticated
    @RolesAllowed("ROLE_ADMIN")
    @DeleteMapping("{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable long reviewId) {
        deleteReviewUseCase.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}

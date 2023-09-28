package getawaygo_project.getawaygo_backend.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateReviewResponse {
    private Review reviewResponse;
}

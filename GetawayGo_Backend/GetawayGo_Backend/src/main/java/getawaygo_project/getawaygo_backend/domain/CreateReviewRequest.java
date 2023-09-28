package getawaygo_project.getawaygo_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateReviewRequest {
    @NotNull
    private long propertyId;
    @NotNull
    private long userId;
    @NotBlank
    private String text;
    @NotNull
    private int rating;
}

package getawaygo_project.getawaygo_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    private long reviewId;
    private long propertyId;
    private long userId;
    private String text;
    private int rating;
    private Instant date;

}

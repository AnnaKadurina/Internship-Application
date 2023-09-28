package getawaygo_project.getawaygo_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingRequest {
    @NotNull
    private long propertyId;
    @NotNull
    private long userId;
    @NotNull
    private Instant startDate;
    @NotNull
    private Instant endDate;
}

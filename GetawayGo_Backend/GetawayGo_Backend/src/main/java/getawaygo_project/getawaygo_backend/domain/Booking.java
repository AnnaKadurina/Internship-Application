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
public class Booking {
    private long bookingId;
    private Instant startDate;
    private Instant endDate;
    private double price;
    private long userId;
    private long propertyId;
}

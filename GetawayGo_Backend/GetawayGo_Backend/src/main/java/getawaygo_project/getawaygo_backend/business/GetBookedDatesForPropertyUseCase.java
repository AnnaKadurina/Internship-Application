package getawaygo_project.getawaygo_backend.business;

import java.time.Instant;
import java.util.List;

public interface GetBookedDatesForPropertyUseCase {
    List<Instant> getBookedDates(long propertyId);
}

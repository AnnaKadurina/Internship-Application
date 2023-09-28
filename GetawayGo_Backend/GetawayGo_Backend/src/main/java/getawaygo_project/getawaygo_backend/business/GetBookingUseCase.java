package getawaygo_project.getawaygo_backend.business;

import getawaygo_project.getawaygo_backend.domain.Booking;

import java.util.Optional;

public interface GetBookingUseCase {
    Optional<Booking> getBooking(long id);

}

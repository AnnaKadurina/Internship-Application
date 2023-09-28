package getawaygo_project.getawaygo_backend.business;

import getawaygo_project.getawaygo_backend.domain.CreateBookingRequest;
import getawaygo_project.getawaygo_backend.domain.CreateBookingResponse;

public interface CreateBookingUseCase {
    CreateBookingResponse createBooking(CreateBookingRequest bookingRequest);

}
